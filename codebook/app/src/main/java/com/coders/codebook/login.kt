package com.coders.codebook

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
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
                val intent = Intent(this, MainActivity::class.java)
                dataUser.create(editNickname.getText().toString(), editPassword.getText().toString())
                Network.getHTTP(this, "http://35.231.202.82:81/login?user=${dataUser.getnickName()}&password=${dataUser.getPassword()}", Response.Listener<String>{
                        response ->
                    try {
                        if (response == "0") {
                            startActivity(intent)
                        } else if (response == "1") {
                            Toast.makeText(C, "Incorrect Password", Toast.LENGTH_SHORT).show()

                        } else if (response == "2") {
                            Toast.makeText(C, "User not found", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, signup::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(C, "error", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(C, e.message, Toast.LENGTH_SHORT).show()
                    }
                })
                //Toast.makeText(this, dataUser.getnickName() + " " + dataUser.getPassword(), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(C, "Sin conexion", Toast.LENGTH_LONG).show()
            }

        } )

        bsignup.setOnClickListener(View.OnClickListener {
            //Toast.makeText(this, "registrarse", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, signup::class.java)
            startActivity(intent)
        })
    }
}
