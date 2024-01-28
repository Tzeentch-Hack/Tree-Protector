package com.tzeench.treeprotectormobile.presenters

import com.tzeench.treeprotectormobile.repository.SatelliteRepository
import com.tzeench.treeprotectormobile.utils.RegistryUiState
import com.tzeench.treeprotectormobile.utils.SatelliteUiState
import com.tzeench.treeprotectormobile.utils.isLoading
import com.tzeench.treeprotectormobile.utils.onFailure
import com.tzeench.treeprotectormobile.utils.onSuccess
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

class SatellitePresenter constructor(
    private val satelliteRepository: SatelliteRepository
) : KoinComponent {

    private val viewModelScope = CoroutineScope(Dispatchers.Default)
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception -> }

    private val _satelliteState = MutableStateFlow<SatelliteUiState>(SatelliteUiState.Loading)
    val satelliteState = _satelliteState.asStateFlow()


    fun makeTreeSatellitePhoto(x: Float, y: Float, deviceId: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            satelliteRepository.makeTreeSatellitePhoto(
                x, y, 18, deviceId
            ).collect { resultState ->
                resultState.isLoading {
                    _satelliteState.value = SatelliteUiState.Loading
                }.onSuccess { data ->
                    _satelliteState.value = SatelliteUiState.Result(result = data)
                }.onFailure { error ->
                    _satelliteState.value = SatelliteUiState.Error(error.message ?: "Unknown")
                }
            }
        }
    }
}