package components

import android.util.Patterns
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun Input(label: String, type: InputType, bindValue: MutableState<String>) {
    var valid by remember { mutableStateOf(true) }

    OutlinedTextField(
        modifier = Modifier.requiredWidth(300.dp),
        value = bindValue.value,
        onValueChange = { newText ->
            bindValue.value = newText.replace("\n", "")
            valid = when (type) {
                InputType.Email -> Patterns.EMAIL_ADDRESS.matcher(newText).matches()
                InputType.Password -> true
            }
            bindValue.value = newText
        },
        label = { Text(label) },
        textStyle = TextStyle(color = Color.White),
        keyboardOptions = when (type) {
            InputType.Email -> KeyboardOptions(keyboardType = KeyboardType.Email)
            InputType.Password -> KeyboardOptions(keyboardType = KeyboardType.Password)
        },
        visualTransformation = if (type == InputType.Password) PasswordVisualTransformation() else VisualTransformation.None,
        isError = !valid,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = if (valid) Color(0xFF858585) else Color.Red,
            unfocusedBorderColor = if (valid) Color(0xFF858585) else Color.Red,
            focusedLabelColor = if (valid) Color(0xFF858585) else Color.Red,
            cursorColor = if (valid) Color(0xFF858585) else Color.Red,

            ),
        singleLine = true,
    )
}

enum class InputType {
    Email,
    Password
}