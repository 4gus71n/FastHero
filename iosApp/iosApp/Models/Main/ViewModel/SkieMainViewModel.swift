import Foundation
import Combine
import ComposeApp

class SkieMainViewModel: ObservableObject {
    private var cancellables = Set<AnyCancellable>()
    private let fastCore: FastCore

    @Published var feed: [TimelineEntry] = []
    @Published var time: String = ""
    @Published var progress: Float = 0.0
    @Published var isRunning: Bool = false
    @Published var hasCompletedFast: String = ""

    init(fastCore: FastCore) {
        self.fastCore = fastCore

        fastCore.feed.toPublisher()
            .receive(on: DispatchQueue.main)
            .sink { [weak self] entries in
                self?.feed = entries
            }
            .store(in: &cancellables)

        fastCore.time.toPublisher()
            .receive(on: DispatchQueue.main)
            .sink { [weak self] entries in
                self?.time = entries
            }
            .store(in: &cancellables)

        fastCore.progress.toPublisher()
            .receive(on: DispatchQueue.main)
            .sink { [weak self] entries in
                self?.progress = entries.floatValue
            }
            .store(in: &cancellables)

        fastCore.observeIsFastRunning.toPublisher()
            .receive(on: DispatchQueue.main)
            .sink { [weak self] entries in
                self?.isRunning = entries.boolValue
            }
            .store(in: &cancellables)

        Publishers.CombineLatest(
            fastCore.observeSelectedFastId.toPublisher(),
            fastCore.observeIsFastRunning.toPublisher()
        )
        .map { fastId, isRunning in
            return "\(fastId)\(isRunning)"
        }
        .removeDuplicates()
        .receive(on: DispatchQueue.main)
        .sink { [weak self] value in
            self?.hasCompletedFast = value
        }
        .store(in: &cancellables)
    }

    func toggleTimer() {
        fastCore.toggleTimer()

    }

    func findSelectedFast() -> SettingOptionList? {
        return fastCore.findSelectedFast()
    }

    func setSelectedFast(fastId: String) {
        self.fastCore.fastId = fastId
    }

    func resumeFast() {
        fastCore.resumeFast()
    }
}
