import SwiftUI
import ComposeApp

@main 
struct iOSApp: App {
    @StateObject private var coordinator = AppCoordinator()

    private let fastCore = FastCore(
        driver: getSqlDriver(context: nil),
        now: { KotlinLong(value: Int64(Date().timeIntervalSince1970 * 1000)) }
    )

    var body: some Scene {
        WindowGroup {
            MainView(coordinator: coordinator, fastCore: fastCore)
        }
    }
}
