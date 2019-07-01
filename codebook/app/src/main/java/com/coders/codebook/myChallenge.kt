package com.coders.codebook

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.android.volley.Response
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class myChallenge : AppCompatActivity() {

    private lateinit var mHandler: Handler
    private lateinit var mRunnable:Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_challenge)


        val c = this

        val bundle: Bundle? =intent.extras
        val idTec: String = bundle!!.getString("id")!!

        val listChallenge = findViewById<ListView>(R.id.myChallengeList)
        var adapter = custumAdapter(this, userChallengeList.getList(userChallengeList.getIndex(idTec)))
        listChallenge.adapter = adapter

        val search = findViewById<SearchView>(R.id.searchChallenges)
        search.setOnClickListener {
            search.setIconifiedByDefault(false)
        }

        val bBack = findViewById<Button>(R.id.back_button)
        bBack.setOnClickListener {
            finish()
        }


        val swipeContainer = findViewById<SwipeRefreshLayout>(R.id.update_finishChalenge)
        mHandler = Handler()

        swipeContainer.setOnRefreshListener {
            mRunnable = Runnable {

                val paramsArray = java.util.LinkedHashMap<String, String>()
                paramsArray["ID"] = 6.toString()
                paramsArray["Who"] = dataUser.getID().toString()
                paramsArray["Technologie"] = dataUser.getIDTechnology(idTec).toString()
                val jsonChallenge = JSONObject(paramsArray)
                val jsonKeyAChallenge = JSONArray()
                jsonKeyAChallenge.put(jsonChallenge)

                Network.getJsonArray(this, "http://35.231.202.82:81/data", jsonKeyAChallenge, Response.Listener<JSONArray>{
                        response_Array ->
                    try {
                        userChallengeList.allChallenge[userChallengeList.getIndex(idTec)]!!.clear()
                        Log.d("json send technologie", jsonKeyAChallenge.toString())
                        Log.d("json finish challenge", response_Array.toString())

                        for (i in 0.. (response_Array.length() - 1)){
                            listChallenges.insertChallenge(technology(response_Array.getJSONObject(i).getInt("ID"), response_Array.getJSONObject(i).getString("Title"), response_Array.getJSONObject(i).getString("Description"), dataUser.getDrawable(response_Array.getJSONObject(i).getInt("Technologie"))))
                            Log.d("list challenge", listChallenges.getList().get(i).title)
                        }

                        userChallengeList.insertListChallenge(userChallengeList.getIndex(idTec))
                        listChallenges.clear()

                        adapter = custumAdapter(this, userChallengeList.getList(userChallengeList.getIndex(idTec)))
                        listChallenge.adapter = adapter

                    } catch (e:Exception) {
                        Log.d("json active challenge", response_Array.toString())
                    }
                })

                swipeContainer.isRefreshing = false
            }
            mHandler.postDelayed(mRunnable,
                1500.toLong())
        }


        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("search", query)
                if (Network.vNetwork(c)) {
                    val paramsArray = LinkedHashMap<String,String>()
                    paramsArray["ID"] = 7.toString()
                    paramsArray["Technologie"] = dataUser.getIDTechnology(idTec).toString()
                    paramsArray["Query"] = query
                    val jsonChallenge = JSONObject(paramsArray)
                    val jsonKeyAChallenge = JSONArray()
                    jsonKeyAChallenge.put(jsonChallenge)

                    Network.getJsonArray(c, "http://35.231.202.82:81/data", jsonKeyAChallenge, Response.Listener<JSONArray>{
                            response_Array ->
                        try {
                            Log.d("json search challenge", response_Array.toString())
                            for (i in 0.. (response_Array.length() - 1)){
                                listChallenges.insertChallenge(technology(response_Array.getJSONObject(i).getInt("ID"), response_Array.getJSONObject(i).getString("Title"), response_Array.getJSONObject(i).getString("Description"), dataUser.getDrawable(response_Array.getJSONObject(i).getInt("Technologie"))))
                                Log.d("list challenge", listChallenges.getList().get(i).title)
                            }

                            userChallengeList.insertListChallenge(userChallengeList.getIndex("Search"))
                            listChallenges.clear()

                            val intentSearch = Intent(c, Searcher::class.java)
                            intentSearch.putExtra("id", "Search")
                            startActivity(intentSearch)

                        } catch (e:Exception) {
                            Log.d("json search challenge", e.message)
                        }
                    })

                } else {
                    Toast.makeText(c, "Sin conexion", Toast.LENGTH_LONG).show()
                }
                return false
            }

        })

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
                        intentInfo.putExtra("state", "Ver Respuestas")
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
