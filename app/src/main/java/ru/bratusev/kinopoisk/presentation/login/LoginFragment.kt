package ru.bratusev.kinopoisk.presentation.login

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
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
        return inflater.inflate(R.layout.fragment_login, container, false).also {
            configureViews(it.rootView)
        }
    }

    private fun setObservers() {
        vm.loginState.observe(viewLifecycleOwner) {
            if (it == true) {
                try {
                    findNavController().navigate(R.id.action_loginFragment_to_searchFragment)
                }catch (_: RuntimeException){ }
            }
            else showPasswordErrorAlert()
        }
    }

    private fun configureViews(rootView: View) {
        val inputLogin = rootView.findViewById<TextInputEditText>(R.id.input_login)
        inputPassword = rootView.findViewById(R.id.input_password)
        val connectionFlag = NetworkUtils.isInternetAvailable(requireContext())
        rootView.findViewById<AppCompatButton>(R.id.button_login).setOnClickListener {
            if (connectionFlag) vm.login(inputLogin.text.toString(), inputPassword.text.toString())
            setObservers()
        }
    }

    private fun showPasswordErrorAlert() {
        vm.showDialog(AlertDialog.Builder(requireContext()))
    }
}