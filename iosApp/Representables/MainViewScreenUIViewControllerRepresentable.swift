import SwiftUI
import ComposeApp

public struct MainViewScreenRepresentable: UIViewControllerRepresentable {
    @Binding var state: MainViewState
    let toggleTimer: () -> Void

    public func makeUIViewController(context: Context) -> UIViewController {
        MainViewScreenUIViewController().make(toggleTimer: toggleTimer)
    }

    public func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        MainViewScreenUIViewController().update(state: state)
    }
}