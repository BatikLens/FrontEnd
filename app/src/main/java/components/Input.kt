package components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun Input(label: String, inputType: InputType, onValidate: (Boolean) -> Unit) {
    var text by remember { mutableStateOf("") }
    var isValid by remember { mutableStateOf(true) }

    OutlinedTextField(
        value = text,
        onValueChange = { newText ->
            text = newText.replace("\n", "")
            isValid = when (inputType) {
                InputType.Email -> android.util.Patterns.EMAIL_ADDRESS.matcher(newText).matches()
                InputType.Password -> newText.length >= 6
            }
            onValidate(isValid)
            text = newText
        },
        label = { Text(label) },
        textStyle = TextStyle(color = Color.White),
        keyboardOptions = when (inputType) {
            InputType.Email -> KeyboardOptions(keyboardType = KeyboardType.Email)
            InputType.Password -> KeyboardOptions(keyboardType = KeyboardType.Password)
        },
        visualTransformation = if (inputType == InputType.Password) PasswordVisualTransformation() else VisualTransformation.None,
        isError = !isValid
    )
}

enum class InputType {
    Email,
    Password
}