package com.example.blogapp.presentation.auth

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.blogapp.core.Result
import com.example.blogapp.domain.auth.AuthRepo
import kotlinx.coroutines.Dispatchers
import java.lang.Exception

class AuthViewModel(private val repo: AuthRepo) : ViewModel() {

    fun singIn(email: String, password: String) = liveData(viewModelScope.coroutineContext + Dispatchers.Main) {
        emit(Result.Loading())
        try {
            emit(Result.Success(repo.singIn(email, password)))

        } catch (e: Exception) {
            emit(Result.Failure(e))
        }
    }

    fun signUp(username: String, email: String, password: String) =
        liveData(Dispatchers.IO) {
            emit(Result.Loading())
            try {
                emit(Result.Success(repo.singUp(username, email, password)))

            } catch (e: Exception) {
                emit(Result.Failure(e))
            }
        }


    fun updateUserProfile(imageBitmap : Bitmap,username: String) = liveData(viewModelScope.coroutineContext + Dispatchers.Main) {
        emit(Result.Loading())
        try {
            emit(Result.Success(repo.updateUserProfile(imageBitmap,username)))
        }catch (e:Exception) {
            emit(Result.Failure(e))
        }
    }
}


class AuthViewModelFactory(private val repo: AuthRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(repo) as T
    }
}