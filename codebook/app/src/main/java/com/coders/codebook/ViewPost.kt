package com.coders.codebook

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class ViewPost : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_post)

        var bundle :Bundle ?=intent.extras
        var state = bundle!!.getString("state")

        var owner = findViewById<Button>(R.id.user_nickname)
        var stateChallenge = findViewById<Button>(R.id.state_button)

        var title = findViewById<TextView>(R.id.challenge_Title)
        var time = findViewById<TextView>(R.id.challenge_Status)
        var descriptionChallenge = findViewById<TextView>(R.id.challenge_description)

        var likes = findViewById<TextView>(R.id.like_meter)
        var dislikes = findViewById<TextView>(R.id.dislike_meter)

        var ref1 = findViewById<TextView>(R.id.bibliography_1)
        var ref2 = findViewById<TextView>(R.id.bibliography_2)
        var ref3 = findViewById<TextView>(R.id.bibliography_3)


        owner.setText(newChallenge.owner.toString())
        stateChallenge.setText(state)

        title.setText(newChallenge.title)
        time.setText("%s %s".format(newChallenge.timeLimit.toString(),"dias para responder"))
        descriptionChallenge.setText(newChallenge.description)

        likes.setText(newChallenge.likes.toString())
        dislikes.setText(newChallenge.dislikes.toString())

        ref1.setText("%s %s".format("ref: ", newChallenge.ref1))
        ref2.setText("%s %s".format("ref: ", newChallenge.ref2))
        ref3.setText("%s %s".format("ref: ", newChallenge.ref3))

    }
}
