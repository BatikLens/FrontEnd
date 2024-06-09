package com.example.batiklens

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import com.example.batiklens.ui.theme.BatikLensTheme
import components.Input
import components.InputType
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import lib.option.Option
import lib.option.none
import lib.reqwest.Method
import lib.reqwest.Reqwest

class RegisterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BatikLensTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegisterUi()
                }
            }
        }
    }

    @Composable
    private fun RegisterUi() = BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter))
        {
            Image(
                painter = painterResource(id = R.drawable.detail),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .requiredSize(220.dp)
                    .offset(y = (-30).dp)
                    .graphicsLayer(alpha = .5f)
            )
            Image(
                painter = painterResource(id = R.drawable.detail2),
                contentDescription = "",
                modifier = Modifier
                    .graphicsLayer(scaleY = -1f, alpha = .5f)
                    .align(Alignment.TopEnd)
                    .requiredSize(160.dp)
                    .offset(y = (16).dp)
            )
        }

        Column(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally)
        {
            Image(painter = painterResource(id = R.drawable.logo), contentDescription = "",
                modifier = Modifier
                    .size(175.dp)
                    .wrapContentSize())

            Column(modifier = Modifier.offset(y = (-30).dp), horizontalAlignment = Alignment.CenterHorizontally) {
                val email = remember { mutableStateOf("") }
                val password = remember { mutableStateOf("") }
                val confPassword = remember { mutableStateOf("") }

                Input(label = "Email", type = InputType.Email, bindValue = email)
                Spacer(Modifier.requiredHeight(16.dp))
                Input(label = "Password", type = InputType.Password, bindValue = password)
                Spacer(Modifier.requiredHeight(16.dp))
                Input(label = "Confirm Password", type = InputType.Password, bindValue = confPassword)
                Spacer(Modifier.requiredHeight(16.dp))

                Button(
                    onClick = {
                        lifecycleScope.launch {
                            val payload =
                                """
                                    {
                                        "email": "${email.value}",
                                        "password": "${password.value}",
                                        "confirmPassword": "${confPassword.value}"
                                    }
                                """.trimIndent()

                            val response = Reqwest("https://batiklens-svnfyuekra-et.a.run.app/register")
                                .method(Method.POST)
                                .headers(mapOf("content-type" to "application/json"))
                                .body(payload)
                                .send()
                                .await()
                                .unwrapOrElse {
                                    runOnUiThread { Toast.makeText(this@RegisterActivity, "Terjadi Kesalahan code: 1", Toast.LENGTH_SHORT).show() }
                                    return@launch
                                }
                                .parseContent<Register>(ignoreKey = true)
                                .unwrapOrElse {
                                    runOnUiThread { Toast.makeText(this@RegisterActivity, "Terjadi Kesalahan code: 2", Toast.LENGTH_SHORT).show() }
                                    return@launch
                                }

                            when (response.status) {
                                "success" -> runOnUiThread {
                                    val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                    val options = ActivityOptionsCompat.makeCustomAnimation(
                                        this@RegisterActivity,
                                        R.anim.fade_in,
                                        R.anim.fade_out
                                    )
                                    startActivity(intent, options.toBundle())
                                }
                                else -> runOnUiThread {
                                    Toast.makeText(this@RegisterActivity, response.message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(Color.Gray)
                ) {
                    Text(text = "Submit", style = TextStyle(Color.White))
                }

                Spacer(Modifier.height(10.dp))
                Row {
                    Text(text = "Sudah punya akun?", color = Color.White)
                    Spacer(Modifier.width(10.dp))
                    ClickableText(
                        text = AnnotatedString("Login"),
                        style = TextStyle(
                            color = Color.White,
                            fontWeight = FontWeight(1000),
                            textDecoration = TextDecoration.Underline
                        ),
                        onClick = {
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            val options = ActivityOptionsCompat.makeCustomAnimation(
                                this@RegisterActivity,
                                R.anim.fade_in,
                                R.anim.fade_out
                            )
                            startActivity(intent, options.toBundle())
                        }
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(115.dp)
                .align(Alignment.BottomCenter))
        {
            Image(
                painter = painterResource(id = R.drawable.detail),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .graphicsLayer(scaleY = -1f, scaleX = -1f, alpha = .5f)
                    .requiredSize(220.dp)
                    .offset(y = 23.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.detail2),
                contentDescription = "",
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .graphicsLayer(scaleX = -1f, alpha = .5f)
                    .requiredSize(160.dp)
                    .offset(y = (-5).dp)
            )
        }
    }

    @Preview(showBackground = true, widthDp = 360, heightDp = 800)
    @Composable
    private fun Preview() = RegisterUi()
}

@Serializable
private data class Register(
    val status: String,
    val message: String,
    val user: Option<RegisterUser> = none()
)

@Serializable
private data class RegisterUser(
    val id: String,
    val email: String,
)