package com.example.djinn

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class GameActivityViewModel(
    private val playerRepository: PlayerRepository,
    private val rivalryRepository: RivalryRepository,
    private val gameRepository: GameRepository,
    private val partialGameRepository: PartialGameRepository
) : ViewModel() {

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insertPartialGames(partialGames: List<PartialGame>) = viewModelScope.launch {
        insertPartialGamesAndRollupScore(partialGames)
    }

    suspend fun insertPartialGamesAndRollupScore(partialGames: List<PartialGame>) {
        insertPartialGamesSuspend(partialGames)
        val gameIds = mutableListOf<Int>()
        for (partialGame in partialGames) {
            gameIds.add(partialGame.game)
        }
        Log.d("gameIds: ", gameIds.toString())
        gameRepository.rollupScore(gameIds)
    }

    suspend fun insertPartialGamesSuspend(partialGames: List<PartialGame>) {
        partialGameRepository.insert(partialGames)
    }

    fun getGameWithPartialGamesById(gameId: Int): LiveData<DataClasses.GameWithPartialGames> {
        return gameRepository.getGameWithPartialGamesById(gameId).asLiveData()
    }

    fun getPlayerIdsAndNames(ids: List<Int>): LiveData<List<DataClasses.IdNameTuple>> {
        return playerRepository.getPlayerIdsAndNames(ids).asLiveData()
    }
}

class GameActivityViewModelFactory(
    private val playerRepository: PlayerRepository,
    private val rivalryRepository: RivalryRepository,
    private val gameRepository: GameRepository,
    private val partialGameRepository: PartialGameRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GameActivityViewModel(
                playerRepository, rivalryRepository, gameRepository, partialGameRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}