package com.codebreakers.WorkItOut

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import com.codebreakers.WorkItOut.databinding.ActivityMainBinding
import com.codebreakers.WorkItOut.databinding.ActivitySecondBinding

class ActivitySecond : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val fLStartButton: FrameLayout = findViewById(R.id.flStart)
        fLStartButton.setOnClickListener {
            val intent = Intent(this,ExcerciseTrackActivity::class.java)
            startActivity(intent)
        }
        val fBMI: FrameLayout = findViewById(R.id.flBMI)
        fBMI.setOnClickListener{
            val intent = Intent(this,BMIActivity::class.java)
            startActivity(intent)
        }

        val fHistoy: FrameLayout = findViewById(R.id.flHistory)
       fHistoy.setOnClickListener{
           val intent = Intent(this,HistoryActivity::class.java)
           startActivity(intent)
       }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}