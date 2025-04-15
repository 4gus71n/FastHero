import UIKit
import SwiftUI
import ComposeApp

struct MainView: View {
    @StateObject private var viewModel: SkieMainViewModel
    @State private var state = MainViewState(time: "--:--", progress: 0.0, isRunning: false, feed: [])

    @ObservedObject var coordinator: AppCoordinator

    init(coordinator: AppCoordinator, fastCore: FastCore) {
            self.coordinator = coordinator
            _viewModel = StateObject(wrappedValue: SkieMainViewModel(fastCore: fastCore))
        }

    var body: some View {
        NavigationStack(path: $coordinator.path) {
            VStack {
                MainViewScreenRepresentable(
                    state: $state,
                    toggleTimer: {
                        let selectedFast = viewModel.findSelectedFast()
                        if (selectedFast != nil) {
                            viewModel.toggleTimer()
                        } else {
                            coordinator.goToSettings()
                        }
                    }
                )
                .onReceive(viewModel.objectWillChange) {
                    state = MainViewState(
                        time: viewModel.time,
                        progress: viewModel.progress,
                        isRunning: viewModel.isRunning,
                        feed: viewModel.feed
                    )
                }
                .onReceive(viewModel.$hasCompletedFast) { _ in
                    viewModel.resumeFast()
                }
                .onAppear {
                    viewModel.resumeFast()
                }
                .ignoresSafeArea(.keyboard) // Compose has own keyboard handler
            }
            .navigationDestination(for: String.self) { value in
                if value == "SettingsView" {
                    SettingsView(coordinator: coordinator)
                        .environmentObject(viewModel)
                }
            }
        }
    }
}

