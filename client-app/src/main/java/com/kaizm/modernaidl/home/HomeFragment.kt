package com.kaizm.modernaidl.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.kaizm.modernaidl.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    companion object {
        fun newInstance() = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDeposit.setOnClickListener {
            viewModel.deposit(binding.edtAmount.text.toString().toDouble())
        }

        binding.btnWithdraw.setOnClickListener {
            viewModel.withdraw(binding.edtAmount.text.toString().toDouble())
        }

        binding.edtAmount.setOnClickListener {
            binding.transactionError.isVisible = false
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userState.collect { state ->
                binding.transactionError.apply {
                    isVisible = state.transactionError != null
                    text = state.transactionError
                }
                binding.tvCurrentBalance.text = state.balance
            }
        }
    }
}