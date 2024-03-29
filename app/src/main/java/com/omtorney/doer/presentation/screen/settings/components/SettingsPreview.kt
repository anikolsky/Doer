package com.omtorney.doer.presentation.screen.settings.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.omtorney.doer.R
import com.omtorney.doer.presentation.theme.DoerTheme

@Preview(
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun SettingsPreview() {
    DoerTheme {
        Surface {
            Column {
                MenuButton(
                    accentColor = Color.Blue,
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_round_color),
                            contentDescription = "Worker"
                        )
                    },
                    title = { Text(text = "Button") },
                    subtitle = "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium",
                    buttonText = "Choose",
                    onClick = {}
                )
                MenuSwitcher(
                    accentColor = Color.Blue,
                    icon = {
                        Icon(
                            painter = painterResource(R.drawable.ic_outline_push_pin),
                            contentDescription = "Worker"
                        )
                    },
                    title = { Text(text = "Switcher") },
                    subtitle = "Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis",
                    state = true,
                    onCheckedChange = {}
                )
//                MenuDropdown(
//                    accentColor = Color.Blue,
//                    icon = {
//                        Icon(
//                            painter = painterResource(R.drawable.ic_round_timer),
//                            contentDescription = "Time period"
//                        )
//                    },
//                    title = { Text(text = "Dropdown") },
//                    subtitle = "Selected period: 15 minutes",
//                    expanded = false,
//                    onClickButton = {},
//                    onClickMenu = {},
//                    onDismissRequest = {}
//                )
            }
        }
    }
}
