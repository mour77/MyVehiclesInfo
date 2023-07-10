package microsleader.george.myvehiclesinfo

data class BottomNavItem(
    val label: String,
    val icon: Int,
    val route: String
)

val BottomNavItems = listOf(
    BottomNavItem(
        label = "Home",
        icon = R.drawable.ic_launcher_foreground,
        route = "home"
    ),
    BottomNavItem(
        label = "Category",
        icon = R.drawable.ic_launcher_foreground,
        route = "category"
    ),

    BottomNavItem(
        label = "Favourite",
        icon = R.drawable.ic_launcher_foreground,
        route = "favourite"
    ),
    BottomNavItem(
        label = "Settings",
        icon = R.drawable.ic_launcher_foreground,
        route = "setting"
    )
)