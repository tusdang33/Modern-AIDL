package com.kaizm.modernaidl.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kaizm.modernaidl.databinding.FragmentSignupBinding
import com.kaizm.modernaidl.login.LoginFragment
import com.kaizm.modernaidl.pushScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding

    companion object {
        fun newInstance() = SignupFragment()
    }

    private val viewModel: SignupViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignup.setOnClickListener {
            viewModel.signUp(
                binding.edtUsername.text.toString(),
                binding.edtPassword.text.toString()
            )
        }
        binding.btnLogin.setOnClickListener {
            pushScreen(requireActivity(), LoginFragment.newInstance(), isAddToBackStack = false)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.signupResult.collect {
                binding.signupError.isVisible = !it
                if (it) {
                    pushScreen(
                        requireActivity(),
                        LoginFragment.newInstance(),
                        isAddToBackStack = false
                    )
                }
            }
        }
    }
}