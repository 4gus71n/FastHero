import Foundation
import Combine
import ComposeApp

class SettingsViewModel: ObservableObject {
    @Published var fastingOptions: [SettingOptionList] = iOSMockData.fastingOptions

    struct FastingMetrics {
        let id: String
        let difficulty: Int
        let compromise: Int
        let doability: Int
    }

    private let wizardMatrix: [FastingMetrics] = [
        FastingMetrics(id: "12:12", difficulty: 2, compromise: 9, doability: 10),
        FastingMetrics(id: "14:10", difficulty: 4, compromise: 2, doability: 9),
        FastingMetrics(id: "16:8", difficulty: 5, compromise: 3, doability: 8),
        FastingMetrics(id: "18:6", difficulty: 7, compromise: 4, doability: 6),
        FastingMetrics(id: "20:4", difficulty: 8, compromise: 6, doability: 4),
        FastingMetrics(id: "OMAD", difficulty: 9, compromise: 7, doability: 3),
        FastingMetrics(id: "14:10 Crescendo", difficulty: 7, compromise: 1, doability: 9)
    ]

    func sortRecommendationList(difficulty: Int, compromise: Int, doability: Int) {
        let fastingOptions = iOSMockData.fastingOptions

        let matchingScores = fastingOptions.map { option -> (String, Int) in
            let metrics = wizardMatrix.first { $0.id == option.id }
            let score = metrics.map {
                abs(($0.difficulty - difficulty) +
                    ($0.compromise - compromise) +
                    ($0.doability - doability))
            } ?? 0
            return (option.id, score)
        }

        let newSortedList = matchingScores
            .sorted { $0.1 < $1.1 }
            .compactMap { pair in fastingOptions.first { $0.id == pair.0 } }

        DispatchQueue.main.async {
            self.fastingOptions = newSortedList
        }
    }
}
