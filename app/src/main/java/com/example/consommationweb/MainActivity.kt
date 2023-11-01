package com.example.consommationweb

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException


class MainActivity : AppCompatActivity(),MyAdapter.OnUserSelectedListener {
private lateinit var textView: TextView
    private lateinit var btn: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: MyAdapter
    private lateinit var userList: ArrayList<User>
    private var requestQueue: RequestQueue? = null

    private lateinit var buttonSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.TextArea)
        btn = findViewById(R.id.parseBtn)
        recyclerView = findViewById(R.id.rv)
        buttonSave = findViewById(R.id.buttonSave)



        userList = ArrayList()
        myAdapter = MyAdapter(userList,this)
       recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = myAdapter


        requestQueue = Volley.newRequestQueue(this)

        btn.setOnClickListener {
            jsonParse()
        }
        buttonSave.setOnClickListener {
            val selectedUser = myAdapter.getSelectedUser()
            if (selectedUser != null) {
                onSaveClick(selectedUser)
            } else {
                Toast.makeText(this, "No user selected", Toast.LENGTH_SHORT).show()
            }
        }
        // Dans votre fonction onCreate
        val buttonShowDetails: Button = findViewById(R.id.buttonShowDetails)

        buttonShowDetails.setOnClickListener {
            val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val userName = sharedPreferences.getString("name", "")
            val userUsername = sharedPreferences.getString("username", "")
            val userEmail = sharedPreferences.getString("email", "")

            if (userName != null && userUsername != null && userEmail != null) {
                val userDetails = "Name: $userName\nUsername: $userUsername\nEmail: $userEmail"
                // Affichez les détails de l'utilisateur dans un TextView ou tout autre élément de l'interface utilisateur.
                textView.text = userDetails
            } else {
                Toast.makeText(this, "User details not found", Toast.LENGTH_SHORT).show()
            }
        }



    }
    override fun onUserSelected(userSelected: Boolean) {
        buttonSave.isEnabled = userSelected
    }

    private fun onSaveClick(user: User) {
        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("name", user.name)
        editor.putString("username", user.username)
        editor.putString("email", user.email)
        editor.apply()
        Toast.makeText(this, "User saved ", Toast.LENGTH_SHORT).show()
    }
    private fun jsonParse() {
        val url = "https://jsonplaceholder.typicode.com/users"
        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    for (i in 0 until response.length()) {
                        val user = response.getJSONObject(i)
                        val firstName = user.getString("name")
                        val username = user.getString("username")
                        val mail = user.getString("email")
                        val userObject = User(firstName, username, mail)
                        userList.add(userObject)
                       // textView.append("$firstName,$username,$mail")
                    }
                    myAdapter.notifyDataSetChanged()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error -> error.printStackTrace() }
        )
        requestQueue?.add(request)
    }


}