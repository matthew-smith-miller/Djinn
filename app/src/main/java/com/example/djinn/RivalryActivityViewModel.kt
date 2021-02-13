package com.example.djinn

import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class RivalryActivityViewModel(
    private val playerRepository: PlayerRepository,
    private val rivalryRepository: RivalryRepository,
    private val gameRepository: GameRepository
) : ViewModel() {
    val insertedGameId = MutableLiveData<Long>()
    private val _visitorPlayer = MutableLiveData<Player>()
    private val _homePlayer = MutableLiveData<Player>()
    val visitorPlayer: LiveData<Player?>
        get() = _visitorPlayer
    val homePlayer: LiveData<Player?>
        get() = _homePlayer

    suspend fun getPlayerById(id: Int): LiveData<Player> {
        return playerRepository.getPlayerById(id).asLiveData()
    }

    fun getRivalryWithGamesById(id: Int): LiveData<DataClasses.RivalryWithGames> {
        return rivalryRepository.getRivalryWithGamesById(id).asLiveData()
    }

    fun setPlayers(visitorPlayerId: Int, homePlayerId: Int) {
        viewModelScope.launch {
            _visitorPlayer.value = getPlayerById(visitorPlayerId).value
            _homePlayer.value = getPlayerById(homePlayerId).value
        }
    }

    fun insert(game: Game) {
        viewModelScope.launch {
            insertedGameId.value = gameRepository.insert(game)
        }
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(rivalry: Rivalry) = viewModelScope.launch {
        rivalryRepository.insert(rivalry)
    }
}

class RivalryActivityViewModelFactory(
    private val playerRepository: PlayerRepository,
    private val rivalryRepository: RivalryRepository,
    private val gameRepository: GameRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RivalryActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RivalryActivityViewModel(
                playerRepository,
                rivalryRepository,
                gameRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}