package com.tzeench.treeprotectormobile.android.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.tzeench.treeprotectorfrontend.MenuSections
import com.tzeench.treeprotectormobile.android.R
import com.tzeench.treeprotectormobile.presenters.RegistryPresenter
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    navController: NavController
) {

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(MenuSections.CameraSection.destination)
                },
                shape = CircleShape
            ) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_photo_camera_24),
                    contentDescription = "Camera Icon"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        LazyVerticalGrid(
            modifier = Modifier.padding(innerPadding),
            columns = GridCells.Fixed(2)
        ) {

            items(2) { index ->
                Card(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(all = 8.dp),
                    onClick = {
                        if (index == 0) {
                            navController.navigate(MenuSections.RegistrySection.destination)
                        } else {

                        }
                    }
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (index == 0) "Registry" else "Maps",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun MenuScreenPreview() {
    MenuScreen(navController = rememberNavController())
}
