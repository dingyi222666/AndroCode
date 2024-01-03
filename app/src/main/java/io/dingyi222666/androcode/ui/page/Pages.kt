package io.dingyi222666.androcode.ui.page

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import io.dingyi222666.androcode.ui.page.main.MainPage
import io.dingyi222666.androcode.ui.resource.LocalNavController

sealed class Page(val route: String) {
    data object Home : Page("home_page")
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    CompositionLocalProvider(LocalNavController provides navController) {
        NavHost(navController = navController, startDestination = Page.Home.route) {
            composable(route = Page.Home.route) {
                MainPage()
            }
        }
    }
}

/*
fun NavGraphBuilder.shopGraph() {
    navigation(
        startDestination = ShopScreen.Shop.route,
        route = ShopScreen.Root.route
    ) {
        composable(route = ShopScreen.Shop.route) {
            ShopPage()
        }
        composable(route = ShopScreen.Product.route,
            arguments = listOf(navArgument(DestinationArg) { type = UserNavType() })
        ) {
            ProductPage()
        }
    }
} */