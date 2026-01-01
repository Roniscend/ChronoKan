sealed class Screen(val route: String) {
    object Todo : Screen("todo")
    object Progress : Screen("progress")
    object Done : Screen("done")
}