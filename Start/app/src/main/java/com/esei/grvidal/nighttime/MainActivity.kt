package com.esei.grvidal.nighttime

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.ui.tooling.preview.Preview

import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import com.esei.grvidal.nighttime.chatutil.ChatConversationPage
import com.esei.grvidal.nighttime.data.*
import com.esei.grvidal.nighttime.datastore.DataStoreManager
import com.esei.grvidal.nighttime.pages.*
import com.esei.grvidal.nighttime.scaffold.*

import com.esei.grvidal.nighttime.ui.NightTimeTheme
import java.lang.StringBuilder

private const val TAG = "MainActivity"

// Iconos personales
// https://developer.android.com/studio/write/image-asset-studio?hl=es-419
class MainActivity : AppCompatActivity() {

    //val chat by viewModels<ChatViewModel>()

    /** Using kotlin delegate by viewModels returns an instance of ViewModel by lazy
     * so the object don't initialize until needed and if the Activity is destroyed and recreated afterwards
     * it will receive the same instance of ViewModel as it had previously
     * */
    private val barVM by viewModels<BarViewModel>()
    private val calendarVM by viewModels<CalendarViewModel>()

    /** This ViewModels need arguments in their constructors so we need to
     * use a Fabric to return a lazy initialization of the ViewModel
     */
    private lateinit var userVM: UserViewModel
    private lateinit var cityVM: CityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "{tags: AssistLogging} onCreate: userToken is going to be created")

        /**
         * [UserViewModel] and [CityViewModel] constructor requires a DataStoreManager instance, so we use [ViewModelProvider] with a
         * Factory [UserViewModelFactory] and [CityViewModelFactory] respectively to return a ViewModel by lazy
         */
        userVM = ViewModelProvider(
            this,
            UserViewModelFactory(DataStoreManager.getInstance(this))
        ).get(UserViewModel::class.java)

        cityVM = ViewModelProvider(
            this,
            CityViewModelFactory(DataStoreManager.getInstance(this))
        ).get(CityViewModel::class.java)


        setContent {

            NightTimeTheme {

                when (userVM.loggingState) {
                    LoginState.LOADING -> {

                        Log.d(TAG, "onCreate: pulling LoadingPage")
                        LoadingScreen()

                    }

                    LoginState.NO_DATA_STORED -> {

                        Log.d(TAG, "onCreate: pulling LoginPage")
                        LoginPage(userVM)//todo add register

                    }
                    LoginState.REFUSED -> {
                        Log.d(TAG, "onCreate: pulling LoginPage with message")
                        LoginPage(userVM, stringResource(id = R.string.loginError))

                    }

                    LoginState.ACCEPTED ->{

                        calendarVM.setUserToken(userVM.loggedUser)
                        Log.d(TAG, "onCreate: pulling MainScreen")
                        MainScreen(
                            userVM,
                            cityVM,
                            calendarVM,
                            barVM
                            //chat,
                            //onAddItem = chat::addItem,
                        )

                    }

                    LoginState.NO_NETWORK -> {

                        if (userVM.credentialsChecked) {

                            Log.d(TAG, "onCreate: pulling MainScreen")
                            MainScreen(
                                userVM,
                                cityVM,
                                calendarVM,
                                barVM
                                //chat,
                                //onAddItem = chat::addItem,
                            )
                        } else {
                            Log.d(
                                TAG,
                                "onCreate: pulling LoginPage, credentials ${userVM.credentialsChecked}"
                            )
                            LoginPage(userVM, stringResource(id = R.string.serverIsDown))
                        }

                    }

                    LoginState.EXCEPTION -> {
                        ErrorPage("Unexpected error")
                    }
                }

            }
        }
    }
}

/**
 * MainScreen with the function that will allow it to manage the navigation system
 */
