package com.coders.codebook

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val blogin = findViewById<Button>(R.id.login_enter)

        blogin.setOnClickListener( View.OnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } )
    }
}
