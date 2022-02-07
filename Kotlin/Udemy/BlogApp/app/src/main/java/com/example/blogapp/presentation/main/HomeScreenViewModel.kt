package com.example.blogapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.blogapp.core.Result
import com.example.blogapp.domain.home.HomeScreenRepo
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class HomeScreenViewModel(private val repo: HomeScreenRepo) : ViewModel() {

    fun fetchLatestPosts() = liveData(viewModelScope.coroutineContext + Dispatchers.Main) {
        emit(Result.Loading())

        try {
            emit(repo.getLatestPosts())
        }catch (e:Exception){
            emit(Result.Failure(e))
        }
    }

    fun registerLikeButtonState(postId : String, liked : Boolean) = liveData(viewModelScope.coroutineContext + Dispatchers.Main) {
        emit(Result.Loading())

        try {
            emit(Result.Success(repo.registerLikeButtonState(postId,liked)))
        }catch (e:Exception){
            Result.Failure(e)
        }
    }
}
class HomeScreenViewModelFactory(private val repo : HomeScreenRepo) :ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(HomeScreenRepo::class.java).newInstance(repo)
    }

}