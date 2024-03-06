package com.example.djinn

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class PartialGameViewModel(private val partialGameRepository: PartialGameRepository) : ViewModel() {

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(partialGames: List<PartialGame>) = viewModelScope.launch {
        partialGameRepository.insert(partialGames)
    }
}

class PartialGameViewModelFactory(
    private val partialGameRepository: PartialGameRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PartialGameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PartialGameViewModel(partialGameRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}