package com.coders.codebook

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast

class CreatePost : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        /*Boton para publicar*/
        val bpost = findViewById<Button>(R.id.post_button)

        /*Botones de los menu*/
        val bpopup_languages = findViewById<Button>(R.id.languages_popup)
        val bpopup_dificulty = findViewById<Button>(R.id.dificulty_popup)
        val bpopup_time = findViewById<Button>(R.id.time_popup)

        bpopup_languages.setOnClickListener {
            val popup_l = PopupMenu(this, bpopup_languages)
            popup_l.inflate(R.menu.programming_languages)
            popup_l.setOnMenuItemClickListener {
                bpopup_languages.setText(it.title)
                true
            }
            popup_l.show()
        }
        bpopup_dificulty.setOnClickListener{
            val popup_d = PopupMenu(this, bpopup_dificulty)
            popup_d.inflate(R.menu.difficulties)
            popup_d.setOnMenuItemClickListener{
                bpopup_dificulty.setText(it.title)
                true
            }
            popup_d.show()
        }
        bpopup_time.setOnClickListener{
            val popup_t = PopupMenu(this, bpopup_time)
            popup_t.inflate(R.menu.timers)
            popup_t.setOnMenuItemClickListener {
                bpopup_time.setText(it.title)
                true
            }
            popup_t.show()
        }
        bpost.setOnClickListener(View.OnClickListener {
            val intent_post = Intent(this, ViewPost::class.java)
            startActivity(intent_post)
        })
    }
}
