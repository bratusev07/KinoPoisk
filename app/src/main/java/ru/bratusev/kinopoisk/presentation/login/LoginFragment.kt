package ru.bratusev.kinopoisk.presentation.login

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.bratusev.kinopoisk.R
import ru.bratusev.kinopoisk.databinding.FragmentLoginBinding
import ru.bratusev.kinopoisk.presentation.search.SearchEvent

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {
    private val vm by viewModels<LoginViewModel>()

    private val viewBinding: FragmentLoginBinding by viewBinding()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        configureViews()
        setObservers()
        vm.handleEvent(LoginEvent.OnFragmentStart)
    }

    private fun setObservers() {
        vm.uiLabels.observe(viewLifecycleOwner) {
            when (it) {
                is LoginLabel.GoToNext -> navigateToSearchFragment()
                is LoginLabel.ShowPasswordAlert -> showAlert(it.message)
                LoginLabel.OnBackClick -> onBackPress()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            vm.uiState
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect {
                    viewBinding.textLastLoginTime.text = it.lastLoginTime
                }
        }
    }

    private fun onBackPress() {

    }

    private fun configureViews() {
        setupBackPressHandler()
        viewBinding.buttonLogin.setOnClickListener {
            vm.handleEvent(
                LoginEvent.OnClickLogin(
                    viewBinding.inputLogin.text.toString(),
                    viewBinding.inputPassword.text.toString(),
                ),
            )
        }
    }

    private fun navigateToSearchFragment() {
        try {
            findNavController().navigate(R.id.action_loginFragment_to_searchFragment)
        } catch (e: RuntimeException) {
            Log.e("DetailFragment", e.message.toString())
        }
    }

    private fun setupBackPressHandler() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    vm.handleEvent(LoginEvent.OnClickBack)
                }
            },
        )
    }

    private fun showAlert(message: String) {
        AlertDialog
            .Builder(requireContext())
            .setTitle("Ошибка")
            .setMessage(message)
            .setPositiveButton("OK") { dialog: DialogInterface, _: Int -> dialog.dismiss() }
            .create()
            .show()
    }
}
