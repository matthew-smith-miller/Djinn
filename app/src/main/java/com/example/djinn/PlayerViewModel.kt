package com.example.djinn

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class PlayerViewModel(private val repository: PlayerRepository) : ViewModel() {

    val allPlayers: LiveData<List<Player>> = repository.allPlayers.asLiveData()

    fun getPlayerByName(name: String): LiveData<Player> {
        return repository.getPlayerByName(name).asLiveData()
    }

    fun getPlayerById(id: Int): LiveData<Player> {
        return repository.getPlayerById(id).asLiveData()
    }

    fun getPlayerIdsAndNames(ids: List<Int>): LiveData<List<DataClasses.IdNameTuple>> {
        return repository.getPlayerIdsAndNames(ids).asLiveData()
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(player: Player) = viewModelScope.launch {
        repository.insert(player)
    }
}

class PlayerViewModelFactory(private val repository: PlayerRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}