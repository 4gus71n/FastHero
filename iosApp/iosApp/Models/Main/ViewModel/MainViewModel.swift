import Foundation
import Combine
import ComposeApp

class MainViewModel: ObservableObject {

    // MARK: - Properties

    private let userDefaults = UserDefaults.standard
    private var countDownTimer: AnyCancellable?

    @Published var feed: [TimelineEntry] = []
    @Published var time: String = "--:--"
    @Published var progress: Float = 0.0
    @Published var isRunning: Bool {
        didSet {
            print("Setting isRunning to:", isRunning)
            userDefaults.set(isRunning, forKey: "is_running")
        }
    }
    @Published var hasCompletedFast: Bool {
        didSet {
            print("Setting hasCompletedFast to:", hasCompletedFast)
            userDefaults.set(hasCompletedFast, forKey: "has_completed_fast")
        }
    }

    private var fastStartDate: Date? {
        get {
            let timestamp = userDefaults.double(forKey: "started_at")
            return timestamp > 0 ? Date(timeIntervalSince1970: timestamp) : nil
        }
        set {
            userDefaults.set(newValue?.timeIntervalSince1970 ?? 0, forKey: "started_at")
        }
    }

    var isFirstTime: Bool {
        get { userDefaults.bool(forKey: "is_first_time") }
        set { userDefaults.set(newValue, forKey: "is_first_time") }
    }

    private var fastId: String {
        get { userDefaults.string(forKey: "fast_id") ?? "" }
        set { userDefaults.set(newValue, forKey: "fast_id") }
    }

    // MARK: - Initializer

    init() {
        self.isRunning = userDefaults.bool(forKey: "is_running")
        self.hasCompletedFast = userDefaults.bool(forKey: "has_completed_fast")
    }

    // MARK: - Timer Methods

    func computeTimeLeft() -> Int {
        guard let fast = findSelectedFast(), let startDate = fastStartDate else { return 0 }
        let expectedEnd = startDate.timeIntervalSince1970 + Double(fast.fastingTimeInSeconds)
        let remainingSeconds = max(0, expectedEnd - Date().timeIntervalSince1970)
        return Int(remainingSeconds)
    }

    private func startTimer() {
        guard findSelectedFast() != nil, fastStartDate != nil else { return }
        countDownTimer?.cancel()
        countDownTimer = Timer.publish(every: 1, on: .main, in: .common)
            .autoconnect()
            .sink { [weak self] _ in
                guard let self = self else { return }

                let remainingTime = self.computeTimeLeft()
                self.time = self.formatClockTime(remainingTime)
                self.progress = Float(1.0 - (Double(remainingTime) / Double(self.findSelectedFast()?.fastingTimeInSeconds ?? 1))) * 100
                computeFeed()
                if remainingTime <= 0 {
                    self.hasCompletedFast = true
                    self.isRunning = false
                    countDownTimer?.cancel()
                }
            }
    }

    func toggleTimer() {
        if hasCompletedFast || isRunning {
            stopTimer()
        } else {
            guard findSelectedFast() != nil else {
                print("No fast selected!")
                return
            }
            fastStartDate = Date()
            hasCompletedFast = false
            isRunning = true
            startTimer()
        }
        resumeFast()
    }

    func stopTimer() {
        countDownTimer?.cancel()
        fastId = ""
        fastStartDate = nil
        isRunning = false
        hasCompletedFast = false
        time = formatClockTime(0)
        progress = 0.0
    }

    func resumeFast() {
        switch true {
            // A. Has fully completed a fast
            case hasCompletedFast:
                computeCongratsFeed()

            // B. Has selected a fast technique and the clock is running
            case isRunning && findSelectedFast() != nil:
                isFirstTime = false
                startTimer()

            // C. We just selected a Fast
            case !isRunning && findSelectedFast() != nil:
                computeHelpFeed()

            // D. Is not running and it is the first time
            case !isRunning && isFirstTime:
                computeWelcomeFeed()

            // E. It is not running
            case !isRunning && !isFirstTime:
                computeDefaultFeed()

            default:
                break
            }
    }


