package com.coders.codebook

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast

class profile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

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
    }
}
