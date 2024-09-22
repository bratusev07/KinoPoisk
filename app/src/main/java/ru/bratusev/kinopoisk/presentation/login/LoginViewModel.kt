package ru.bratusev.kinopoisk.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.bratusev.domain.Resource
import ru.bratusev.domain.usecase.LoginUseCase

class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {

    private val mutableLoginState = MutableLiveData(false)
    internal val loginState: LiveData<Boolean> = mutableLoginState

    internal fun login() {
        loginUseCase.invoke().onEach { result ->
            when (result) {
                is Resource.Success -> {
                    Log.d("LoginFragment", "Resource.Success")
                    mutableLoginState.value = result.data ?: false
                }

                is Resource.Error -> {
                    Log.d("LoginFragment", "Resource.Error ${result.message.toString()}")
                }

                is Resource.Loading -> {
                    Log.d("LoginFragment", "Resource.Loading")
                }
            }
        }.launchIn(viewModelScope)
    }

}