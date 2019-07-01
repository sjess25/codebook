package com.coders.codebook

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.View
import android.widget.*
import com.android.volley.Response
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class profile : AppCompatActivity() {

    private lateinit var mHandler: Handler
    private lateinit var mRunnable:Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val bcreatePost = findViewById<Button>(R.id.createPost_button)
        val beditProfile = findViewById<ImageButton>(R.id.profile_picture)

        val bBack = findViewById<Button>(R.id.back_button)
        bBack.setOnClickListener {
            finish()
        }


        val listChallenge = findViewById<ListView>(R.id.myChallengeList)
        var adapter = custumAdapter(this, userChallengeList.getList(userChallengeList.getIndex("Asesor")))
        listChallenge.adapter = adapter

        val swipeContainer = findViewById<SwipeRefreshLayout>(R.id.update_challenge)
        mHandler = Handler()

        swipeContainer.setOnRefreshListener {
            mRunnable = Runnable {

                if(Network.vNetwork(this)) {
                    val params = java.util.LinkedHashMap<String, String>()
                    params["ID"] = 1.toString()
                    params["Owner"] = dataUser.getID().toString()
                    val jsonUser = JSONObject(params)
                    val jsonList = JSONArray()
                    jsonList.put(jsonUser)
                    Network.getJsonArray(this, "http://35.231.202.82:81/data", jsonList, Response.Listener<JSONArray>{
                            response_Json ->
                        try {
                            userChallengeList.allChallenge[userChallengeList.getIndex("Asesor")]!!.clear()
                            for (i in 0.. (response_Json.length() - 1)){
                                listChallenges.insertChallenge(technology(response_Json.getJSONObject(i).getInt("ID"), response_Json.getJSONObject(i).getString("Title"), response_Json.getJSONObject(i).getString("Description"), dataUser.getDrawable(response_Json.getJSONObject(i).getInt("Technologie"))))
                                Log.d("list challenge", listChallenges.getList().get(i).title)
                            }

                            userChallengeList.insertListChallenge(userChallengeList.getIndex("Asesor"))
                            listChallenges.clear()

                            adapter = custumAdapter(this, userChallengeList.getList(userChallengeList.getIndex("Asesor")))
                            listChallenge.adapter = adapter
                        }catch (e: Exception){
                            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                            Log.d("all challenge", e.message)
                        }
                    })
                } else {
                    Toast.makeText(this, "Sin conexion", Toast.LENGTH_LONG).show()
                }

                swipeContainer.isRefreshing = false
            }
            mHandler.postDelayed(mRunnable,
                1500.toLong())
        }



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
                        intentInfo.putExtra("challenge", userChallengeList.getList(userChallengeList.getIndex("Asesor"))[position].id.toString())
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
