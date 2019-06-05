package com.coders.codebook

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast

class CreatePost : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        /*Botones de los menu*/
        val bpopup_languages = findViewById<Button>(R.id.languages_popup)
        val bpopup_dificulty = findViewById<Button>(R.id.dificulty_popup)
        val bpopup_time = findViewById<Button>(R.id.time_popup)

        bpopup_languages.setOnClickListener {
            val popup_l = PopupMenu(this, bpopup_languages)
            popup_l.inflate(R.menu.programming_languages)
            popup_l.setOnMenuItemClickListener {
                Toast.makeText(this, "Item : " + it.title,Toast.LENGTH_SHORT).show()
                true
            }
            popup_l.show()
        }
        bpopup_dificulty.setOnClickListener{
            val popup_d = PopupMenu(this, bpopup_dificulty)
            popup_d.inflate(R.menu.difficulties)
            popup_d.setOnMenuItemClickListener{
                Toast.makeText(this, "Item : " + it.title, Toast.LENGTH_SHORT).show()
                true
            }
            popup_d.show()
        }
        bpopup_time.setOnClickListener{
            val popup_t = PopupMenu(this, bpopup_time)
            popup_t.inflate(R.menu.timers)
            popup_t.setOnMenuItemClickListener {
                Toast.makeText(this, "Item : " + it.title, Toast.LENGTH_SHORT).show()
                true
            }
            popup_t.show()
        }
    }
}
