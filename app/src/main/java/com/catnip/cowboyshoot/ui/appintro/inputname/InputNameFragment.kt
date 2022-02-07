package com.catnip.cowboyshoot.ui.appintro.inputname

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.catnip.cowboyshoot.R
import com.catnip.cowboyshoot.databinding.FragmentInputNameBinding
import com.catnip.cowboyshoot.preference.UserPreference
import com.google.android.material.snackbar.Snackbar

class InputNameFragment : Fragment() {

    private lateinit var binding: FragmentInputNameBinding

    private val userPreference: UserPreference? by lazy {
        context?.let { UserPreference(it) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInputNameBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPrefilledData()
    }

    private fun setPrefilledData() {
        userPreference?.let {
            binding.etPlayerName.setText(it.name.orEmpty())
        }
    }

    public fun navigateToMenuGame() {
        if (isNameFilled()) {
            userPreference?.name = binding.etPlayerName.text.toString().trim()
            Toast.makeText(context, userPreference?.name, Toast.LENGTH_SHORT).show()
        }
    }

    private fun isNameFilled(): Boolean {
        val name = binding.etPlayerName.text.toString().trim()
        var isFormValid = true
        if (name.isEmpty()) {
            isFormValid = false
            Snackbar.make(
                binding.root,
                getString(R.string.text_snackbar_name_should_be_filled),
                Snackbar.LENGTH_SHORT
            ).show()
        }
        return isFormValid
    }

}