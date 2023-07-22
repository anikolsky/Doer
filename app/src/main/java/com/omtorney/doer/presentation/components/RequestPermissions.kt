package com.omtorney.doer.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissions(
    permission: String,
    deniedMessage: String = "Grant permission here to use app, or do it manually in system settings",
    rationaleMessage: String = "Permissions are needed to use app properly",
    onDismiss: () -> Unit
) {
    val permissionState = rememberPermissionState(permission = permission)

    HandleRequest(
        permissionState = permissionState,
        deniedContent = { shouldShowRationale ->
            PermissionDeniedContent(
                deniedMessage = deniedMessage,
                rationaleMessage = rationaleMessage,
                shouldShowRationale = shouldShowRationale,
                onRequestPermission = { permissionState.launchPermissionRequest() }
            )
        },
        content = {
            Content(
                message = "Permissions granted!",
                buttonText = "Back",
                showButton = true,
                onClick = onDismiss
            )
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HandleRequest(
    permissionState: PermissionState,
    deniedContent: @Composable (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    when (permissionState.status) {
        PermissionStatus.Granted -> {
            content()
        }
        is PermissionStatus.Denied -> {
            deniedContent(permissionState.status.shouldShowRationale)
        }
    }
}

@Composable
fun Content(
    message: String,
    buttonText: String,
    showButton: Boolean = true,
    onClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp)
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        if (showButton) {
            Button(onClick = onClick) {
                Text(text = buttonText)
            }
        }
    }
}

@Composable
fun PermissionDeniedContent(
    deniedMessage: String,
    rationaleMessage: String,
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit
) {
    if (shouldShowRationale) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = "Permission request",
                    style = TextStyle(
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = { Text(text = rationaleMessage) },
            confirmButton = {
                Button(onClick = onRequestPermission) {
                    Text(text = "Grant permissions")
                }
            }
        )
    } else {
        Content(
            message = deniedMessage,
            buttonText = "Request",
            onClick = onRequestPermission
        )
    }
}
