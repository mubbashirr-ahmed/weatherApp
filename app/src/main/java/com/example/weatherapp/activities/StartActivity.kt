package com.example.weatherapp.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityStartBinding
import com.google.android.material.snackbar.Snackbar
import java.util.*

class StartActivity : AppCompatActivity() {

    lateinit var binding: ActivityStartBinding
    lateinit var res: String
    private val REQUEST_MICROPHONE_PERMISSION_CODE: Int by lazy { 1001 }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        checkPermissions()
        clickListeners()
    }

    private fun clickListeners() {
        binding.bDone.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("cityName", res)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun startSpeechToText() {

        val speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!SpeechRecognizer.isRecognitionAvailable(this)) {
                speechRecognizer.triggerModelDownload(speechRecognizerIntent)
            }
        }
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(bundle: Bundle) {}
            override fun onBeginningOfSpeech() {
                binding.ripple.startRippleAnimation()
            }

            override fun onRmsChanged(v: Float) {}
            override fun onBufferReceived(bytes: ByteArray) {}
            override fun onEndOfSpeech() {
                binding.ripple.stopRippleAnimation()
            }

            @SuppressLint("SetTextI18n")
            override fun onError(i: Int) {
                binding.ripple.stopRippleAnimation()
                binding.tvTap.text = "Tap again to record"
                binding.editText.hint = "Try Saying London"
            }

            override fun onResults(bundle: Bundle) {
                val matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null) {
                    res = matches[0]
                    binding.bDone.visibility = View.VISIBLE
                }
                binding.editText.text = res
                binding.ripple.stopRippleAnimation()
                binding.tvTap.text = getString(R.string.tag_again)
            }

            override fun onPartialResults(bundle: Bundle) {
                binding.ripple.stopRippleAnimation()
            }

            override fun onEvent(i: Int, bundle: Bundle) {
                binding.ripple.stopRippleAnimation()
            }
        })
        binding.btSpeech.setOnClickListener {
            checkPermissions()
            binding.bDone.visibility = View.GONE
            speechRecognizer.startListening(speechRecognizerIntent)
            binding.editText.text = ""
            binding.editText.hint = "Listening..."

        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                REQUEST_MICROPHONE_PERMISSION_CODE
            )
        } else {
            isSpeechRecognitionAvailable()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_MICROPHONE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isSpeechRecognitionAvailable()
                } else {
                    Snackbar.make(
                        findViewById(android.R.id.content),
                        "Permission is required for app to function properly!",
                        Snackbar.LENGTH_INDEFINITE
                    ).setAction("Settings") {
                        gotoSettings()
                    }.show()
                }
                return
            }
        }
    }

    private fun gotoSettings() {
        startActivity(
            Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:$packageName")
            )
        )

    }

    fun isSpeechRecognitionAvailable() {
        startSpeechToText()
//        if(SpeechRecognizer.isRecognitionAvailable(this)){
//            startSpeechToText()
//        }
//        Snackbar.make(
//            findViewById(android.R.id.content),
//            "Download Speech Recognition services for app to function properly!",
//            Snackbar.LENGTH_INDEFINITE
//        ).setAction("Install") {
//            installSRService()
//        }.show()
    }




}