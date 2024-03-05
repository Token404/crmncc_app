package it.crmnccgroup.crmncc.ui.auth

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import it.crmnccgroup.crmncc.ui.main.MainActivity
import it.crmnccgroup.crmncc.R

@AndroidEntryPoint
class Login : AppCompatActivity() {

    private var shortAnimationDuration: Int = 1000
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = Firebase.auth

        // aggiunta di un delay per la schermata di caricamento iniziale
        Handler(Looper.getMainLooper()).postDelayed({
            ObjectAnimator.ofFloat(findViewById<ImageView>(R.id.imageLogo),
                "translationY",
                -150f).apply {//animazione che muove logo e textbox
                    duration = 1000
                    start()
            }

            findViewById<ProgressBar>(R.id.loadingCircle).animate()
                .alpha(0f)
                .setDuration(shortAnimationDuration.toLong())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        findViewById<ProgressBar>(R.id.loadingCircle).visibility = View.GONE
                        findViewById<TextInputLayout>(R.id.casellaMail).apply {//fade animation textbox mail
                            alpha = 0f
                            visibility = View.VISIBLE
                            animate()
                                .alpha(1f)
                                .setDuration(shortAnimationDuration.toLong())
                                .setListener(null)
                        }
                        findViewById<TextInputLayout>(R.id.casellaPassword).apply {//fade animation textbox password
                            alpha = 0f
                            visibility = View.VISIBLE
                            animate()
                                .alpha(1f)
                                .setDuration(shortAnimationDuration.toLong())
                                .setListener(null)
                        }
                        findViewById<Button>(R.id.buttonLogin).apply {//fade animation textbox bottone login
                            alpha = 0f
                            visibility = View.VISIBLE
                            animate()
                                .alpha(1f)
                                .setDuration(shortAnimationDuration.toLong())
                                .setListener(null)
                        }
                    }
                })
        }, 2000)

        findViewById<Button>(R.id.buttonLogin).setOnClickListener {//listener onclick bottone
            val email: String = findViewById<TextInputLayout>(R.id.casellaMail).editText?.text.toString()
            val password: String = findViewById<TextInputLayout>(R.id.casellaPassword).editText?.text.toString()

            if(email.equals("")) {
                findViewById<TextInputLayout>(R.id.casellaMail).error = "Fornisci un'email"
            }
            else {
                if(password.equals("")) {
                    findViewById<TextInputLayout>(R.id.casellaPassword).error = "Fornisci una password"
                }
                else {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {

                                // success nel login, si passa all'attività principale
                                val intent = Intent(this, MainActivity::class.java)

                                // inizio dell'attività principale
                                startActivity(intent)

                                // fine dell'attività di login
                                finish()
                            } else {

                                if(task.exception?.message.toString() == "The email address is badly formatted.") {
                                    findViewById<TextInputLayout>(R.id.casellaMail).error = "L'email fornita non è valida"
                                }
                                else
                                {
                                    findViewById<TextInputLayout>(R.id.casellaMail).error = "Controlla la tua email"
                                    findViewById<TextInputLayout>(R.id.casellaPassword).error = "Controlla la tua password"
                                }
                            }
                        }
                }
            }


        }
    }

    // riduce la tastiera quando si clicca al di fuori dell'area di inserimento
    override fun dispatchTouchEvent(event: MotionEvent): Boolean {

        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    Log.d("focus", "touchevent")
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}