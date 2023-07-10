package microsleader.george.myvehiclesinfo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.qrolic.jetpackcomposecomponents.bottomnav.FavouriteScreen
import com.qrolic.jetpackcomposecomponents.bottomnav.SettingsScreen
import microsleader.george.myvehiclesinfo.bottomnav.CategoryScreen
import microsleader.george.myvehiclesinfo.bottomnav.HomeScreen
import microsleader.george.myvehiclesinfo.dialogs.AddVehicleDialog
import microsleader.george.myvehiclesinfo.ui.theme.MyVehiclesInfoTheme


class MainMenuActivity: ComponentActivity() {

    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val showAddVehicleDialog =  remember { mutableStateOf(false) }
            if (showAddVehicleDialog.value)
                AddVehicleDialog(value = "", setShowDialog = {showAddVehicleDialog.value = it}, setValue = {Log.i("HomePage","HomePage : $it")})

            MyVehiclesInfoTheme() {
                var topBarText by rememberSaveable { mutableStateOf("Home") }
                val context = LocalContext.current
                val navController = rememberNavController()


                LaunchedEffect(navController) {
                    navController.currentBackStackEntryFlow.collect { backStackEntry ->
                        // You can map the title based on the route using:
                        topBarText = getTitleByRoute( backStackEntry.destination.route)
                    }
                }


                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(topBarText)
                            },
                            actions = {
                                IconButton(onClick = {
                                    FirebaseAuth.getInstance().signOut()
                                    context.startActivity(Intent(context, LoginActivity::class.java))
                                }) {
                                    Icon(Icons.Filled.ExitToApp, contentDescription = null)
                                }
                            },
                        )
                    }

                    ,
                    bottomBar = {
                        BottomAppBar(
                            modifier = Modifier
                                .height(65.dp),
                            cutoutShape = CircleShape,
                            backgroundColor = MaterialTheme.colors.surface,
                            elevation = 10.dp
                        ) {
                            BottomNavigationBar(navController = navController)
                        }
                    }, content = { padding ->
                        NavHostContainer(navController = navController, padding = padding)
                    },
                    floatingActionButtonPosition = FabPosition.Center,
                    isFloatingActionButtonDocked = true,
                    floatingActionButton = {
                        FloatingActionButton(
                            shape = CircleShape,
                            onClick = {
                                showAddVehicleDialog.value = true;
                                Toast.makeText(context, "button clicked",Toast.LENGTH_LONG).show()
                            },
                            contentColor = MaterialTheme.colors.surface
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "add",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                )

            }
        }
    }

    private fun getTitleByRoute( route: String?): String {
        return when (route) {
            "home" -> "home"
            "category" -> "category"
            "favourite" -> "favourite"
            // other cases
            else -> "settings"
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun NavHostContainer(
    navController: NavHostController,
    padding: PaddingValues
) {

    NavHost(
        navController = navController,

        // set the start destination as home
        startDestination = "home",
        modifier = Modifier.padding(paddingValues = padding),

        builder = {

            // route : Home
            composable("home") {
                HomeScreen()
            }

            // route : furniture
            composable("category") {
                CategoryScreen()
            }


            // route : search
            composable("favourite") {
                FavouriteScreen()
            }

            // route : profile
            composable("setting") {
                SettingsScreen()
            }
        })

}

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    BottomNavigation(

        // set background color
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 22.dp
    ) {

        // observe the backstack
        val navBackStackEntry  by navController.currentBackStackEntryAsState()

        // observe current route to change the icon
        // color,label color when navigated
        val currentRoute = navBackStackEntry?.destination?.route

        // Bottom nav items we declared
        BottomNavItems.forEach { navItem ->

            // Place the bottom nav items
            BottomNavigationItem(

                unselectedContentColor = MaterialTheme.colors.onSurface,
                selectedContentColor = MaterialTheme.colors.primary,
                // it currentRoute is equal then its selected route
                selected = currentRoute == navItem.route,

                // navigate on click
                onClick = {

                    navController.navigate(navItem.route){
                        popUpTo(
                            navController.graph.startDestinationId
                        )
                        launchSingleTop = true
                    }
                },

                // Icon of navItem
                icon = {
                    Icon(
                        painter = painterResource(id = navItem.icon),
                        contentDescription = "afdersfs",

                        modifier = Modifier
                            .height(20.dp)
                            .width(32.dp)
                    )
                },
                label = { Text(text = navItem.label) },

                alwaysShowLabel = true
            )
        }
    }
}
