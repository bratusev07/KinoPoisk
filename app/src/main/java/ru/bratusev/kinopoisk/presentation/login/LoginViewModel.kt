package ru.bratusev.kinopoisk.presentation.login

import android.app.AlertDialog
import android.content.DialogInterface
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.bratusev.domain.Resource
import ru.bratusev.domain.model.UserData
import ru.bratusev.domain.usecase.LoginUseCase
import ru.bratusev.kinopoisk.common.SingleLiveEvent

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {
    private val _uiLabels = SingleLiveEvent<LoginLabel>()
    val uiLabels: LiveData<LoginLabel> get() = _uiLabels

    private fun login(
        login: String,
        password: String,
    ) {
        loginUseCase
            .invoke(UserData(login, password))
            .onEach { result ->
                handleLoginResult(result)
            }.launchIn(viewModelScope)
    }

    private fun handleLoginResult(result: Resource<Boolean>) {
        when (result) {
            is Resource.Success ->
                _uiLabels.value =
                    if (result.data == true) {
                        LoginLabel.GoToNext
                    } else {
                        LoginLabel.ShowPasswordAlert("Неверный пароль. Пожалуйста, попробуйте снова.")
                    }
            is Resource.Error -> logError(result.message)
            is Resource.Loading -> logLoading()
        }
    }

    fun showDialog(
        builder: AlertDialog.Builder,
        message: String,
    ) {
        builder
            .setTitle("Ошибка")
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

    internal fun handleEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnClickLogin -> handleLoginEvent(event.login, event.password)
        }
    }

    private fun handleLoginEvent(
        login: String,
        password: String,
    ) {
        if (login.isEmpty() || password.isEmpty()) {
            _uiLabels.value = LoginLabel.ShowPasswordAlert("Логин или пароль не были введены.")
        } else {
            login(login, password)
        }
    }
}
