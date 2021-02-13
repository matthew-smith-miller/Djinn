package com.example.djinn

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import kotlin.properties.Delegates

class GameViewModel(private val gameRepository: GameRepository) : ViewModel() {
    val insertedGameId = MutableLiveData<Long>()
    fun getAllGames(): LiveData<List<Game>> {
        return gameRepository.getAllGames().asLiveData()
    }

    fun getGameWithPartialGamesById(gameId: Int): LiveData<DataClasses.GameWithPartialGames> {
        return gameRepository.getGameWithPartialGamesById(gameId).asLiveData()
    }

    fun insert(game: Game) {
        viewModelScope.launch {
            insertedGameId.value = gameRepository.insert(game)
        }
    }
}

class GameViewModelFactory(private val gameRepository: GameRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(gameRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}