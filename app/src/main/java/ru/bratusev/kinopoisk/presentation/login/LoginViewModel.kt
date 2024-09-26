package ru.bratusev.kinopoisk.presentation.login

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.bratusev.domain.Resource
import ru.bratusev.domain.model.UserData
import ru.bratusev.domain.usecase.LoginUseCase
class LoginViewModel(private val loginUseCase: LoginUseCase) : ViewModel() {

    private val _loginState = MutableLiveData<Boolean>()
    val loginState: LiveData<Boolean> = _loginState

    internal fun login(login: String, password: String) {
        loginUseCase.invoke(UserData(login, password)).onEach { result ->
            handleLoginResult(result)
        }.launchIn(viewModelScope)
    }

    private fun handleLoginResult(result: Resource<Boolean>) {
        when (result) {
            is Resource.Success -> _loginState.value = result.data ?: false
            is Resource.Error -> logError(result.message)
            is Resource.Loading -> logLoading()
        }
    }

    fun showDialog(builder: AlertDialog.Builder, message: String) {
        builder.setTitle("Ошибка")
            .setMessage(message)
            .setPositiveButton("OK") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
            .create()
            .show()
    }

    private fun logError(message: String?) {
        Log.d("LoginViewModel", "Resource.Error ${message ?: "Unknown error"}")
    }

    private fun logLoading() {
        Log.d("LoginViewModel", "Resource.Loading")
    }
}