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

class login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val blogin = findViewById<Button>(R.id.login_enter)
        val bsignup = findViewById<Button>(R.id.to_signup_button)

        val editNickname = findViewById<EditText>(R.id.enterNickname)
        val editPassword = findViewById<EditText>(R.id.enterPassword)

        val C: login = this

        blogin.setOnClickListener( View.OnClickListener {
            if (Network.vNetwork(this)){
                dataUser.create(editNickname.getText().toString(), editPassword.getText().toString())
                Network.getHTTP(this, "http://35.231.202.82:81/login?user=${dataUser.getNickName()}&password=${dataUser.getPassword()}", Response.Listener<String>{
                        response ->
                    try {
                        if (response == "0") {
                            val params = LinkedHashMap<String,String>()
                            params["ID"] = 0.toString()
                            params["User"] = dataUser.getNickName()
                            params["Password"] = dataUser.getPassword()
                            val jsonKey = JSONObject(params)
                            Network.getJson(this, "http://35.231.202.82:81/data", jsonKey, Response.Listener<JSONObject>{
                                    response_Json ->
                                try {
                                    Log.d("json enviado", jsonKey.toString())
                                    dataUser.setID(response_Json.get("ID").toString().toInt())
                                    dataUser.setName(response_Json.get("Name").toString())
                                    dataUser.setTeacher(response_Json.get("Teacher").toString().toBoolean())
                                    Toast.makeText(this, dataUser.getID().toString(), Toast.LENGTH_LONG).show()
                                    if (dataUser.getID() != -1){
                                        val intent = Intent(this, MainActivity::class.java)
                                        startActivity(intent)
                                    }
                                }catch (e:Exception){
                                    Toast.makeText(this, response_Json.toString(), Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                }
                            })
                            startActivity(intent)
                        } else if (response == "1") {
                            Toast.makeText(C, "Incorrect Password", Toast.LENGTH_SHORT).show()

                        } else if (response == "2") {
                            Toast.makeText(C, "User not found", Toast.LENGTH_SHORT).show()
                            val intent_signup = Intent(this, signup::class.java)
                            startActivity(intent_signup)
                        } else {
                            Toast.makeText(C, "error", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(C, e.message, Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(C, "Sin conexion", Toast.LENGTH_LONG).show()
            }

        } )

        bsignup.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, signup::class.java)
            startActivity(intent)
        })
    }
}
