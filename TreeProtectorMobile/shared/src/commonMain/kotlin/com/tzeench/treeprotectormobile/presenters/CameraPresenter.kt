package com.tzeench.treeprotectormobile.presenters

import com.tzeench.treeprotectormobile.repository.AddNewTreeRepository
import com.tzeench.treeprotectormobile.utils.CameraUiState
import com.tzeench.treeprotectormobile.utils.isLoading
import com.tzeench.treeprotectormobile.utils.onFailure
import com.tzeench.treeprotectormobile.utils.onSuccess
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class CameraPresenter constructor(
    private val addNewTreeRepository: AddNewTreeRepository
) : KoinComponent {

    private val _cameraState = MutableStateFlow<CameraUiState>(CameraUiState.Initial)
    val cameraState = _cameraState.asStateFlow()

    private val viewModelScope = CoroutineScope(Dispatchers.Default)
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        _cameraState.value = CameraUiState.Loading
    }

    fun addNewTree(deviceId: String, coordinates: String, file: ByteArray) {
        viewModelScope.launch(coroutineExceptionHandler) {
            addNewTreeRepository.addNewTree(
                deviceId = deviceId,
                coordinates = coordinates,
                file = file
            ).collect { state ->
                state.isLoading {
                    _cameraState.value = CameraUiState.Loading
                }.onSuccess {
                    _cameraState.value = CameraUiState.GoToRegistryScreen
                }.onFailure {
                    _cameraState.value = CameraUiState.GoToRegistryScreen
                }
            }
        }
    }
}