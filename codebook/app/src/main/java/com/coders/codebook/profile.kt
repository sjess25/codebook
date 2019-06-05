package com.coders.codebook

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

class profile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val bcreatePost = findViewById<Button>(R.id.createPost_button)

        var miChallenges: ArrayList<technology> = ArrayList()

        miChallenges.add(technology("Hola Mundo", "Hola mundo en C", R.drawable.c))
        miChallenges.add(technology("Hola Mundo", "Hola mundo en Java", R.drawable.java))
        miChallenges.add(technology("Hola Mundo", "Hola mundo en Ruby", R.drawable.ruby))
        miChallenges.add(technology("Hola Mundo", "Hola mundo en Prolog", R.drawable.prolog))

        val listChallenge = findViewById<ListView>(R.id.myChallengeList)
        //val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, miChallenges)
        val adapter = custumAdapter(this, miChallenges)

        listChallenge.adapter = adapter

        listChallenge.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, miChallenges.get(position).title, Toast.LENGTH_LONG).show()
        }

        bcreatePost.setOnClickListener(View.OnClickListener{
            val intentCreatePost = Intent(this, CreatePost::class.java)
            startActivity(intentCreatePost)
        })
    }
}
