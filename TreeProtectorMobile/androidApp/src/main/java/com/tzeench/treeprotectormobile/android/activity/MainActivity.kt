package com.tzeench.treeprotectormobile.android.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.tzeench.treeprotectormobile.android.TreeProtectorApp
import com.tzeench.treeprotectorfrontend.ui.theme.TreeProtectorFrontEndTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TreeProtectorFrontEndTheme {
                TreeProtectorApp()
            }
        }
    }
}
