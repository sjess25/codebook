package com.coders.codebook

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val blogin = findViewById<Button>(R.id.login_enter)
        val bsignup = findViewById<Button>(R.id.to_signup_button)

        blogin.setOnClickListener( View.OnClickListener {
            if (Network.vNetwork(this)){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Sin conexion", Toast.LENGTH_LONG).show()
            }

        } )

        bsignup.setOnClickListener(View.OnClickListener {
            //Toast.makeText(this, "registrarse", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, signup::class.java)
            startActivity(intent)
        })
    }
}
