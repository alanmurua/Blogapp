package com.example.blogapp.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.blogapp.core.Result
import com.example.blogapp.data.model.Post
import com.example.blogapp.domain.profile.ProfileRepo
import kotlinx.coroutines.Dispatchers

class ProfileViewModel(private val repo: ProfileRepo) : ViewModel() {

    fun getUserInfo() = liveData(viewModelScope.coroutineContext + Dispatchers.Main) {
        emit(Result.Loading())
        try {
            emit(Result.Success(repo.getUserInfo()))
        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    fun getUserPosts()  = liveData(viewModelScope.coroutineContext + Dispatchers.Main){
        emit(Result.Loading())
        try {
            emit(Result.Success(repo.getUserPosts()))
        }catch (e:Exception){
            emit(Result.Failure(e))
        }
    }

}

class ProfileViewModelFactory(private val repo: ProfileRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ProfileRepo::class.java).newInstance(repo)
    }
}
