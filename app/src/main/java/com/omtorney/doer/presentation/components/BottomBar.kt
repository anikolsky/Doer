package com.omtorney.doer.presentation.components

import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.omtorney.doer.presentation.Screen

@Composable
fun BottomBar(
    accentColor: Long,
    navController: NavController
) {
    val navItems = arrayOf(
        Screen.Notes,
        Screen.Goals
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.background,
//        cutoutShape = MaterialTheme.shapes.small.copy(CornerSize(percent = 50)),
        tonalElevation = 0.dp,
//        modifier = Modifier.graphicsLayer {
//            shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
//            clip = true
//        }
    ) {
        navItems.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(item.icon!!),
                        contentDescription = item.label
                    )
                },
                label = { Text(text = item.label) },
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