    // MARK: - Helpers

    func findSelectedFast() -> SettingOptionList? {
        return fastId.isEmpty ? nil : iOSMockData.fastingOptions.first { $0.id == fastId }
    }

    func setSelectedFast(fastId: String) {
        self.fastId = fastId
    }

    private func formatClockTime(_ seconds: Int) -> String {
        let hours = seconds / 3600
        let minutes = (seconds % 3600) / 60
        return String(format: "%02d:%02d", hours, minutes)
    }

    // MARK: - Feed Computation

    func computeWelcomeFeed() {
        feed = [TimelineEntry(title: "Welcome!", description: "Ready to begin your fasting journey? Tap start!", emoji: "😼", isPulsing: true)]
    }

    /**
     * Computes the fasting progress and updates the timeline feed.
     */
    func computeFeed() {
        guard let fast = findSelectedFast(), let fastingStarted = fastStartDate else { return }
        let currentTimeMillis = Date().timeIntervalSince1970 * 1000

        let milestoneTips: [TimelineEntry] = iOSMockData.fastingMilestones.enumerated().map { (index, milestone) in
            let fastingStartedMillis = fastingStarted.timeIntervalSince1970 * 1000
            let thisMilestoneMillis = fastingStartedMillis + (Double(milestone.timestampInSeconds) * 1000)

            let nextMilestone = iOSMockData .fastingMilestones[safe: index + 1]
            let nextMilestoneMillis = nextMilestone.map { fastingStartedMillis + (Double($0.timestampInSeconds) * 1000) }

            let completionState: Int
            if currentTimeMillis >= thisMilestoneMillis {
                completionState = (nextMilestoneMillis == nil || currentTimeMillis < nextMilestoneMillis!) ? 0 : -1
            } else {
                completionState = 1
            }

            return TimelineEntry(
                title: "\(formatFeedTime(Int(milestone.timestampInSeconds))) – \(milestone.title)",
                description: [
                    milestone.description,
                    milestone.warnings.map { "⚠️ \($0)" },
                    milestone.advice.map { "ℹ️️ \($0)" }
                ].compactMap { $0 }.joined(separator: "\n"),
                emoji: completionState == 0 ? "🔥" : (completionState == -1 ? "✅" : "☑️"),
                isPulsing: completionState == 0
            )
        }

        self.feed = milestoneTips
    }

    private func formatFeedTime(_ seconds: Int) -> String {
        let hours = seconds / 3600
        let minutes = (seconds % 3600) / 60
        switch (hours, minutes) {
            case (let h, let m) where h > 0 && m > 0: return "\(h)h \(m)m"
            case (let h, _) where h > 0: return "\(h)h"
            case (_, let m) where m > 0: return "\(m)m"
            default: return "0m"
        }
    }

    func computeHelpFeed() {
        let helpingTip = [
            TimelineEntry(
                title: "Let's go!",
                description: "Hit the start button to begin!",
                emoji: "🚀",
                isPulsing: true
            )
        ]
        feed = helpingTip
    }


