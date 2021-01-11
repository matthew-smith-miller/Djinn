package com.example.djinn

import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class RivalryViewModel(private val repository: RivalryRepository) : ViewModel() {

    val allRivalries: LiveData<List<Rivalry>> = repository.allRivalries.asLiveData()

    fun getRivalryWithGamesById(id: Int): LiveData<DataClasses.RivalryWithGames> {
        return repository.getRivalryWithGamesById(id).asLiveData()
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(rivalry: Rivalry) = viewModelScope.launch {
        repository.insert(rivalry)
    }
}

class RivalryViewModelFactory(private val repository: RivalryRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RivalryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RivalryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}