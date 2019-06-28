package com.coders.codebook

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    override fun onDestroy() {
        super.onDestroy()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)

        logo_app.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_in))
        Handler().postDelayed({
            logo_app.startAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_out))
            Handler().postDelayed({
                startActivity(Intent(this, login::class.java))
                finish()
            }, 500)
        }, 2500)
    }
}
