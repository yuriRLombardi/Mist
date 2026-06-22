package com.example.mist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.mist.screen.addEdit.AddEditScreen
import com.example.mist.screen.list.ListScreen
import kotlinx.serialization.Serializable


@Serializable
object ListPlataformRoute

@Serializable
object ListGamesRoute

@Serializable
data class AddEditScreenRoute(val id: Long? = null)

@Composable
fun MistNavHost(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ListPlataformRoute){
        composable<ListPlataformRoute> {
            ListScreen(
                navigateToAddEditScreen = { id ->
                    navController.navigate(AddEditScreenRoute(id = id))
            })
        }

        composable<AddEditScreenRoute> { navBackStackEntry ->
            val addEditScreenRoute = navBackStackEntry.toRoute<AddEditScreenRoute>()

            AddEditScreen(
                id = addEditScreenRoute.id,
                navigateBack = {
                    navController.popBackStack()
                },
            )
        }
    }


}
