package com.coders.codebook

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Response
import org.json.JSONObject
import java.lang.Exception

class Searcher : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searcher)

        val bundle: Bundle? =intent.extras
        val idTec: String = bundle!!.getString("id")!!


        val listChallenge = findViewById<ListView>(R.id.searchList)
        val adapter = custumAdapter(this, userChallengeList.getList(userChallengeList.getIndex(idTec)))
        listChallenge.adapter = adapter

        val bBack = findViewById<Button>(R.id.back_button)

        bBack.setOnClickListener {
            (this as Searcher).finish()
        }


        listChallenge.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            //Toast.makeText(this, listChallenges.getList()[position].title, Toast.LENGTH_LONG).show()
            if(Network.vNetwork(this)) {
                val params = LinkedHashMap<String,String>()
                params["ID"] = 4.toString()
                params["Challenge"] = userChallengeList.getList(userChallengeList.getIndex(idTec))[position].id.toString() //listChallenges.getList()[position].id.toString()
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
                        newChallenge.id = response_Json["ID"].toString().toInt()
                        newChallenge.ownerNickname = response_Json["OwnerNickname"].toString()
                        val intentInfo = Intent(this, ViewPost::class.java)
                        intentInfo.putExtra("state", "Aceptar Desafio")
                        intentInfo.putExtra("challenge", userChallengeList.getList(userChallengeList.getIndex(idTec))[position].id.toString())
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
