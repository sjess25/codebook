package com.coders.codebook

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.android.volley.Response
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class profile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val bcreatePost = findViewById<Button>(R.id.createPost_button)
        val beditProfile = findViewById<ImageButton>(R.id.vw_profiles)


        val listChallenge = findViewById<ListView>(R.id.myChallengeList)
        val adapter = custumAdapter(this, listChallenges.getList())
        listChallenge.adapter = adapter

        Log.d("list challenge", "profile")

        bcreatePost.setOnClickListener(View.OnClickListener{
            (this as profile).finish()
            val intentCreatePost = Intent(this, CreatePost::class.java)
            startActivity(intentCreatePost)
        })

        beditProfile.setOnClickListener(View.OnClickListener {
            (this as profile).finish()
            val intentEditProfile = Intent(this, EditProfile::class.java)
            startActivity(intentEditProfile)
        })

        listChallenge.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            //Toast.makeText(this, listChallenges.getList()[position].title, Toast.LENGTH_LONG).show()
            if(Network.vNetwork(this)) {
                val params = LinkedHashMap<String,String>()
                params["ID"] = 4.toString()
                params["Challenge"] = listChallenges.getList()[position].id.toString()
                val jsonUser = JSONObject(params)

                Network.getJson(this, "http://35.231.202.82:81/data", jsonUser, Response.Listener<JSONObject>{
                        response_Json ->
                    try {
                        Log.d("select challenge", jsonUser.toString())
                        Log.d("response challenge", response_Json.toString())
                    }catch (e: Exception){
                        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Sin conexion", Toast.LENGTH_LONG).show()
            }
        }
    }
}
