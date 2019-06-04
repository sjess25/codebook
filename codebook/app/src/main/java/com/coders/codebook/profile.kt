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

        var miChallenges: ArrayList<String> = ArrayList()

        miChallenges.add("Reto 1")
        miChallenges.add("Reto 2")
        miChallenges.add("Reto 3")
        miChallenges.add("Reto 4")

        val listChallenge = findViewById<ListView>(R.id.myChallengeList)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, miChallenges)

        listChallenge.adapter = adapter

        listChallenge.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, miChallenges.get(position), Toast.LENGTH_LONG).show()
        }
    }
}
