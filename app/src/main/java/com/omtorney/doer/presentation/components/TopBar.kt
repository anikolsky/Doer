package com.omtorney.doer.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.omtorney.doer.R

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    content: @Composable (RowScope.() -> Unit),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        content()
    }
}

@Composable
fun AppName(
    modifier: Modifier = Modifier,
    accentColor: Long
) {
    Text(
        text = stringResource(R.string.app_name),
        style = MaterialTheme.typography.titleLarge.merge(
            TextStyle(
                color = contentColorFor(backgroundColor = Color(accentColor)),
                fontWeight = FontWeight.Bold
            )
        ),
        modifier = modifier.padding(start = 8.dp)
    )
}

@Composable
fun ScreenName(
    modifier: Modifier = Modifier,
    title: String,
    accentColor: Long
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge.merge(
            TextStyle(color = contentColorFor(backgroundColor = Color(accentColor)))
        ),
        modifier = modifier.padding(10.dp)
    )
}

@Composable
fun MoreButton(
    accentColor: Long,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = stringResource(R.string.sort),
            tint = contentColorFor(backgroundColor = Color(accentColor))
        )
    }
}

@Composable
fun SettingsButton(
    accentColor: Long,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Rounded.Settings,
            contentDescription = stringResource(R.string.settings),
            tint = contentColorFor(backgroundColor = Color(accentColor))
        )
    }
}

@Composable
fun BackButton(
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = Icons.Rounded.ArrowBack,
            contentDescription = stringResource(R.string.dismiss)
        )
    }
}
