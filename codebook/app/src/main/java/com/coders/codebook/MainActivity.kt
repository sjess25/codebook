package com.coders.codebook

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.view.View
import android.content.Intent

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        if(dataUser.getTeacher()){
            val bProfile = findViewById<ImageButton>(R.id.vw_profiles)

            bProfile.setOnClickListener( View.OnClickListener {
                val intent = Intent(this, profile::class.java)
                startActivity(intent)
            } )
        }
    }
}
