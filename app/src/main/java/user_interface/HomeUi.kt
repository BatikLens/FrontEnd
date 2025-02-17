package user_interface

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Menu
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.batiklens.R
import components.Navbar
import components.TypeForm

@Composable
fun HomeUi(modifier: Modifier = Modifier) = BoxWithConstraints(
    modifier
        .fillMaxSize()
        .background(Color.Black)
) {
    Box(
        modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter)
    ) {
        Image(
            painter = painterResource(id = R.drawable.detail5),
            contentDescription = "",
            modifier
                .graphicsLayer(alpha = .5f)
                .requiredSize(85.dp)
                .wrapContentSize()
                .graphicsLayer(scaleY = -1f, alpha = .7f)
        )
        Image(
            painter = painterResource(id = R.drawable.detail5),
            contentDescription = "",
            modifier
                .align(Alignment.TopEnd)
                .graphicsLayer(alpha = .5f)
                .requiredSize(85.dp)
                .wrapContentSize()
                .graphicsLayer(scaleY = -1f, scaleX = -1f, alpha = .7f)
        )
    }

    Column(
        modifier
            .fillMaxWidth()
            .align(Alignment.TopCenter)
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.detail3),
            contentDescription = "",
            modifier
                .graphicsLayer(alpha = .5f)
                .requiredSize(150.dp)
                .wrapContentSize()
        )
        Text(
            text = "Kenali Macam-Macam\nBatik Nusantara",
            style = TextStyle(
                color = Color.White
            ),
            textAlign = TextAlign.Center,
            modifier = modifier
                .offset(y = (-42).dp),
            fontSize = 25.sp
        )
    }

    Column(
        modifier
            .fillMaxWidth()
            .align(Alignment.Center)
            .zIndex(10f),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "",
            modifier
                .wrapContentSize()
                .requiredSize(225.dp)
        )

        Button(
            onClick = {  },
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(Color.Gray)
        ) {
            Text(
                text = "Sejarah Batik",
                style = TextStyle(Color.White)
            )
        }

        Spacer(modifier.requiredHeight(40.dp))

        Text(
            text = "Warisan Budaya\nUNESCO",
            style = TextStyle(
                color = Color.White
            ),
            textAlign = TextAlign.Center,
            modifier = modifier,
            fontSize = 25.sp
        )
    }

    Box(
        modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .align(Alignment.BottomCenter)
    ) {
        Column {
            Box(
                modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.detail4),
                    contentDescription = "",
                    modifier
                        .align(Alignment.TopStart)
                        .graphicsLayer(alpha = .5f)
                        .requiredSize(140.dp)
                        .offset(x = (-12).dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.detail4),
                    contentDescription = "",
                    modifier
                        .align(Alignment.TopEnd)
                        .graphicsLayer(scaleX = -1f, alpha = .5f)
                        .requiredSize(140.dp)
                        .offset(x = (-12).dp)
                )
            }

            Navbar(modifier.align(Alignment.End))
        }
    }
}