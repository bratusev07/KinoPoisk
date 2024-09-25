package ru.bratusev.kinopoisk.presentation.login

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bratusev.kinopoisk.R
import ru.bratusev.kinopoisk.common.NetworkUtils

class LoginFragment : Fragment() {

    private val vm: LoginViewModel by viewModel<LoginViewModel>()

    private lateinit var inputPassword: TextInputEditText

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false).apply {
            configureViews(this)
        }
    }

    private fun configureViews(rootView: View) {
        val inputLogin = rootView.findViewById<TextInputEditText>(R.id.input_login)
        inputPassword = rootView.findViewById(R.id.input_password)

        rootView.findViewById<AppCompatButton>(R.id.button_login).setOnClickListener {
            handleLogin(inputLogin.text.toString(), inputPassword.text.toString())
        }
    }

    private fun handleLogin(login: String, password: String) {
        if (NetworkUtils.isInternetAvailable(requireContext())) {
            vm.login(login, password)
            observeLoginState()
        }
    }

    private fun observeLoginState() {
        vm.loginState.observe(viewLifecycleOwner) { isLoggedIn ->
            if (isLoggedIn == true) {
                navigateToSearchFragment()
            } else {
                showPasswordErrorAlert()
            }
        }
    }

    private fun navigateToSearchFragment() {
        try {
            findNavController().navigate(R.id.action_loginFragment_to_searchFragment)
        } catch (e: RuntimeException) { }
    }

    private fun showPasswordErrorAlert() {
        vm.showDialog(AlertDialog.Builder(requireContext()))
    }
}