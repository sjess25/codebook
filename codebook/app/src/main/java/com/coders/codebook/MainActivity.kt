package com.coders.codebook

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.view.View
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if(dataUser.getTeacher()){

            val bProfile = findViewById<ImageButton>(R.id.vw_profiles)

            bProfile.setOnClickListener( View.OnClickListener {

                if(Network.vNetwork(this)) {
                    val params = LinkedHashMap<String,String>()
                    params["ID"] = 1.toString()
                    params["Owner"] = dataUser.getID().toString()
                    val jsonUser = JSONObject(params)
                    val jsonList = JSONArray()
                    jsonList.put(jsonUser)
                    Network.getJsonArray(this, "http://35.231.202.82:81/data", jsonList, Response.Listener<JSONArray>{
                            response_Json ->
                        try {
                            for (i in 0.. (response_Json.length() - 1)){
                                listChallenges.insertChallenge(technology(response_Json.getJSONObject(i).getInt("ID"), response_Json.getJSONObject(i).getString("Title"), response_Json.getJSONObject(i).getString("Description"), dataUser.getDrawable(response_Json.getJSONObject(i).getInt("Technologie"))))
                                Log.d("list challenge", listChallenges.getList().get(i).title)
                            }
                            val intent = Intent(this, profile::class.java)
                            startActivity(intent)
                        }catch (e: Exception){
                            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Toast.makeText(this, "Sin conexion", Toast.LENGTH_LONG).show()
                }

            } )


        } else {
            val bProfile = findViewById<ImageButton>(R.id.vw_profiles)

            bProfile.setOnClickListener( View.OnClickListener {
                val intent = Intent(this, EditProfile::class.java)
                startActivity(intent)
            } )
        }
    }
}
