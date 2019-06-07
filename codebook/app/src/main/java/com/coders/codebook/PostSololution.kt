package com.coders.codebook

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class PostSololution : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_sololution)

        /*Boton para publicar*/
        val bpost = findViewById<Button>(R.id.next_button)

        bpost.setOnClickListener(View.OnClickListener {
            val intent_post = Intent(this, profile::class.java)
            startActivity(intent_post)
        })
    }
}
