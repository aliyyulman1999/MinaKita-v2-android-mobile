package org.telkomuniversity.minakitav2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()

        btnSignUp.setOnClickListener {
            val email = etEmailRegister.text.toString().trim()
            val password = etPasswordRegister.text.toString().trim()

            if (email.isEmpty()){
                etEmailRegister.error = "Email harus diisi"
                etEmailRegister.requestFocus()
                return@setOnClickListener
            }
            
            //mengecek email valid atau tidaknya
            
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                etEmailRegister.error = "Email tidak valid"
                etEmailRegister.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty()||password.length < 6){
                etPasswordRegister.error = "Password harus lebih daro 6 karakter"
                etPasswordRegister.requestFocus()
                return@setOnClickListener
            }
            registerUser(email, password)
        }

        btnAlreadyAccount.setOnClickListener {
            Intent(this@RegisterActivity, LoginActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    // ketika tombol back di klik akun tidak keluar
                    Intent(this@RegisterActivity, MainActivity::class.java).also {
                        it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
                }else {
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null){
            Intent(this@RegisterActivity, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
    }
}