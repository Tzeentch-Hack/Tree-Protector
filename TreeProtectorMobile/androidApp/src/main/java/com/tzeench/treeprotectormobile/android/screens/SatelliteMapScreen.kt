package com.tzeench.treeprotectormobile.android.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.tzeench.treeprotectormobile.android.R
import com.tzeench.treeprotectormobile.presenters.SatellitePresenter
import com.tzeench.treeprotectormobile.utils.RegistryUiState
import com.tzeench.treeprotectormobile.utils.SatelliteUiState
import org.koin.compose.koinInject

@Composable
fun SatelliteMapScreen(
    navController: NavController,
    satellitePresenter: SatellitePresenter = koinInject()
) {

    LaunchedEffect(key1 = Unit) {
        satellitePresenter.makeTreeSatellitePhoto(x = 63.320852f, y = 41.323740f, deviceId = "bsdfjhasbdf")
    }

    when(val result = satellitePresenter.satelliteState.collectAsState().value) {
        is SatelliteUiState.Result -> {
            Log.e("TAG", "RegistryScreen: Success")
            SatelliteMapScreen(result.result.coloredPhotoUrl)
        }
        is SatelliteUiState.Loading -> {
            Log.e("TAG", "RegistryScreen: Loading")
        }
        else -> { Log.e("TAG", "RegistryScreen: Error ${result}") }
    }
}

@Composable
fun SatelliteMapScreen(
    photoUrl: String
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ){
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                model = photoUrl,
                contentDescription = "loadPhoto",
                contentScale = ContentScale.Crop,
            )
        }
    }
}