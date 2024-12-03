package com.example.fitness.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.fitness.database.*
import com.example.fitness.repository.FitnessRepository
import kotlinx.coroutines.launch

class FitnessViewModel(private val repository: FitnessRepository) : ViewModel() {

    fun syncUserData(email: String) {
        viewModelScope.launch {
            try {
                repository.syncUserData(email)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun syncAllData() {
        viewModelScope.launch {
            try {
                repository.syncAllData()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getUser(email: String): User? {
        return repository.getUserByEmail(email)
    }

    suspend fun updateUserProfile(user: User) {
        repository.updateUserProfile(user)
    }
}
