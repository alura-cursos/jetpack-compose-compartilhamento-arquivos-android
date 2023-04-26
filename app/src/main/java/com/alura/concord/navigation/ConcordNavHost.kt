package com.alura.concord.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun ConcordNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = chatListRoute,
        modifier = modifier,
    ) {
        chatListScreen(
            onOpenChat = { chatId ->
                navController.navigateToMessageScreen(chatId)
            }
        )

        messageListScreen(
            onBack = {
                navController.navigateUp()
            }
        )
    }
}


