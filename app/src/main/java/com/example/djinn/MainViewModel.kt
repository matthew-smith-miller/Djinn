package com.example.djinn

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MainViewModel(
    private val playerRepository: PlayerRepository,
    private val rivalryRepository: RivalryRepository
) : ViewModel() {

    val allPlayers: LiveData<List<Player>> = playerRepository.allPlayers.asLiveData()
    val allRivalries: LiveData<List<Rivalry>> = rivalryRepository.allRivalries.asLiveData()

    fun getPlayerByName(name: String): LiveData<Player> {
        return playerRepository.getPlayerByName(name).asLiveData()
    }

    fun getPlayerIdsAndNames(ids: List<Int>): LiveData<List<DataClasses.IdNameTuple>> {
        return playerRepository.getPlayerIdsAndNames(ids).asLiveData()
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(player: Player) = viewModelScope.launch {
        playerRepository.insert(player)
    }
}

class MainViewModelFactory(
    private val playerRepository: PlayerRepository,
    private val rivalryRepository: RivalryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(playerRepository, rivalryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}