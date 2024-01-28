package com.tzeench.treeprotectormobile.android.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.location.LocationRequest
import android.os.Build
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings.Secure
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCapture.OutputFileOptions
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.androidbrowserhelper.locationdelegation.LocationProvider
import com.tzeench.treeprotectormobile.android.utils.Helper.convertUriToFile
import com.tzeench.treeprotectormobile.android.utils.getCameraProvider
import com.tzeench.treeprotectormobile.presenters.CameraPresenter
import com.tzeench.treeprotectormobile.utils.CameraUiState
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("MutableCollectionMutableState", "HardwareIds")
@Composable
fun CameraScreen(
    navController: NavController,
    presenter: CameraPresenter = koinInject()
) {

    when (presenter.cameraState.collectAsState().value) {
        CameraUiState.Initial -> {
            val file by remember {
                mutableStateOf(byteArrayOf())
            }
            val coroutineScope = rememberCoroutineScope()
            val lifecycleOwner = LocalLifecycleOwner.current
            val context = LocalContext.current
            val imageCapture by remember {
                mutableStateOf(ImageCapture.Builder().build())
            }
            val contentValues by remember {
                mutableStateOf(ContentValues())
            }
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "NEW_IMAGE")
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")

            val options by remember {
                mutableStateOf(
                    OutputFileOptions.Builder(
                        context.contentResolver,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                    ).build()
                )
            }

            val deviceId by remember {
                mutableStateOf(Secure.getString(context.contentResolver, Secure.ANDROID_ID))
            }
//            val locationUpdate: ((String) -> Unit)? = null
//            val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

//            var location by remember { mutableStateOf("No location") }
//            val locationProvider = remember { com.tzeench.treeprotectormobile.utils.LocationProvider(context) }

            var coordinates by remember { mutableStateOf("41.32374063.320852") }

            LaunchedEffect(key1 = Unit) {
//                if (ActivityCompat.checkSelfPermission(
//                        context,
//                        Manifest.permission.ACCESS_FINE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                        context,
//                        Manifest.permission.ACCESS_COARSE_LOCATION
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    val task: Task<Location> = fusedLocationClient.lastLocation
//                    task.addOnSuccessListener { location ->
//                        if (location != null) {
//                            val lat = location.latitude
//                            val long = location.longitude
//                            locationUpdate?.invoke("Latitude: $lat, Longitude: $long")
//                            coordinates = "$lat, $long"
//                            Log.e("CameraPreview", "coordinates = $coordinates", )
//                        } else {
//                            locationUpdate?.invoke("Unable to get location")
//                        }
//                    }
//                    task.addOnFailureListener {
//                        locationUpdate?.invoke("Error getting location")
//                    }
//                }

//                locationProvider.fetchLocation { result ->
//                    coordinates = result
//                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AndroidView(
                        modifier = Modifier
                            .weight(0.9f)
                            .fillMaxWidth(),
                        factory = { context ->
                            val previewView = PreviewView(context).apply {
                                this.scaleType = PreviewView.ScaleType.FIT_CENTER
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT
                                )
                            }

                            val previewUseCase = androidx.camera.core.Preview.Builder()
                                .build()
                                .also {
                                    it.setSurfaceProvider(previewView.surfaceProvider)
                                }
                            coroutineScope.launch {
                                val cameraProvider = context.getCameraProvider()
                                try {
                                    cameraProvider.unbindAll()
                                    cameraProvider.bindToLifecycle(
                                        lifecycleOwner,
                                        CameraSelector.DEFAULT_BACK_CAMERA,
                                        previewUseCase,
                                        imageCapture,
                                    )
                                } catch (ex: Exception) {
                                    Log.e("CameraPreview", "Use case binding failed", ex)
                                }
                            }
                            previewView
                        }

                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Button(
                            onClick = { navController.popBackStack() },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF304040)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                text = "Back",
                                color = Color(0xFFC9D1C8),
                                fontWeight = FontWeight.W500,
                                fontSize = 18.sp
                            )
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                presenter.addNewTree(deviceId, coordinates, file)
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF304040)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                text = "Send",
                                color = Color(0xFFC9D1C8),
                                fontWeight = FontWeight.W500,
                                fontSize = 18.sp
                            )
                        }
                        Button(
                            onClick = {
                                imageCapture.takePicture(
                                    options,
                                    ContextCompat.getMainExecutor(context),
                                    object : ImageCapture.OnImageSavedCallback {
                                        override fun onError(exc: ImageCaptureException) {
                                            Log.e(
                                                "err",
                                                "Photo capture failed: ${exc.message}",
                                                exc
                                            )
                                        }

                                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                            output.savedUri?.let { uri ->
                                                convertUriToFile(
                                                    uri,
                                                    context
                                                )?.readBytes()?.let { file }
                                            }
                                        }
                                    }
                                )
                            },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF304040)
                            ),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                text = "Take a picture",
                                color = Color(0xFFC9D1C8),
                                fontWeight = FontWeight.W500,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }

        CameraUiState.GoToRegistryScreen -> {
            navController.popBackStack()
        }

        CameraUiState.Loading -> {
        }
    }
}
