package com.tzeench.treeprotectormobile.presenters

import com.tzeench.treeprotectormobile.repository.RegistryRepository
import com.tzeench.treeprotectormobile.utils.RegistryUiState
import com.tzeench.treeprotectormobile.utils.isLoading
import com.tzeench.treeprotectormobile.utils.onFailure
import com.tzeench.treeprotectormobile.utils.onSuccess
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.time.Timer

class RegistryPresenter constructor(
    private val registryRepository: RegistryRepository
) : KoinComponent {

    private val viewModelScope = CoroutineScope(Dispatchers.Default)
    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception -> }

    private val _registryState = MutableStateFlow<RegistryUiState>(RegistryUiState.Loading)
    val registryState = _registryState.asStateFlow()


    fun getAllTrees() {
        viewModelScope.launch(coroutineExceptionHandler) {
            registryRepository.getAllTrees().collect { resultState ->
                resultState.isLoading {
                    _registryState.value = RegistryUiState.Loading
                }.onSuccess { data ->
                    _registryState.value = RegistryUiState.Result(result = data)
                }.onFailure { error ->
                    _registryState.value = RegistryUiState.Error(error.message ?: "Unknown")
                }
            }
        }
    }
}