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

        val miChallenges: ArrayList<technology> = ArrayList()


        if(Network.vNetwork(this)) {
            val params = LinkedHashMap<String,String>()
            params["ID"] = 1.toString()
            params["Owner"] = dataUser.getID().toString()
            val jsonUser = JSONObject(params)
            val jsonList =JSONArray()
            jsonList.put(jsonUser)
            Network.getJsonArray(this, "http://35.231.202.82:81/data", jsonList, Response.Listener<JSONArray>{
                    response_Json ->
                try {
                    for (i in 0.. (response_Json.length() - 1)){
                        Log.d("json", response_Json.toString())
                        miChallenges.add(technology(response_Json.getJSONObject(i).getInt("ID"), response_Json.getJSONObject(i).getString("Title"), response_Json.getJSONObject(i).getString("Description"), dataUser.getDrawable(response_Json.getJSONObject(i).getInt("Technologie"))))
                    }
                    val listChallenge = findViewById<ListView>(R.id.myChallengeList)
                    val adapter = custumAdapter(this, miChallenges)

                    listChallenge.adapter = adapter

                    listChallenge.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                        Toast.makeText(this, miChallenges.get(position).id.toString(), Toast.LENGTH_LONG).show()
                    }
                }catch (e: Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "Sin conexion", Toast.LENGTH_LONG).show()
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
    }
}