@Composable
private fun MainScreen(
    user: UserViewModel,
    cityVM: CityViewModel,
    calendarVM: CalendarViewModel,
    barVM: BarViewModel
    //chat : ChatViewModel,
    //onAddItem: (Message) -> Unit,
) {
/* Actual Navigation system
        https://proandroiddev.com/implement-bottom-bar-navigation-in-jetpack-compose-b530b1cd9ee2

Navigation with their own files ( no dependencies )
    https://medium.com/google-developer-experts/how-to-handle-navigation-in-jetpack-compose-a9ac47f7f975
 */
    val navController = rememberNavController()

    val bottomNavigationItems = listOf(
        BottomNavigationScreens.BarNav,
        BottomNavigationScreens.CalendarNav,
        BottomNavigationScreens.FriendsNav,
        BottomNavigationScreens.ProfileNav
    )

    Log.d(TAG, "MainScreen: Starting navigation graph")
    NavHost(navController, startDestination = BottomNavigationScreens.CalendarNav.route) {
        composable(BottomNavigationScreens.CalendarNav.route) {// Calendar
            ScreenScaffolded(
                topBar = {
                    TopBarConstructor(
                        setCityDialog = cityVM::setDialog,
                        nameCity = cityVM.city.name
                    )
                },
                bottomBar = { BottomBarNavConstructor(navController, bottomNavigationItems) },
            ) {
                CityDialogConstructor(
                    cityDialog = cityVM.showDialog,
                    items = cityVM.allCities,
                    setCityDialog = cityVM::setDialog,
                    setCityId = cityVM::setCity
                )

                //Setting city to CalendarVM to make calls to api
                calendarVM.cityId = (cityVM.city.id)

                CalendarPage(calendarVM = calendarVM)
            }
        }

        composable(BottomNavigationScreens.BarNav.route) {// Bar
            ScreenScaffolded(
                topBar = {
                    TopBarConstructor(
                        setCityDialog = cityVM::setDialog,
                        nameCity = cityVM.city.name
                    )
                },
                bottomBar = { BottomBarNavConstructor(navController, bottomNavigationItems) },
            ) {
                CityDialogConstructor(
                    cityDialog = cityVM.showDialog,
                    items = cityVM.allCities,
                    setCityDialog = cityVM::setDialog,
                    setCityId = cityVM::setCity
                )
                barVM.city = cityVM.city
                BarPage( navController, barVM )
            }

        }
        composable(  // Bar details
            NavigationScreens.BarDetails.route + "/{barId}",
            arguments = listOf(navArgument("barId") { type = NavType.IntType })
        ) { backStackEntry ->
            //Sometimes Android would reorganize backStackEntry.arguments?.getLong  as an int and showing
            // W/Bundle: Key barId expected Long but value was a java.lang.Integer.  The default value 0 was returned.
            // So we send an int then transform it to long
            barVM.getSelectedBarDetails(backStackEntry.arguments?.getInt("barId")?.toLong() ?: -1L)

            ScreenScaffolded(
                modifier = Modifier
            ) {
                BarDetails(barVM, navController)
            }

        }

        composable(BottomNavigationScreens.FriendsNav.route) {

            ScreenScaffolded(
                topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) }) },
                bottomBar = { BottomBarNavConstructor(navController, bottomNavigationItems) },
            ) {
                FriendsPageView(navController)
            }
        }

        composable(
            NavigationScreens.ChatConversation.route + "/{ChatId}",
            arguments = listOf(navArgument("ChatId") { type = NavType.IntType })
        ) { backStackEntry ->
            ChatConversationPage(navController, backStackEntry.arguments?.getInt("ChatId"))

            //ChatConversationPage(navController, backStackEntry.arguments?.getInt("ChatId"))

        }

        composable(
            BottomNavigationScreens.ProfileNav.route
        ) {
            ScreenScaffolded(
                topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) }) },
                bottomBar = { BottomBarNavConstructor(navController, bottomNavigationItems) },
            ) {
                ProfilePageView(navController, User("me").id, user)//todo is hardcoded
            }
        }

        composable(
            BottomNavigationScreens.ProfileNav.route + "/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType })
        ) { backStackEntry ->

            ScreenScaffolded(
                topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) }) },
                bottomBar = { BottomBarNavConstructor(navController, bottomNavigationItems) },
            ) {
                ProfilePageView(navController, backStackEntry.arguments?.getInt("userId"), user)
            }
        }

        composable(
            NavigationScreens.ProfileEditor.route
        ) {

            ScreenScaffolded(
                topBar = { TopAppBar(title = { Text(text = stringResource(id = R.string.app_name)) }) },
                bottomBar = {},
            ) {
                ProfileEditorPage(navController)
            }
        }
    }
}

fun NavHostController.navigateWithId(route: String, id: Long) {

    val navString = StringBuilder()
        .append(route)
        .append("/")
        .append(id)
        .toString()
    this.navigate(navString)
}

@Composable
fun BottomBarNavConstructor(
    navController: NavHostController,
    bottomNavigationItems: List<BottomNavigationScreens>
) {
    BottomBarNavigation {
        val currentRoute = currentRoute(navController)
        bottomNavigationItems.forEach { screen ->
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

@Preview("Main Page")
@Composable
fun PreviewScreen() {
    NightTimeTheme {
        ScreenScaffolded {}


    }
}


//Weights
/*
Row() {
    Box(
        Modifier.weight(1f),
        backgroundColor = Color.Blue) {
        Text(text = "Weight = 1", color = Color.White)
    }
    Box(
        Modifier.weight(2f),
        backgroundColor = Color.Yellow
    ) {
        Text(text = "Weight = 2")
    }
}
*/

/*
Concepto principal: cuando agregues un estado interno a un elemento que admite composición,
evalúa si debe conservarse luego de los cambios de configuración o las interrupciones como las llamadas telefónicas.

De ser así, usa savedInstanceState para almacenar el estado.

var expanded by savedInstanceState { false }

 */
