package com.codebreakers.WorkItOut

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.window.SplashScreen
import com.codebreakers.WorkItOut.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val SPLASH_TIME_OUT:Long = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val text_logo : TextView = findViewById(R.id.textLogo)
        val slideAnimation = AnimationUtils.loadAnimation(this,R.anim.side_slide)
        text_logo.startAnimation(slideAnimation)
        Handler().postDelayed({
            startActivity(Intent(this,ActivitySecond::class.java))
            finish()
        },SPLASH_TIME_OUT)
    }
}