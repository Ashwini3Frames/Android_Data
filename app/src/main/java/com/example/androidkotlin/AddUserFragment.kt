package com.example.androidkotlin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import android.widget.Button
import android.widget.EditText
import android.util.Patterns
import android.widget.Spinner
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddUserFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddUserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var editTextFullName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var buttonAdd: Button
    private lateinit var spinnerRole:Spinner
    private lateinit var radioGroupGender: RadioGroup
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
        val view=inflater.inflate(R.layout.fragment_add_user, container, false)
        editTextFullName = view.findViewById(R.id.editTextFullName)
        editTextEmail = view.findViewById(R.id.editTextEmail)
        editTextPhone = view.findViewById(R.id.editTextPhone)
        buttonAdd = view.findViewById(R.id.buttonAdd)
        spinnerRole = view.findViewById(R.id.spinnerRole)
        radioGroupGender = view.findViewById(R.id.radioGroupGender)
        buttonAdd.setOnClickListener {
            if (validateInputs()) {
                // Perform signup operation
                saveUserData()
                clearInputs()
                Toast.makeText(requireContext(), "User Added successfully", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
    private fun saveUserData() {
        val fullName =editTextFullName.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val phoneNumber = editTextPhone.text.toString().trim()
        val role = spinnerRole.selectedItem.toString()
        val gender = when (radioGroupGender.checkedRadioButtonId) {
            R.id.radioButtonMale -> "Male"
            R.id.radioButtonFemale -> "Female"
            else -> ""
        }

        val sharedPreferences = requireActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val gson = Gson()
        val existingUserListJson = sharedPreferences.getString("userList", "[]")
        val existingUserList: MutableList<User> = gson.fromJson(existingUserListJson, object : TypeToken<MutableList<User>>() {}.type)

        // Generating a unique id for the new user
        val newUserId = (System.currentTimeMillis() / 1000).toInt() // Using current timestamp as id

        val newUser = User(newUserId, fullName, email, phoneNumber, gender, role) // Including id
        existingUserList.add(newUser)

        val editor = sharedPreferences.edit()
        editor.putString("userList", gson.toJson(existingUserList))
        editor.apply()
    }
    private fun clearInputs() {
        editTextFullName.text.clear()
        editTextEmail.text.clear()
        editTextPhone.text.clear()
        radioGroupGender.clearCheck()
        spinnerRole.setSelection(0)
    }
    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        // Define your custom regular expression for phone number validation
        val regex = Regex("^\\d{10}\$") // Example: Validates 10-digit phone number

        // Check if the phone number matches the regular expression
        return regex.matches(phoneNumber)
    }

    private fun validateInputs(): Boolean {
        val fullName = editTextFullName.text.toString().trim()
        val email = editTextEmail.text.toString().trim()
        val phoneNumber = editTextPhone.text.toString().trim()
        val role = spinnerRole.selectedItem.toString()
        if (fullName.isEmpty()) {
            editTextFullName.error = "Full name is required"
            return false
        }

        if (email.isEmpty()) {
            editTextEmail.error = "Email is required"
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = "Invalid email address"
            return false
        }

        if (!isPhoneNumberValid(phoneNumber)) {
            editTextPhone.error = "Invalid phone number"
            return false
        }

        if (phoneNumber.isEmpty()) {
            editTextPhone.error = "Phone number is required"
            return false
        }

        val genderSelected = view?.findViewById<RadioGroup>(R.id.radioGroupGender)
        val radioButtonSelected = genderSelected?.findViewById<RadioButton>(genderSelected.checkedRadioButtonId)
        if (radioButtonSelected == null) {
            Toast.makeText(requireContext(), "Please select gender", Toast.LENGTH_SHORT).show()
            return false
        }
        if (role == "Select Role") {
            Toast.makeText(requireContext(), "Please select a role", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddUserFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddUserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}