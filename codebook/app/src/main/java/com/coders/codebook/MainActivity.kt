package com.coders.codebook

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.view.View
import android.content.Intent
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Response
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import android.os.Handler
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mHandler: Handler
    private lateinit var mRunnable:Runnable



    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*Botones para el intent de my_challenges*/
        val bChallengeC      = findViewById<ImageButton>(R.id.c_challenges)
        val bChallengeJava   = findViewById<ImageButton>(R.id.java_challenges)
        val bChallengeProlog = findViewById<ImageButton>(R.id.prolog_challenges)
        val bChallengeRuby   = findViewById<ImageButton>(R.id.ruby_challenges)
        val bChallengePraxis = findViewById<ImageButton>(R.id.praxis_challenges)

        val listChallenge = findViewById<ListView>(R.id.myChallengeList)
        var adapter = custumAdapter(this, userChallengeList.getList(userChallengeList.getIndex("Activos")))

        listChallenge.adapter = adapter


        mHandler = Handler()

        val swipeContainer = findViewById<SwipeRefreshLayout>(R.id.update_main)
        swipeContainer.setOnRefreshListener {
            mRunnable = Runnable {
                if (Network.vNetwork(this)) {
                    val paramsArray = LinkedHashMap<String,String>()
                    paramsArray["ID"] = 5.toString()
                    paramsArray["Who"] = dataUser.getID().toString()
                    val jsonChallenge = JSONObject(paramsArray)
                    val jsonKeyAChallenge = JSONArray()
                    jsonKeyAChallenge.put(jsonChallenge)

                    Network.getJsonArray(this, "http://35.231.202.82:81/data", jsonKeyAChallenge, Response.Listener<JSONArray>{
                            response_Array ->
                        try {
                            userChallengeList.allChallenge[userChallengeList.getIndex("Activos")]!!.clear()
                            Log.d("json active challenge", response_Array.toString())
                            for (i in 0.. (response_Array.length() - 1)){
                                listChallenges.insertChallenge(technology(response_Array.getJSONObject(i).getInt("ID"), response_Array.getJSONObject(i).getString("Title"), response_Array.getJSONObject(i).getString("Description"), dataUser.getDrawable(response_Array.getJSONObject(i).getInt("Technologie"))))
                                Log.d("list challenge", listChallenges.getList().get(i).title)
                            }

                            userChallengeList.insertListChallenge(userChallengeList.getIndex("Activos"))
                            listChallenges.clear()
                            adapter = custumAdapter(this, userChallengeList.getList(userChallengeList.getIndex("Activos")))
                            listChallenge.adapter = adapter
                        } catch (e:Exception) {
                            Log.d("error json active challenge", e.message)
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
                            updateListChallenge(response_Json, "Asesor")
                            val intent = Intent(this, profile::class.java)
                            startActivity(intent)
                        }catch (e: Exception){
                            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                            Log.d("all challenge", e.message)
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
        if (Network.vNetwork(this)) {

            bChallengeC.setOnClickListener(View.OnClickListener {
                getListFinishChallenge("C")

            })

            bChallengeJava.setOnClickListener(View.OnClickListener {
                getListFinishChallenge("Java")
            })

            bChallengeProlog.setOnClickListener(View.OnClickListener {
                getListFinishChallenge("Prolog")

            })

            bChallengeRuby.setOnClickListener(View.OnClickListener {
                getListFinishChallenge("Ruby")

            })

            bChallengePraxis.setOnClickListener(View.OnClickListener {
                getListFinishChallenge("Praxis")
            })

            listChallenge.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
                //Toast.makeText(this, listChallenges.getList()[position].title, Toast.LENGTH_LONG).show()
                if(Network.vNetwork(this)) {
                    val params = LinkedHashMap<String,String>()
                    params["ID"] = 4.toString()
                    params["Challenge"] = userChallengeList.getList(userChallengeList.getIndex("Activos"))[position].id.toString() //listChallenges.getList()[position].id.toString()
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
                            intentInfo.putExtra("state", "Responder")
                            intentInfo.putExtra("challenge", userChallengeList.getList(userChallengeList.getIndex("Activos"))[position].id.toString())
                            startActivity(intentInfo)
                        }catch (e: Exception){
                            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                        }
                    })
                } else {
                    Toast.makeText(this, "Sin conexion", Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(this, "Sin conexion", Toast.LENGTH_LONG).show()
        }

    }

    fun getListFinishChallenge(technology: String) {

        val paramsArray = LinkedHashMap<String,String>()
        paramsArray["ID"] = 6.toString()
        paramsArray["Who"] = dataUser.getID().toString()
        paramsArray["Technologie"] = dataUser.getIDTechnology(technology).toString()
        val jsonChallenge = JSONObject(paramsArray)
        val jsonKeyAChallenge = JSONArray()
        jsonKeyAChallenge.put(jsonChallenge)

        Network.getJsonArray(this, "http://35.231.202.82:81/data", jsonKeyAChallenge, Response.Listener<JSONArray>{
                response_Array ->
            try {
                Log.d("json send technologie", jsonKeyAChallenge.toString())
                Log.d("json finish challenge", response_Array.toString())
                updateListChallenge(response_Array, technology)

                val intentChallenge = Intent(this, myChallenge::class.java)
                intentChallenge.putExtra("id", technology)
                startActivity(intentChallenge)
            } catch (e:Exception) {
                Log.d("json active challenge", response_Array.toString())
            }
        })
    }

    fun updateListChallenge(list: JSONArray, type: String){

        for (i in 0.. (list.length() - 1)){
            listChallenges.insertChallenge(technology(list.getJSONObject(i).getInt("ID"), list.getJSONObject(i).getString("Title"), list.getJSONObject(i).getString("Description"), dataUser.getDrawable(list.getJSONObject(i).getInt("Technologie"))))
            Log.d("list challenge", listChallenges.getList().get(i).title)
        }

        userChallengeList.insertListChallenge(userChallengeList.getIndex(type))
        listChallenges.clear()

    }
}
