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

    fun getPlayerById(id: Int): LiveData<Player> {
        return playerRepository.getPlayerById(id).asLiveData()
    }

    fun insertPartialGames(partialGames: List<PartialGame>) = viewModelScope.launch {
        insertPartialGamesAndRollupScore(partialGames)
    }

    private suspend fun insertPartialGamesAndRollupScore(partialGames: List<PartialGame>) {
        insertPartialGamesSuspend(partialGames)
        val gameIds = mutableListOf<Int>()
        for (partialGame in partialGames) {
            gameIds.add(partialGame.game)
        }
        Log.d("gameIds: ", gameIds.toString())
        val rivalryIds = gameRepository.rollupScore(gameIds)
        rollupRivalryScores(rivalryIds)
    }

    private suspend fun insertPartialGamesSuspend(partialGames: List<PartialGame>) {
        partialGameRepository.insert(partialGames)
    }

    private suspend fun rollupRivalryScores(rivalryIds: List<Int>) {
        rivalryRepository.rollupScore(rivalryIds)
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