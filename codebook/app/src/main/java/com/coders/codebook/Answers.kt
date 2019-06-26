package com.coders.codebook

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.util.LinkedHashMap

class Answers : AppCompatActivity() {

    private lateinit var mHandler: Handler
    private lateinit var mRunnable:Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answers)

        val bundle :Bundle ?=intent.extras
        val challenge: String = bundle!!.getString("challenge")!!

        val listChallenge = findViewById<ListView>(R.id.answers_students)
        var adapter = AdapterAnswer(this, userChallengeList.allAnswer)
        listChallenge.adapter = adapter

        val towner = findViewById<TextView>(R.id.nickname_owner)
        val tAsnwerOwner = findViewById<TextView>(R.id.answer_teacher)

        towner.setText(userChallengeList.answerOwner.owner)
        tAsnwerOwner.setText(userChallengeList.answerOwner.answer)

        val bBack = findViewById<Button>(R.id.back_button)

        bBack.setOnClickListener {
            (this as Answers).finish()
        }
        val swipeContainer = findViewById<SwipeRefreshLayout>(R.id.update_main)
        mHandler = Handler()
        swipeContainer.setOnRefreshListener {
            mRunnable = Runnable {
                if(Network.vNetwork(this)) {
                    val params = LinkedHashMap<String, String>()
                    params["ID"] = 9.toString()
                    params["Challenge"] = challenge
                    val jsonUser = JSONObject(params)
                    val jsonList = JSONArray()
                    jsonList.put(jsonUser)
                    Network.getJsonArray(this, "http://35.231.202.82:81/data", jsonList, Response.Listener<JSONArray>{
                            response_Json ->
                        try {
                            Log.d("all challenge", response_Json.toString())
                            var ownerAnswer: String = ""
                            var answer: String = ""
                            if (userChallengeList.allAnswer.size > 0) {
                                userChallengeList.allAnswer.clear()
                            }
                            for (i in 0.. (response_Json.length() - 1)) {
                                ownerAnswer = response_Json.getJSONObject(i)["Owner"].toString()
                                answer = response_Json.getJSONObject(i)["Answer"].toString()

                                if (ownerAnswer == newChallenge.ownerNickname) {
                                    userChallengeList.saveOwnerAnswer(ownerAnswer, answer)
                                } else {
                                    userChallengeList.allAnswer.add(answer(ownerAnswer, answer))
                                }
                            }
                            adapter = AdapterAnswer(this, userChallengeList.allAnswer)
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
    }
}
