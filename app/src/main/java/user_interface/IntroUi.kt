package user_interface

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.batiklens.R
import kotlinx.coroutines.delay

@Composable
fun IntroUi(modifier: Modifier = Modifier, onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000)
        onTimeout()
    }

    BoxWithConstraints(
        modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Box(
            modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter))
        {
            Image(
                painter = painterResource(id = R.drawable.detail),
                contentDescription = "",
                modifier
                    .align(Alignment.TopStart)
                    .requiredSize(220.dp)
                    .offset(y = (-30).dp)
                    .graphicsLayer(alpha = .5f)
            )
            Image(
                painter = painterResource(id = R.drawable.detail2),
                contentDescription = "",
                modifier
                    .graphicsLayer(scaleY = -1f, alpha = .5f)
                    .align(Alignment.TopEnd)
                    .requiredSize(160.dp)
                    .offset(y = (16).dp)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "",
            modifier
                .align(Alignment.Center)
                .requiredSize(275.dp)
        )

        Box(
            modifier
                .fillMaxWidth()
                .requiredHeight(115.dp)
                .align(Alignment.BottomCenter))
        {
            Image(
                painter = painterResource(id = R.drawable.detail),
                contentDescription = "",
                modifier
                    .align(Alignment.BottomEnd)
                    .graphicsLayer(scaleY = -1f, scaleX = -1f, alpha = .5f)
                    .requiredSize(220.dp)
                    .offset(y = 23.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.detail2),
                contentDescription = "",
                modifier
                    .align(Alignment.BottomStart)
                    .graphicsLayer(scaleX = -1f, alpha = .5f)
                    .requiredSize(160.dp)
                    .offset(y = (-5).dp)
            )
        }
    }
}