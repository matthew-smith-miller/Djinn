package com.example.djinn

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class PartialGameViewModel(private val repository: PartialGameRepository) : ViewModel() {

    fun getPartialGamesFromGameId(gameId: Int?): LiveData<List<PartialGame>> {
        return repository.getPartialGamesFromGameId(gameId).asLiveData()
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(partialGame: PartialGame) = viewModelScope.launch {
        repository.insert(partialGame)
    }
}

class PartialGameViewModelFactory(private val repository: PartialGameRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PartialGameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PartialGameViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}