package com.tzeench.treeprotectormobile.utils

import com.tzeench.treeprotectormobile.dto.GetAllTreesModelDto
import com.tzeench.treeprotectormobile.dto.SatelliteDto

sealed class RegistryUiState {

    object Loading : RegistryUiState()

    data class Result(val result: GetAllTreesModelDto): RegistryUiState()

    data class Error(val error: String) : RegistryUiState()
}

sealed class CameraUiState {
    object Initial : CameraUiState()

    object Loading : CameraUiState()

    object GoToRegistryScreen : CameraUiState()
}

sealed class SatelliteUiState {

    object Loading : SatelliteUiState()

    data class Result(val result: SatelliteDto): SatelliteUiState()

    data class Error(val error: String) : SatelliteUiState()
}
