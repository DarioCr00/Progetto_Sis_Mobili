package it.unibo.demo.nearpharm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.TextView

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DELAY = 2000L

    private val TAG = "Welcome"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val textView = findViewById<TextView>(R.id.textView)
        textView.text = "NEARPHARM"

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, PermissionLocationActivity::class.java)
            startActivity(intent)
            finish()
        }, SPLASH_DELAY)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")

    //mettere l'handler qui se si vuole creare il loop infinito generato con il tasto back

    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }
}
