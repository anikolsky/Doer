package com.omtorney.doer.core.presentation.components

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.omtorney.doer.core.presentation.Screen

@Composable
fun BottomBar(
    accentColor: Long,
    navController: NavController
) {
    val navItems = arrayOf(
        Screen.Home,
        Screen.Goals
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomAppBar(
        backgroundColor = Color(accentColor),
        cutoutShape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
        modifier = Modifier.graphicsLayer {
            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
            clip = true
        }
    ) {
        navItems.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        painter = painterResource(item.icon!!),
                        contentDescription = item.label
                    )
                },
                label = { Text(text = item.label!!) },
                alwaysShowLabel = true,
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(route = item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route = route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}