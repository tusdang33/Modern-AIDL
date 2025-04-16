package com.kaizm.modernaidl.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kaizm.modernaidl.databinding.FragmentLoginBinding
import com.kaizm.modernaidl.home.HomeFragment
import com.kaizm.modernaidl.pushScreen
import com.kaizm.modernaidl.signup.SignupFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    companion object {
        fun newInstance() = LoginFragment()
    }

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLogin.setOnClickListener {
            viewModel.login(
                binding.edtUsername.text.toString(),
                binding.edtPassword.text.toString()
            )
        }

        binding.btnSignup.setOnClickListener {
            pushScreen(requireActivity(), SignupFragment.newInstance())
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginResult.collect {
                binding.loginError.isVisible = !it
                if (it) {
                    pushScreen(requireActivity(), HomeFragment.newInstance())
                }
            }
        }
    }
}