package ru.bratusev.kinopoisk.presentation.login

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bratusev.kinopoisk.R
import ru.bratusev.kinopoisk.common.NetworkUtils
import ru.bratusev.kinopoisk.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val vm: LoginViewModel by viewModel<LoginViewModel>()
    private val viewBinding: FragmentLoginBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureViews()
    }

    private fun configureViews() {
        viewBinding.buttonLogin.setOnClickListener {
            handleLogin(viewBinding.inputLogin.text.toString(), viewBinding.inputPassword.text.toString())
        }
    }

    private fun handleLogin(login: String, password: String) {
        if (NetworkUtils.isInternetAvailable(requireContext())) {
            if (login.isEmpty() || password.isEmpty()) vm.showDialog(
                AlertDialog.Builder(
                    requireContext()
                ), "Логин или пароль не были введены."
            )
            else {
                vm.login(login, password)
                observeLoginState()
            }
        }
    }

    private fun observeLoginState() {
        viewLifecycleOwner.lifecycleScope.launch {
            vm.uiState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED).collect{
                if (it.loginState) {
                    navigateToSearchFragment()
                } else {
                    showPasswordErrorAlert()
                }
            }
        }
    }

    private fun navigateToSearchFragment() {
        try {
            findNavController().navigate(R.id.action_loginFragment_to_searchFragment)
        } catch (e: RuntimeException) {
            Log.e("DetailFragment", e.message.toString())
        }
    }

    private fun showPasswordErrorAlert() {
        vm.showDialog(
            AlertDialog.Builder(requireContext()),
            "Неверный пароль. Пожалуйста, попробуйте снова."
        )
    }
}