    func computeDefaultFeed() {
        let tipList: [TimelineEntry] = [
                    TimelineEntry(title: "Fat for Fuel!", description: "When you fast, your body switches from burning sugar to burning fat for energy. It's like flipping a metabolic switch! 🔥", emoji: "💪", isPulsing: false),
                    TimelineEntry(title: "Autophagy Mode", description: "Fasting triggers autophagy, a process where your cells clean out damaged parts and recycle them. It's like a deep clean for your body! 🧹", emoji: "🧼", isPulsing: false),
                    TimelineEntry(title: "Brain Booster", description: "Fasting increases brain-derived neurotrophic factor (BDNF), which helps improve memory and learning. Stay sharp! 🧠", emoji: "🤓", isPulsing: false),
                    TimelineEntry(title: "Inflammation Fighter", description: "Studies show fasting can reduce inflammation, helping with conditions like arthritis and asthma. Bye-bye, aches! 👋", emoji: "🔥", isPulsing: false),
                    TimelineEntry(title: "Immune System Reset", description: "Going without food for a while encourages your body to recycle old immune cells and create new ones. A natural reboot! 🔄", emoji: "🛡️", isPulsing: false),
                    TimelineEntry(title: "Longevity Hack", description: "Fasting has been linked to increased lifespan in many studies. It might just help you age like fine wine! 🍷", emoji: "⏳", isPulsing: false),
                    TimelineEntry(title: "Metabolism Booster", description: "Intermittent fasting can speed up metabolism and improve fat-burning efficiency. More gains, less effort! 💥", emoji: "🏋️", isPulsing: false),
                    TimelineEntry(title: "Cell Repair Mode", description: "During fasting, cells repair themselves and remove harmful waste. It's like hitting the refresh button! 🔄", emoji: "🔧", isPulsing: false),
                    TimelineEntry(title: "Hunger Hormone Tamer", description: "Fasting helps regulate ghrelin, the hunger hormone, making it easier to control cravings. No more snack attacks! 🍕", emoji: "🚫", isPulsing: false),
                    TimelineEntry(title: "Heart Health Hero", description: "Intermittent fasting can lower blood pressure, cholesterol, and triglycerides, reducing heart disease risk. ❤️", emoji: "💖", isPulsing: false),
                    TimelineEntry(title: "Ketone Power", description: "Fasting boosts ketone production, which fuels your brain and muscles for improved performance! ⚡", emoji: "⚡", isPulsing: false),
                    TimelineEntry(title: "Insulin Sensitivity Boost", description: "Fasting helps regulate blood sugar and improve insulin sensitivity, lowering the risk of diabetes. 🍭", emoji: "📉", isPulsing: false),
                    TimelineEntry(title: "Gut Rest & Reset", description: "Fasting gives your digestive system a break, allowing it to heal and function better. No bloating, just bliss! 🌿", emoji: "🍃", isPulsing: false),
                    TimelineEntry(title: "Mood Enhancer", description: "Fasting can increase dopamine and serotonin, making you feel happier and more energized! 😊", emoji: "😄", isPulsing: false),
                    TimelineEntry(title: "More Growth Hormone", description: "Fasting can increase growth hormone levels by up to 5 times, aiding muscle gain and fat loss. Gains incoming! 💪", emoji: "🏆", isPulsing: false),
                    TimelineEntry(title: "Better Sleep", description: "Fasting can regulate your circadian rhythm and improve sleep quality. Say hello to deep rest! 💤", emoji: "🌙", isPulsing: false),
                    TimelineEntry(title: "Skin Glow Up", description: "Autophagy from fasting helps clear out damaged cells, making your skin look fresher and younger! ✨", emoji: "💆", isPulsing: false),
                    TimelineEntry(title: "Simpler Meal Planning", description: "Fewer meals = less cooking and fewer dishes. Fasting saves you time and effort! 🍽️", emoji: "⏰", isPulsing: false),
                    TimelineEntry(title: "A Natural Detox", description: "Your liver works better when fasting, helping flush out toxins naturally. No expensive juice cleanses needed! 🥤", emoji: "🚿", isPulsing: false),
                    TimelineEntry(title: "Saves Money", description: "Eating less often means spending less on food. Fasting is good for your wallet too! 💰", emoji: "🤑", isPulsing: false)
                ]

                // Shuffle the list and take the first 3 entries
                let shuffledTips = tipList.shuffled().prefix(3).map { $0 }
        feed = [TimelineEntry(title: "Did you know?", description: "Fasting has many benefits!", emoji: "❓", isPulsing: true)] + shuffledTips
    }

    func computeCongratsFeed() {
        feed = [TimelineEntry(title: "Hurray!", description: "You've successfully completed your fast!", emoji: "🎉", isPulsing: true)]
    }
}

// Helper extension to safely access an array element
extension Array {
    subscript(safe index: Int) -> Element? {
        return indices.contains(index) ? self[index] : nil
    }
}
