package com.santimattius.basic.skeleton

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.santimattius.basic.skeleton.core.storage.FileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class MainUiState(
    val isLoading: Boolean = false,
    val message: String = "",
    val uri: Uri = Uri.EMPTY,
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val fileRepository: FileRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(MainUiState())
    val state: StateFlow<MainUiState>
        get() = _state.asStateFlow()

    init {
        generateUri()
    }

    private fun generateUri() {
        val uri = fileRepository.createImageFile()
        _state.update { it.copy(uri = uri) }
    }
}