package com.omtorney.doer.presentation.screen.settings.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.omtorney.doer.data.remote.database.FirestoreUser
import com.omtorney.doer.firebase.presentation.FirestoreListItem
import com.omtorney.doer.firebase.presentation.FirestoreUserState
import com.omtorney.doer.presentation.screen.settings.SettingsEvent

@Composable
fun SignInCard(
    accentColor: Color,
    firestoreUser: FirestoreUser?,
    firestoreUserState: FirestoreUserState,
    onEvent: (SettingsEvent) -> Unit
) {
    val context = LocalContext.current

    Column(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .border(
                width = 1.dp,
                color = if (firestoreUser != null) Color.Cyan.copy(alpha = 0.5f) else accentColor,
                shape = RoundedCornerShape(6.dp)
            )
    ) {
        Text(
            text = "Аккаунт: ${firestoreUser?.name ?: "не используется"}",
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
        )
        Button(
            enabled = firestoreUser?.name != null,
            onClick = {
                if (firestoreUser != null) {
                    onEvent(SettingsEvent.CreateUser(firestoreUser, context))
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan.copy(alpha = 0.5f)),
            shape = MaterialTheme.shapes.extraSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 0.dp)
        ) {
            Text("Сохранение ${firestoreUser?.name ?: "недоступно"}")
        }
        when {
            firestoreUserState.isLoading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            !firestoreUserState.errorMessage.isNullOrEmpty() -> {
                Text(
                    text = firestoreUserState.errorMessage,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            firestoreUserState.data.isNullOrEmpty() -> {
                Text(
                    text = "Empty",
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            firestoreUserState.data.isNotEmpty() -> {
                LazyColumn(modifier = Modifier.padding(horizontal = 14.dp, vertical = 0.dp)) {
                    items(firestoreUserState.data) { item ->
                        FirestoreListItem(
                            userName = item?.name.toString(),
                            userId = item?.id.toString(),
                            onItemClick = {

                            },
                            onDeleteClick = { id, name ->
                                onEvent(SettingsEvent.DeleteUser(FirestoreUser(id, name), context))
                            }
                        )
                    }
                }
            }
        }
    }
}
