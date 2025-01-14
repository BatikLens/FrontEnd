package components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.batiklens.R

@Composable
fun Navbar(modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = Color(0xFF2B2B2B),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )
            },
        horizontalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = {  },
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            shape = CircleShape,
            modifier = modifier.size(60.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.wrapContentSize()
            ) {
                Icon(
                    Icons.Rounded.Home,
                    contentDescription = "",
                    modifier = modifier.requiredSize(25.dp),
                    tint = Color.White
                )
                Text(
                    text = "Home",
                    style = TextStyle(color = Color.White),
                    textAlign = TextAlign.Center,
                    modifier = modifier.requiredWidth(75.dp)
                )
            }
        }

        Spacer(modifier.requiredWidth(30.dp))

        Button(
            onClick = {  },
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            shape = CircleShape,
            modifier = modifier.size(60.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.wrapContentSize()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "",
                    modifier = modifier.requiredSize(25.dp),
                    tint = Color.White
                )
                Text(
                    text = "Scan",
                    style = TextStyle(color = Color.White),
                    textAlign = TextAlign.Center,
                    modifier = modifier.requiredWidth(75.dp)
                )
            }
        }

        Spacer(modifier.requiredWidth(30.dp))

        Button(
            onClick = {  },
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            shape = CircleShape,
            modifier = modifier.size(60.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.wrapContentSize()
            ) {
                Icon(
                    Icons.Rounded.List,
                    contentDescription = "",
                    modifier = modifier.requiredSize(25.dp),
                    tint = Color.White
                )
                Text(
                    text = "Ragam",
                    style = TextStyle(color = Color.White),
                    textAlign = TextAlign.Center,
                    modifier = modifier.requiredWidth(75.dp)
                )
            }
        }

        Spacer(modifier.requiredWidth(30.dp))

        Button(
            onClick = {  },
            colors = ButtonDefaults.buttonColors(Color.Transparent),
            shape = CircleShape,
            modifier = modifier.size(60.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.wrapContentSize()
            ) {
                Icon(
                    Icons.Rounded.Settings,
                    contentDescription = "",
                    modifier = modifier.requiredSize(25.dp),
                    tint = Color.White
                )
                Text(
                    text = "Settings",
                    style = TextStyle(color = Color.White),
                    textAlign = TextAlign.Center,
                    modifier = modifier.requiredWidth(75.dp)
                )
            }
        }
    }
}