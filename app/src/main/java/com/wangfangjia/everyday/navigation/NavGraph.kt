package com.wangfangjia.everyday.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.wangfangjia.everyday.data.repository.DailyRepository
import com.wangfangjia.everyday.ui.edit.EditScreen
import com.wangfangjia.everyday.ui.home.HomeScreen

/**
 * 导航路由定义
 */
object Routes {
    const val HOME = "home?date={date}"
    const val EDIT = "edit/{date}"

    fun homeRoute(date: String? = null) = if (date != null) "home?date=$date" else "home"
    fun editRoute(date: String) = "edit/$date"
}

/**
 * 应用导航图
 */
@Composable
fun AppNavGraph(
    navController: NavHostController,
    repository: DailyRepository
) {
    NavHost(
        navController = navController,
        startDestination = Routes.homeRoute()
    ) {
        // 首页
        composable(
            route = Routes.HOME,
            arguments = listOf(
                navArgument("date") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val dateParam = backStackEntry.arguments?.getString("date")
            HomeScreen(
                repository = repository,
                initialDate = dateParam,
                onNavigateToEdit = { date ->
                    navController.navigate(Routes.editRoute(date))
                }
            )
        }

        // 编辑页
        composable(
            route = Routes.EDIT,
            arguments = listOf(
                navArgument("date") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: return@composable
            EditScreen(
                date = date,
                repository = repository,
                onNavigateBack = { returnDate ->
                    navController.popBackStack()
                    if (returnDate != null) {
                        navController.navigate(Routes.homeRoute(returnDate)) {
                            popUpTo(Routes.homeRoute()) { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}