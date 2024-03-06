package com.example.djinn

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(
    private val playerRepository: PlayerRepository,
    private val rivalryRepository: RivalryRepository
) : ViewModel() {

    val allPlayers: LiveData<List<Player>> = playerRepository.allPlayers.asLiveData()
    val activeRivalries: LiveData<List<Rivalry>> = rivalryRepository.activeRivalries.asLiveData()

    fun getPlayerByName(name: String): LiveData<Player> {
        return playerRepository.getPlayerByName(name).asLiveData()
    }

    fun getPlayerIdsAndNames(ids: List<Int>): LiveData<List<DataClasses.IdNameTuple>> {
        return playerRepository.getPlayerIdsAndNames(ids).asLiveData()
    }

    fun insertPlayerAndRivalry(player: Player) = viewModelScope.launch {
        val newPlayerId = insertPlayer(player)
        rivalryRepository.insert(Rivalry.makeRivalry(newPlayerId, 33))
    }

    fun insert(player: Player) = viewModelScope.launch {
        playerRepository.insert(player)
    }

    private suspend fun insertPlayer(player: Player): Int {
        return playerRepository.insert(player).toInt()
    }

    private suspend fun getHomePlayerId(): Int? {
        return playerRepository.getPlayerByName("Matthew Miller").asLiveData().value?.id
    }
}

class MainActivityViewModelFactory(
    private val playerRepository: PlayerRepository,
    private val rivalryRepository: RivalryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(playerRepository, rivalryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}