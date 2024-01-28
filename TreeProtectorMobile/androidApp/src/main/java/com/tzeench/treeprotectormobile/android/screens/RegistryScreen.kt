package com.tzeench.treeprotectormobile.android.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.tzeench.treeprotectorfrontend.MenuSections
import com.tzeench.treeprotectormobile.android.R
import com.tzeench.treeprotectormobile.dto.GetAllTreesModelDto
import com.tzeench.treeprotectormobile.dto.TreeModelDto
import com.tzeench.treeprotectormobile.presenters.RegistryPresenter
import com.tzeench.treeprotectormobile.utils.RegistryUiState
import org.koin.compose.koinInject

@Composable
fun RegistryScreen(
    navController: NavController,
    registryPresenter: RegistryPresenter = koinInject()
) {
    LaunchedEffect(key1 = Unit) {
        registryPresenter.getAllTrees()
    }

    when(val result = registryPresenter.registryState.collectAsState().value) {
        is RegistryUiState.Result -> {
            Log.e("TAG", "RegistryScreen: Success")
            RegistryScreen(trees = result.result.trees)
        }
        is RegistryUiState.Loading -> {
            Log.e("TAG", "RegistryScreen: Loading")
        }
        else -> { Log.e("TAG", "RegistryScreen: Error ${result}") }
    }
}

@Composable
fun RegistryScreen(
    trees: List<TreeModelDto>
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding),
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(all = 8.dp)
            ) {
                itemsIndexed(trees) { index, item ->
                    Box(modifier = Modifier.fillMaxWidth()){
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            model = if (item.photoUrl.isNullOrEmpty()) painterResource(id = R.drawable.tree_clf) else item.photoUrl,
                            contentDescription = "loadPhoto",
                            contentScale = ContentScale.Crop,
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Device Id: ${item.deviceId}",
                                fontSize = 12.sp
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    modifier = Modifier.padding(end = 4.dp),
                                    text = "Tree type: ${item.treeType}",
                                    fontSize = 12.sp
                                )

                                Text(
                                    text = "lat, long: ${item.coordinates}",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun RegistryScreenPreview() {
    RegistryScreen(
        trees = emptyList()
    )
}
