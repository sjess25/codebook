package com.coders.codebook

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class signup : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val bfacebook = findViewById<Button>(R.id.facebook_signup_button)
        val bgmail = findViewById<Button>(R.id.gmail_signup_button)

        bfacebook.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Registro por FaceBook", Toast.LENGTH_SHORT).show()
        })

        bgmail.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Registro por GMail", Toast.LENGTH_SHORT).show()
        })
    }
}
