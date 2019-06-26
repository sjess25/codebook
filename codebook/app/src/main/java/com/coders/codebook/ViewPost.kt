package com.coders.codebook

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class ViewPost : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_post)

        val bundle :Bundle ?=intent.extras
        var state = bundle!!.getString("state")
        val challenge: String = bundle!!.getString("challenge")!!

        val owner = findViewById<Button>(R.id.user_nickname)
        val stateChallenge = findViewById<Button>(R.id.state_button)

        val title = findViewById<TextView>(R.id.challenge_Title)
        val time = findViewById<TextView>(R.id.challenge_Status)
        val descriptionChallenge = findViewById<TextView>(R.id.challenge_description)

        val likes = findViewById<TextView>(R.id.like_meter)
        val dislikes = findViewById<TextView>(R.id.dislike_meter)

        val ref1 = findViewById<TextView>(R.id.bibliography_1)
        val ref2 = findViewById<TextView>(R.id.bibliography_2)
        val ref3 = findViewById<TextView>(R.id.bibliography_3)


        owner.setText(newChallenge.ownerNickname)
        stateChallenge.setText(state)

        title.setText(newChallenge.title)
        time.setText("%s %s".format(newChallenge.timeLimit.toString(),"dias para responder"))
        descriptionChallenge.setText(newChallenge.description)

        likes.setText(newChallenge.likes.toString())
        dislikes.setText(newChallenge.dislikes.toString())

        ref1.setText("%s %s".format("ref: ", newChallenge.ref1))
        ref2.setText("%s %s".format("ref: ", newChallenge.ref2))
        ref3.setText("%s %s".format("ref: ", newChallenge.ref3))

        val bBack = findViewById<Button>(R.id.back_button)

        bBack.setOnClickListener {
            (this as ViewPost).finish()
        }

        val bAnswer = findViewById<Button>(R.id.state_button)

        bAnswer.setOnClickListener {

            when (state) {
                "Responder"-> {
                    val intentSolution = Intent(this, PostSololution::class.java)
                    intentSolution.putExtra("id", "1")
                    (this as ViewPost).finish()
                    startActivity(intentSolution)
                }
                "Ver Respuestas" -> {
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
                                val intentAnswer = Intent(this, Answers::class.java)
                                intentAnswer.putExtra("challenge", challenge)
                                (this as ViewPost).finish()
                                startActivity(intentAnswer)
                            }catch (e: Exception){
                                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                                Log.d("all challenge", e.message)
                            }
                        })
                    } else {
                        Toast.makeText(this, "Sin conexion", Toast.LENGTH_LONG).show()
                    }
                }
                "Aceptar Desafio" -> {
                    if(Network.vNetwork(this)) {
                        val params = LinkedHashMap<String,String>()
                        params["ID"] = 8.toString()
                        params["Who"] = dataUser.getID().toString()
                        params["Challenge"] = challenge
                        val jsonUser = JSONObject(params)

                        Network.getJson(this, "http://35.231.202.82:81/data", jsonUser, Response.Listener<JSONObject>{
                                response_Json ->
                            try {
                                Log.d("send accept", jsonUser.toString())
                                Log.d("response accept", response_Json.toString())
                                //state = "Responder"
                                //stateChallenge.setText(state)
                                val intentInfo = Intent(this, ViewPost::class.java)
                                intentInfo.putExtra("state", "Responder")
                                intentInfo.putExtra("challenge", challenge)
                                (this as ViewPost).finish()
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
    }
}
