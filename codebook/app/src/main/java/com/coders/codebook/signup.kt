package com.coders.codebook

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
import org.json.JSONObject
import java.util.regex.Matcher
import java.util.regex.Pattern


class signup : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val bfacebook = findViewById<Button>(R.id.facebook_signup_button)
        val bgmail = findViewById<Button>(R.id.gmail_signup_button)

        val bregistrer = findViewById<Button>(R.id.email_signup_button)

        val editNickname = findViewById<EditText>(R.id.user_name_signup)
        val editname = findViewById<EditText>(R.id.full_name)
        val editPassword = findViewById<EditText>(R.id.password_signup)
        val editEmail = findViewById<EditText>(R.id.email_signup)


        bregistrer.setOnClickListener {
            if (Network.vNetwork(this)){
                val nickName = editNickname.getText().toString()
                val name = editname.getText().toString()
                val password = editPassword.getText().toString()
                val email = editEmail.getText().toString()

                if (nickName != "" && name != "" && password != "" && email != ""){
                    val intent = Intent(this, login::class.java)
                    if (isEmailValid(email)) {
                        if (isPasswordValid(password)) {
                            val params = HashMap<String,String>()
                            params["Name"] = name
                            params["NickName"] = nickName
                            params["Email"] = email
                            params["Password"] = password
                            val jsonObject = JSONObject(params)
                            Network.postHTTP(this, "http://35.231.202.82:81/register", jsonObject, Response.Listener<JSONObject>{
                                    response ->
                                try {
                                    //val respJson: String = response.get("result").toString()
                                    //Toast.makeText(this, respJson, Toast.LENGTH_LONG).show()
                                    if (response.get("result").toString() == "success"){
                                        Toast.makeText(this, "registered user", Toast.LENGTH_LONG).show()
                                        val params_post = LinkedHashMap<String,String>()
                                        params_post["ID"] = 8.toString()
                                        params_post["Who"] = response.get("data").toString()
                                        params_post["Challenge"] = 14.toString()
                                        val jsonUser = JSONObject(params_post)

                                        Network.getJson(this, "http://35.231.202.82:81/data", jsonUser, Response.Listener<JSONObject>{
                                                response_Json ->
                                            try {
                                                Log.d("send register", jsonUser.toString())
                                                Log.d("response register", response_Json.toString())
                                                (this as signup).finish()
                                                startActivity(intent)
                                            }catch (e: Exception){
                                                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                                            }
                                        })
                                    } else {
                                        Toast.makeText(this, response.get("data").toString(), Toast.LENGTH_LONG).show()
                                    }
                                }catch (e:Exception){
                                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                                }
                            })
                        } else {
                            Toast.makeText(this, "password no seguro\nDebe contener mas de 8 caracteres, al menos una mayuscula, minuscula, numero y caracter especial (ej: @, #, ?, &)", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(this, "email no valido", Toast.LENGTH_LONG).show()
                    }

                }
            } else {
                Toast.makeText(this, "Sin conexion", Toast.LENGTH_LONG).show()
            }
        }

        bfacebook.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Registro por FaceBook", Toast.LENGTH_SHORT).show()
        })

        bgmail.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "Registro por GMail", Toast.LENGTH_SHORT).show()
        })
    }

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPasswordValid(password: String): Boolean {
        val pattern: Pattern
        val matcher: Matcher

        val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"

        pattern = Pattern.compile(PASSWORD_PATTERN)
        matcher = pattern.matcher(password)

        return matcher.matches()
    }
}
