package ru.bratusev.kinopoisk.presentation.login

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bratusev.kinopoisk.R
import ru.bratusev.kinopoisk.databinding.FragmentLoginBinding

class LoginFragment : Fragment(R.layout.fragment_login) {
    private val vm: LoginViewModel by viewModel<LoginViewModel>()
    private val viewBinding: FragmentLoginBinding by viewBinding()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        configureViews()
        setObservers()
    }

    private fun setObservers() {
        vm.uiLabels.observe(viewLifecycleOwner) {
            when (it) {
                is LoginLabel.GoToNext -> navigateToSearchFragment()
                is LoginLabel.ShowPasswordAlert -> showAlert(it.message)
            }
        }
    }

    private fun configureViews() {
        viewBinding.buttonLogin.setOnClickListener {
            vm.handleEvent(LoginEvent.OnClickLogin(viewBinding.inputLogin.text.toString(), viewBinding.inputPassword.text.toString()))
        }
    }

    private fun navigateToSearchFragment() {
        try {
            findNavController().navigate(R.id.action_loginFragment_to_searchFragment)
        } catch (e: RuntimeException) {
            Log.e("DetailFragment", e.message.toString())
        }
    }

    private fun showAlert(message: String) {
        vm.showDialog(AlertDialog.Builder(requireContext()), message)
    }
}
