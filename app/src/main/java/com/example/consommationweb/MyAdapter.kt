package com.example.consommationweb

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val userList: ArrayList<User>, private val onUserSelectedListener: OnUserSelectedListener) :
RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    private var selectedUser: User? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.users_list,
            parent, false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.textViewName.text = currentUser.name
        holder.textViewUsername.text = currentUser.username
        holder.textViewEmail.text = currentUser.email

        // Gérer la sélection de l'utilisateur ici
        holder.itemView.setOnClickListener {
            selectedUser = userList[position]
           // onUserSelectedListener.onUserSelected(selectedUser != null)
            notifyDataSetChanged() // Mettre à jour l'interface utilisateur si nécessaire
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewUsername: TextView = itemView.findViewById(R.id.textViewUsername)
        val textViewEmail: TextView = itemView.findViewById(R.id.textViewEmail)
    }

    fun getSelectedUser(): User? {
        return selectedUser
    }

    interface OnUserSelectedListener {
        fun onUserSelected(userSelected: Boolean)
    }
}
