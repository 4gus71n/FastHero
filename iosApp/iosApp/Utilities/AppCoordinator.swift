import SwiftUI

class AppCoordinator: ObservableObject {
    @Published var path = NavigationPath()

    func goToSettings() {
        path.append("SettingsView") // Add a new route
    }

    func goBack() {
        path.removeLast() // Remove the last screen (go back)
    }

    func goToRoot() {
        path = NavigationPath() // Clears the stack, returning to the root view
    }
}
