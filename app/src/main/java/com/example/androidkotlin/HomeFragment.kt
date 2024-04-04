package com.example.androidkotlin

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.Toolbar
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.reflect.TypeToken
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {
    private lateinit var userListAdapter: UserListAdapter
    private val TAG = "HomeFragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        val fabAddUser = rootView.findViewById<FloatingActionButton>(R.id.fab)
        fabAddUser.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addUserFragment)
        }
        val toolbar = rootView.findViewById<Toolbar>(R.id.toolbarmenu)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        setHasOptionsMenu(true) // Enable options menu
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadUserList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d("TAG", "start")
        val selectedUsers = userListAdapter.getSelectedUsers()

        Log.d(TAG, "Selected users: $selectedUsers")

        return when (item.itemId) {
            R.id.menu_view -> {
                if (selectedUsers.size == 1) {
                    navigateToViewScreen(selectedUsers.first())
                } else {
                    showSelectionAlert()
                }
                true
            }
            R.id.menu_update -> {
                if (selectedUsers.size == 1) {
                    navigateToUpdateScreen(selectedUsers.first())
                } else {
                    showSelectionAlert()
                }
                true
            }
            R.id.menu_delete -> {
                if (selectedUsers.isNotEmpty()) {
                    showDeleteConfirmationDialog(selectedUsers)
                }
                true
            }
            R.id.menu_logout ->{
                navigateToLoginScreen()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }




    private fun showSelectionAlert() {
        AlertDialog.Builder(requireContext())
            .setMessage("Please select exactly one user.")
            .setPositiveButton("OK", null)
            .show()
    }
    private fun navigateToLoginScreen(){
        findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
    }

    private fun navigateToViewScreen(userId: Int) {
        findNavController().navigate(R.id.viewUserFragment, bundleOf("userId" to userId))
    }

    private fun navigateToUpdateScreen(userId: Int) {
        findNavController().navigate(R.id.updateUserFragment, bundleOf("userId" to userId))
    }

    private fun setupRecyclerView() {
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.recyclerView)
        userListAdapter = UserListAdapter()
        recyclerView.apply {
            adapter = userListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun loadUserList() {
        val sharedPreferences = requireActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val userListJson = sharedPreferences.getString("userList", "[]")
        val userListType = object : TypeToken<List<User>>() {}.type
        val userList: List<User> = Gson().fromJson(userListJson, userListType)
        userListAdapter.submitList(userList)
    }

    private fun showDeleteConfirmationDialog(selectedUsers: Set<Int>) {
        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to delete selected users?")
            .setPositiveButton("Delete") { _, _ ->
                deleteSelectedItems(selectedUsers)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteSelectedItems(selectedUsers: Set<Int>) {
        val sharedPreferences = requireActivity().getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val gson = Gson()

        // Retrieve existing user list from SharedPreferences
        val userListJson = sharedPreferences.getString("userList", "[]")
        val userListType = object : TypeToken<List<User>>() {}.type
        val existingUserList: MutableList<User> = gson.fromJson(userListJson, userListType)

        // Remove the selected users from the list
        existingUserList.removeAll { user -> selectedUsers.contains(user.id) }

        // Update the user list in SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString("userList", gson.toJson(existingUserList))
        editor.apply()

        // Refresh the user list in the RecyclerView
        userListAdapter.submitList(existingUserList)
    }
}
