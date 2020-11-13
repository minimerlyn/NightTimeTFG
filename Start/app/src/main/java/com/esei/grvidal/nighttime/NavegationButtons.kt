package com.esei.grvidal.nighttime

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.ButtonConstants.defaultButtonColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.VectorAsset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.KEY_ROUTE
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigate
import androidx.ui.tooling.preview.Preview

/**
 * Bottom navigation icons with their route to the view
 *
 * @param route String that represents the route of the View
 * @param resourceId String from resources used to backtrack the users view
 * @param icon VectorAsset of the representeted icon
 */
sealed class BottomNavigationScreens(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: VectorAsset
) {
    object Calendar :
        BottomNavigationScreens("Calendar", R.string.calendar_route, Icons.Default.Today)

    object Bar :
        BottomNavigationScreens("Bar", R.string.bar_route, Icons.Default.LocalBar)

    object Friends :
        BottomNavigationScreens("Friends", R.string.friends_route, Icons.Default.People)

    object Profile :
        BottomNavigationScreens("Profile", R.string.profile_route, Icons.Default.Person)
}

/**
 * Screens of the App
 *
 * @param route String that represents the route of the View
 * @param resourceId String from resources used to backtrack the users view
 */
sealed class NavigationScreens(
    val route: String,
    @StringRes val resourceId: Int
) {
    object BarDetails :
        NavigationScreens("BarDetails", R.string.barDetails_route )

}


/**
 *  Formated view of the BottomBar
 *
 *  @param navController controller of the navigation
 *  @param items list of the bottom buttons
 *
 */
@Composable
fun bottomBarNavigation(
    navController: NavHostController,
    items: List<BottomNavigationScreens>
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colors.background
    ) {
        Column {
            Divider(
                modifier = Modifier.padding(3.dp),
                color = MaterialTheme.colors.onSurface,
                thickness = 1.dp
            )

            //Navigation Buttons
            Row(
                modifier = Modifier.padding(top = 6.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                val currentRoute = currentRoute(navController)
                items.forEach {screen ->
                    SelectableIconButton(
                        icon = screen.icon,
                        isSelected = currentRoute == screen.route,
                        onIconSelected = {
                            // This is the equivalent to popUpTo the start destination
                            navController.popBackStack(navController.graph.startDestination, false)

                            // This if check gives us a "singleTop" behavior where we do not create a
                            // second instance of the composable if we are already on that destination
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route)
                            }
                        }
                    )
                }
            }
        }
    }

}

/**
 * Method to recover the navigation's backtrack and return it as a string
 *
 * @param navController controller to be analyzed
 */
@Composable
private fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.arguments?.getString(KEY_ROUTE)
}


/**
 * Formatted Icons with the right color and the underline if they are selected
 *
 * @param icon VectorAsset of the own icon
 * @param onIconSelected setter of the selected navButtonsIcon
 * @param isSelected boolean that is true if the icon is selected
 * @param modifier Modifier
 *
 */
@Composable
fun SelectableIconButton(
    icon: VectorAsset,
    onIconSelected: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val tint = if (isSelected) {
        MaterialTheme.colors.primary
    } else {
        MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
    }
    Button(
        onClick = { onIconSelected() },
        shape = CircleShape,
        colors = defaultButtonColors(
            MaterialTheme.colors.background
        ),
        border = null,
        elevation = null,
        modifier = modifier
    ) {
        Column {
            Icon(icon, tint = tint)

            if (isSelected) {
                Box(
                    Modifier
                        .padding(top = 3.dp)
                        .preferredWidth(icon.defaultWidth)
                        .preferredHeight(1.dp)
                        .background(tint)
                )
            } else {
                Spacer(modifier = Modifier.preferredHeight(4.dp))
            }
        }
    }
}

@Preview("bottomBar")
@Composable
fun bottomBarPreview() {
    SelectableIconButton(Icons.Default.LocalBar,{},true)

}