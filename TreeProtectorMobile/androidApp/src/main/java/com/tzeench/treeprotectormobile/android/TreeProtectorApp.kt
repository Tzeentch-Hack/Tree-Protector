package com.tzeench.treeprotectormobile.android

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tzeench.treeprotectorfrontend.NavGraph

@Composable
fun TreeProtectorApp() {
    Scaffold { innerPaddingModifier ->
        Box {
          NavGraph(modifier = Modifier.padding(innerPaddingModifier))
        }
    }
}