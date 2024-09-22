package ru.bratusev.kinopoisk.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.bratusev.kinopoisk.R
import ru.bratusev.kinopoisk.common.NetworkUtils

class LoginFragment : Fragment() {

    private val vm: LoginViewModel by viewModel<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false).also {
            configureViews(it.rootView)
            setObservers()
        }
    }

    private fun setObservers() {
        vm.loginState.observe(viewLifecycleOwner) {
            if (it == true) findNavController().navigate(R.id.action_loginFragment_to_searchFragment)
        }
    }

    private fun configureViews(rootView: View) {
        val connectionFlag = NetworkUtils.isInternetAvailable(requireContext())
        rootView.findViewById<AppCompatButton>(R.id.button_login).setOnClickListener {
            if (connectionFlag) vm.login()
        }
    }
}