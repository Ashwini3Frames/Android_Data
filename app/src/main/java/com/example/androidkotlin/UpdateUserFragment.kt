package com.example.androidkotlin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.content.Context

import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UpdateUserFragment : Fragment() {

    private lateinit var etFullName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var btnUpdate: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_update_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etFullName = view.findViewById(R.id.etFullName)
        etEmail = view.findViewById(R.id.etEmail)
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber)
        btnUpdate = view.findViewById(R.id.btnUpdate)

        val userId = arguments?.getInt("userId") ?: 0
        val userDetails = getUserDetailsFromSharedPreferences(userId)
        userDetails?.let {
            etFullName.setText(it.fullName)
            etEmail.setText(it.email)
            etPhoneNumber.setText(it.phoneNumber)
            // Bind other fields as needed
        }

        btnUpdate.setOnClickListener {
            updateUserDetails(userId)
        }
    }

    private fun getUserDetailsFromSharedPreferences(userId: Int): User? {
        val sharedPreferences = requireActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val userListJson = sharedPreferences.getString("userList", "[]")
        val userListType = object : TypeToken<List<User>>() {}.type
        val userList: List<User> = Gson().fromJson(userListJson, userListType)
        return userList.find { it.id == userId }
    }

    private fun updateUserDetails(userId: Int) {
        val fullName = etFullName.text.toString()
        val email = etEmail.text.toString()
        val phoneNumber = etPhoneNumber.text.toString()

        if (fullName.isNotBlank() && email.isNotBlank() && phoneNumber.isNotBlank()) {
            val sharedPreferences = requireActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
            val gson = Gson()
            val userListJson = sharedPreferences.getString("userList", "[]")
            val userList: MutableList<User> = gson.fromJson(userListJson, object : TypeToken<MutableList<User>>() {}.type)

            val userIndex = userList.indexOfFirst { it.id == userId }
            if (userIndex != -1) {
                userList[userIndex] = userList[userIndex].copy(fullName = fullName, email = email, phoneNumber = phoneNumber)
                val editor = sharedPreferences.edit()
                editor.putString("userList", gson.toJson(userList))
                editor.apply()
                Toast.makeText(requireContext(), "Updated Successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "User not found", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
        }
    }
}
