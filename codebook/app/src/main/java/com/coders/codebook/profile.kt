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
        val adapter = custumAdapter(this, userChallengeList.getList(userChallengeList.getIndex("Asesor")))
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
                params["Challenge"] = userChallengeList.getList(userChallengeList.getIndex("Asesor"))[position].id.toString() //listChallenges.getList()[position].id.toString()
                val jsonUser = JSONObject(params)

                Network.getJson(this, "http://35.231.202.82:81/data", jsonUser, Response.Listener<JSONObject>{
                        response_Json ->
                    try {
                        Log.d("send challenge", jsonUser.toString())
                        Log.d("response challenge", response_Json.toString())

                        var likes: Int = 0
                        var dislikes: Int = 0

                        if (response_Json["Likes"].toString() != "null") {
                            likes = response_Json["Likes"].toString().toInt()
                        }
                        if (response_Json["Dislikes"].toString() != "null") {
                            dislikes = response_Json["Dislikes"].toString().toInt()
                        }

                        newChallenge.setInfoChallenge(response_Json["Title"].toString(), response_Json["Description"].toString(), response_Json["Difficulty"].toString().toInt(), response_Json["TimeLimit"].toString().toInt(), response_Json["Ref1"].toString(), response_Json["Ref2"].toString(), response_Json["Ref3"].toString(), response_Json["Owner"].toString().toInt(), likes, dislikes)
                        val intentInfo = Intent(this, ViewPost::class.java)
                        intentInfo.putExtra("state", "Ver Respuestas")
                        startActivity(intentInfo)
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
