package com.example.djinn

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class GameViewModel(private val repository: GameRepository) : ViewModel() {

    fun getGamesFromRivalryId(rivalryId: Int): LiveData<List<Game>> {
        return repository.getGamesFromRivalryId(rivalryId).asLiveData()
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(game: Game) = viewModelScope.launch {
        repository.insert(game)
    }
}

class GameViewModelFactory(private val repository: GameRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}