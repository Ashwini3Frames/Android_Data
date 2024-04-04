package com.example.androidkotlin
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ViewUserFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_view_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userId = arguments?.getInt("userId") ?: 0

        // Retrieve user data based on userId
        val user = getUserFromSharedPreferences(userId)

        // Bind user data to views
        user?.let {
            view.findViewById<TextView>(R.id.fullNameTextView).text = it.fullName
            view.findViewById<TextView>(R.id.emailTextView).text = it.email
            view.findViewById<TextView>(R.id.phoneNumberTextView).text = it.phoneNumber
            view.findViewById<TextView>(R.id.genderTextView).text = it.gender
            view.findViewById<TextView>(R.id.roleTextView).text = it.role
        }
    }

    private fun getUserFromSharedPreferences(userId: Int): User? {
        val sharedPreferences = requireActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val userListJson = sharedPreferences.getString("userList", "[]")
        val userListType = object : TypeToken<List<User>>() {}.type
        val userList: List<User> = Gson().fromJson(userListJson, userListType)
        return userList.find { it.id == userId }
    }
}
