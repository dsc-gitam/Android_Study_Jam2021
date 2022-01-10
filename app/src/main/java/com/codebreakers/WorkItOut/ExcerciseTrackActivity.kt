package com.codebreakers.WorkItOut

import android.app.Dialog
import android.content.IntentSender
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.codebreakers.WorkItOut.databinding.ActivityExcerciseTrackBinding
import com.codebreakers.WorkItOut.databinding.CustomDialogBoxBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExcerciseTrackActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExcerciseTrackBinding?=null

    private var restTimer: CountDownTimer?=null
    private var restProgress = 0

    private var excerciseTimer: CountDownTimer?=null
    private var excerciseProgress = 0

    private var excerciseList:ArrayList<ExcerciseModel>?=null
    private var currentExcercisePosition=-1

    private var tts:TextToSpeech?=null
    private var player:MediaPlayer?=null
    private var exerciseAdapter: ExerciseStatusAdapter? = null
    private var exerciseTimerDuration:Long = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityExcerciseTrackBinding.inflate(layoutInflater)
        setContentView(binding?.root)
       setSupportActionBar(binding?.toolbarExercise)
        if(supportActionBar!=null)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // adding excercise list
        excerciseList = Constants.defaultExcerciseList()
        tts = TextToSpeech(this,this)

        binding?.toolbarExercise?.setNavigationOnClickListener{
            customDialogForBackButton()
        }
        setUpRestView()
        setupExerciseStatusRecyclerView()
    }

////..........check from here
    private fun setupExerciseStatusRecyclerView() {

        // Defining a layout manager for the recycle view
        // Here we have used a LinearLayout Manager with horizontal scroll.
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // As the adapter expects the exercises list and context so initialize it passing it.
         exerciseAdapter  = ExerciseStatusAdapter(excerciseList!!)

        // Adapter class is attached to recycler view
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }
    // END


    private fun setUpRestView()
    {
        try{
            val soundURI = Uri.parse("android.resource://com.codebreakers.WorkItOut/"+ R.raw.sound)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping=false
            player?.start()
        }catch (e: Exception){
            e.printStackTrace()
        }
        binding?.flRestView?.visibility=View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility=View.INVISIBLE
        binding?.tvExerciseName?.visibility=View.INVISIBLE
        binding?.ivImage?.visibility=View.VISIBLE
        binding?.upcomingLabel?.visibility=View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility=View.VISIBLE
        if(restTimer!=null){
            restTimer?.cancel()
            restProgress=0
        }

        binding?.tvUpcomingExerciseName?.text = excerciseList!![currentExcercisePosition+1].getName()
        setRestProgressBar()
    }

    private fun setUpExcerciseView()
    {
       binding?.flRestView?.visibility=View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility=View.VISIBLE
        binding?.tvExerciseName?.visibility=View.VISIBLE
        binding?.ivImage?.visibility=View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility=View.INVISIBLE
        binding?.upcomingLabel?.visibility=View.INVISIBLE

        if(excerciseTimer!=null){
            excerciseTimer?.cancel()
            excerciseProgress=0
        }
        speakOut(excerciseList!![currentExcercisePosition].getName())
        binding?.ivImage?.setImageResource(excerciseList!![currentExcercisePosition].getImage())
        binding?.tvExerciseName?.text =excerciseList!![currentExcercisePosition].getName()
        setExcerciseProgressBar()
    }


    private fun setRestProgressBar()
    {
        binding?.progressBar?.progress=restProgress
        restTimer = object : CountDownTimer(10000,1000){
            override fun onTick(millisUntilFinished:Long) {
                restProgress++
                binding?.progressBar?.progress=10-restProgress
                binding?.tvTimer?.text=(10 - restProgress).toString()
            }

            override fun onFinish() {
                currentExcercisePosition++
                excerciseList!![currentExcercisePosition].setIsSelected(true) // Current Item is selected
                exerciseAdapter?.notifyDataSetChanged() // Notified the current item to adapter class to reflect it into UI.
                setUpExcerciseView()
            }
        }.start()
    }

    override fun onDestroy() {
        if(restTimer!=null){
            restTimer?.cancel()
            restProgress=0
        }
        if(excerciseTimer!=null){
            excerciseTimer?.cancel()
            excerciseProgress=0
        }
        // Shutting down the Text to Speech feature when activity is destroyed
        // START
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        // END
        //  When the activity is destroyed if the media player instance is not null then stop it.)
        // START
        if(player != null){
            player!!.stop()
        }
        // END
        super.onDestroy()
        binding=null
    }
    private fun setExcerciseProgressBar()
    {
        binding?.progressBarExercise?.progress=excerciseProgress
        excerciseTimer = object : CountDownTimer(exerciseTimerDuration * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                excerciseProgress++
                binding?.progressBarExercise?.progress = exerciseTimerDuration.toInt() - excerciseProgress
                binding?.tvTimerExercise?.text = (exerciseTimerDuration.toInt() - excerciseProgress).toString()
            }

            override fun onFinish() {
            if(currentExcercisePosition <excerciseList?.size!!-1){
                excerciseList!![currentExcercisePosition].setIsSelected(false) // exercise is completed so selection is set to false
                excerciseList!![currentExcercisePosition].setIsCompleted(true) // updating in the list that this exercise is completed
                exerciseAdapter?.notifyDataSetChanged()
                setUpRestView()
            }else {
                Toast.makeText(this@ExcerciseTrackActivity,"You've completed all the excercise .. ",Toast.LENGTH_SHORT).show()
            }
            }
        }.start()
    }

    override fun onInit(status: Int) {
        //  After variable initializing set the language after a "success"ful result.)
        // START
        if (status == TextToSpeech.SUCCESS) {
            // set US English as language for tts
            val result = tts?.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            }

        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }
    private fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    override fun onBackPressed() {
        customDialogForBackButton()
        super.onBackPressed()
    }

    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        //create a binding variable
        val dialogBinding = CustomDialogBoxBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)

        customDialog.setCanceledOnTouchOutside(false)
        dialogBinding.tvYes.setOnClickListener {

            this@ExcerciseTrackActivity.finish()
            customDialog.dismiss() // Dialog will be dismissed
        }
        dialogBinding.tvNo.setOnClickListener {
            customDialog.dismiss()
        }
        //Start the dialog and display it on screen.
        customDialog.show()
    }


}