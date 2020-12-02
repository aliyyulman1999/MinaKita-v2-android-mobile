package org.telkomuniversity.minakitav2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty()){
                etEmail.error = "Email harus diisi"
                etEmail.requestFocus()
                return@setOnClickListener
            }

            //mengecek email valid atau tidaknya

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                etEmail.error = "Email tidak valid"
                etEmail.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()||password.length < 6){
                etPassword.error = "Password harus lebih daro 6 karakter"
                etPassword.requestFocus()
                return@setOnClickListener
            }
            
            LoginUser(email, password)
        }

        btnRegister.setOnClickListener {
            Intent(this@LoginActivity,RegisterActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun LoginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Intent(this@LoginActivity, MainActivity::class.java).also {intent ->
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    }
                } else {
                    Toast.makeText(this,"${it.exception?.message}",Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null){
            Intent(this@LoginActivity, MainActivity::class.java).also {intent ->
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

}