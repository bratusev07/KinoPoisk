package ru.bratusev.kinopoisk.presentation.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.bratusev.domain.Resource
import ru.bratusev.domain.model.UserData
import ru.bratusev.domain.usecase.LoginUseCase
import ru.bratusev.kinopoisk.common.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
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
