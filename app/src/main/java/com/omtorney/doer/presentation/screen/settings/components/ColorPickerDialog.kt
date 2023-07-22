package com.omtorney.doer.presentation.screen.settings.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.skydoves.colorpicker.compose.*

@Composable
fun ColorPickerDialog(
    accentColor: Long,
    onChooseClick: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    val controller = rememberColorPickerController()

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(color = MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AlphaTile(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    controller = controller
                )
            }
            HsvColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .padding(10.dp),
                controller = controller,
                onColorChanged = {
                    Log.d("Color", it.hexCode)
                }
            )
            AlphaSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(35.dp),
                controller = controller,
                tileOddColor = Color.White,
                tileEvenColor = Color.Black
            )
            BrightnessSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .height(35.dp),
                controller = controller,
            )
            Row {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(Color(accentColor))
                ) {
                    Text(text = "Dismiss")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        onChooseClick(controller.selectedColor.value)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(Color(accentColor))
                ) {
                    Text(text = "Select")
                }
            }
        }
    }
}

sealed class ColorType {
    object Accent : ColorType()
    object Secondary : ColorType()
}
