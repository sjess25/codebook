package com.coders.codebook

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
import org.json.JSONObject
import java.lang.Exception

class PostSololution : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_sololution)

        /*Boton para publicar*/
        val bpost = findViewById<Button>(R.id.next_button)
        val textSolution = findViewById<EditText>(R.id.solution_text)

        bpost.setOnClickListener(View.OnClickListener {
            newChallenge.setAnswerChallenge(textSolution.getText().toString())
            Log.d("challenge new", newChallenge.getJsonSENDpost().toString())

            if(Network.vNetwork(this)) {
                Network.getJson(this, "http://35.231.202.82:81/data", newChallenge.getJsonSENDpost(), Response.Listener<JSONObject> {
                    response_newChallenge ->
                    try {
                        response_newChallenge
                        if(response_newChallenge.get("cID").toString().toInt() != -1) {
                            (this as PostSololution).finish()
                            val intent_post = Intent(this, profile::class.java)
                            startActivity(intent_post)
                        } else {
                            Toast.makeText(this, "Error, Intentar nuevamente", Toast.LENGTH_LONG).show()
                        }
                        Log.d("challenge request", response_newChallenge.toString())
                    } catch (e: Exception) {
                        Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
                    }
                })
            } else{
                Toast.makeText(this, "Sin conexion", Toast.LENGTH_LONG).show()
            }
        })
    }
}
