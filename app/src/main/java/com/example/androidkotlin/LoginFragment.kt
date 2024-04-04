package com.example.androidkotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.navigation.Navigation
import android.widget.Toast
import android.util.Patterns
// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    private lateinit var editTextEmailOrPhone: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var buttonSignup: Button
    private var errormsg: String? = null
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view=inflater.inflate(R.layout.fragment_login, container, false)
        editTextEmailOrPhone = view.findViewById(R.id.editTextEmailPhone)
        editTextPassword = view.findViewById(R.id.editTextPassword)
        buttonLogin = view.findViewById(R.id.buttonLogin)
        buttonSignup = view.findViewById(R.id.buttonSignUp)

        buttonLogin.setOnClickListener {
            login()
        }

        buttonSignup.setOnClickListener {
             Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signupFragment)
        }

        return view

    }
    private fun isValidEmailOrPhone(emailOrPhone: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(emailOrPhone).matches() ||
                Patterns.PHONE.matcher(emailOrPhone).matches()
    }
    private fun isValidPassword(password: String): Boolean {
        if (password.length < 8) {
            errormsg = "Minimum 8 characters required for creating password"
            return false
        }
        if (!password.matches(".*[A-Z].*".toRegex())) {
            errormsg = "Must Contain at least 1 Upper-Case Character for creating password"
            return false
        }
        if (!password.matches(".*[a-z].*".toRegex())) {
            errormsg = "Must Contain at least 1 Lower-Case Character for creating password"
            return false
        }
        return true
    }
    private fun login() {
        val emailOrPhone = editTextEmailOrPhone.text.toString()
        val password = editTextPassword.text.toString()

        if (isValidEmailOrPhone(emailOrPhone) && isValidPassword(password)) {
            Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment)
            // Clear the back stack
           // requireActivity().onBackPressedDispatcher.onBackPressed()

          //  Toast.makeText(activity, "Login successfully", Toast.LENGTH_SHORT).show()
            return
        }

        if (emailOrPhone.isEmpty() || password.isEmpty()) {
            Toast.makeText(activity, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidEmailOrPhone(emailOrPhone)) {
            Toast.makeText(activity, "Invalid email or phone format", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidPassword(password)) {
            Toast.makeText(activity, errormsg, Toast.LENGTH_SHORT).show()
            return
        }
    }


companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}