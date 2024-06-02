package components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun Form(modifier: Modifier = Modifier, typeForm: TypeForm, textClick: (Int) -> Unit, buttonClick: () -> Unit) {
    Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Input(label = "Email", inputType = InputType.Email) {}
        Spacer(modifier.height(10.dp))
        Input(label = "Password", inputType = InputType.Password) {}
        Spacer(modifier.height(10.dp))

        when (typeForm) {
            TypeForm.Login -> Unit
            TypeForm.Register -> {
                Input(label = "Confirm Password", inputType = InputType.Password) {}
                Spacer(modifier.height(10.dp))
            }
        }

        Button(
            onClick = buttonClick,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(Color.Gray)
        ) {
            Text(
                text = when (typeForm) {
                    TypeForm.Login -> "Login"
                    TypeForm.Register -> "Register"
                },
                style = TextStyle(Color.White)
            )
        }

        Spacer(modifier.height(10.dp))
        Row {
            Text(text = when (typeForm) {
                TypeForm.Login -> "Belum punya akun?"
                TypeForm.Register -> "Sudah punya akun?"
            },
                color = Color.White
            )
            Spacer(modifier.width(10.dp))
            ClickableText(
                text = AnnotatedString(when (typeForm) {
                    TypeForm.Login -> "Register"
                    TypeForm.Register -> "Login"
                }),
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight(1000),
                    textDecoration = TextDecoration.Underline
                ),
                onClick = textClick
            )
        }
    }
}

enum class TypeForm {
    Login,
    Register
}