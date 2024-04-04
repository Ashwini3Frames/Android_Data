package com.example.androidkotlin

import androidx.recyclerview.widget.DiffUtil

class UserDiffCallback : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        // Check if the item identifiers are the same
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        // Check if the item contents are the same
        return oldItem == newItem
    }
}