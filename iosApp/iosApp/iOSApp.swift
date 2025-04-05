import SwiftUI

@main 
struct iOSApp: App {
    @StateObject private var coordinator = AppCoordinator()

    var body: some Scene {
        WindowGroup {
            MainView(coordinator: coordinator)
        }
    }
}
