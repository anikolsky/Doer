package com.omtorney.doer.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.omtorney.doer.ui.compose.DoerApp
import com.omtorney.doer.ui.theme.DoerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val launcher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.values.all { it }) {
                Toast.makeText(this, "Permissions are granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permissions are not granted", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                DoerTheme {
                    DoerApp()
                }
            }
        }

        checkPermissions()
    }

    /** Permissions */

    private fun checkPermissions() {
        val areAllGranted = REQUEST_PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }
        if (areAllGranted) {
//            Toast.makeText(this, "Permissions are granted!", Toast.LENGTH_SHORT).show()
        } else {
            launcher.launch(REQUEST_PERMISSIONS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            shouldShowRequestPermissionRationale(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
        }
    }

    companion object {
        private val REQUEST_PERMISSIONS: Array<String> = buildList {
//            add(Manifest.permission.INTERNET)
//            add(Manifest.permission.ACCESS_NETWORK_STATE)
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                add(Manifest.permission.POST_NOTIFICATIONS)
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                add(Manifest.permission.SCHEDULE_EXACT_ALARM)
//            }
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                add(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
            }

        }.toTypedArray()
    }
}