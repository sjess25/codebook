package com.coders.codebook

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu

class CreatePost : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        /*Boton para continuar con la siguiente parte del post*/
        val bnext = findViewById<Button>(R.id.next_button)

        /*Botones de los menu*/
        val bpopup_languages = findViewById<Button>(R.id.languages_popup)
        val bpopup_dificulty = findViewById<Button>(R.id.dificulty_popup)
        val bpopup_time = findViewById<Button>(R.id.time_popup)

        val textTitle = findViewById<EditText>(R.id.challenge_tittle)
        val textDescrption = findViewById<EditText>(R.id.challenge_description)

        val textRef1 = findViewById<EditText>(R.id.link_1)
        val textRef2 = findViewById<EditText>(R.id.link_2)
        val textRef3 = findViewById<EditText>(R.id.link_3)

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
        bnext.setOnClickListener(View.OnClickListener {

            newChallenge.create(dataUser.getIDTechnology(bpopup_languages.getText().toString()).toString(), textTitle.getText().toString(), textDescrption.getText().toString(),bpopup_dificulty.getText().toString().toInt(), bpopup_time.getText().toString().toInt(), dataUser.getID(), textRef1.getText().toString(), textRef2.getText().toString(), textRef3.getText().toString())
            val intent_post = Intent(this, PostSololution::class.java)
            intent_post.putExtra("id", "0")
            startActivity(intent_post)
        })
    }
}
