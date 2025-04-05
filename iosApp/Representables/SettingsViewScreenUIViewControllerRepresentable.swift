import SwiftUI
import ComposeApp

public struct SettingsViewScreenRepresentable: UIViewControllerRepresentable {
    @Binding var state: SettingsViewState
    let onSlideChanged: (KotlinInt, KotlinInt, KotlinInt) -> Void
    let onOptionSelected: (String) -> Void
    let onBackButtonClicked: () -> Void

    public func makeUIViewController(context: Context) -> UIViewController {
        SettingsViewScreenUIViewController().make(onSlideChanged: onSlideChanged, onOptionSelected: onOptionSelected, onBackButtonClicked: onBackButtonClicked)
    }

    public func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
        SettingsViewScreenUIViewController().update(state: state)
    }
}