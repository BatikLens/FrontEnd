package com.example.batiklens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityOptionsCompat
import com.example.batiklens.ui.theme.BatikLensTheme
import components.Navbar
import components.PageColor

class ScanActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BatikLensTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ScanUi()
                }
            }
        }
    }

    @Composable
    fun ScanUi(modifier: Modifier = Modifier) = BoxWithConstraints(
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
                .offset(y = (-75).dp)
                .align(Alignment.Center)
                .fillMaxWidth(),
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
            Column(
                modifier
                    .offset(y = (-35).dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(Color.DarkGray)
                    .requiredWidth(325.dp)
                    .padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Intruksi Penggunaan",
                    style = TextStyle(color = Color.White),
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp
                )
                Spacer(modifier = modifier.requiredHeight(15.dp))
                Text(
                    text = "1. Klik tombol kamera\n\n" +
                            "2. Ambil foto gambar batik\n\n" +
                            "3. Tunggu beberapa saat\n\n" +
                            "4. Anda akan melihat nama dan tautan motif batik yang mirip dengan gambar.",
                    style = TextStyle(color = Color.White),
                    fontSize = 18.sp
                )
            }

            Button(
                onClick = {
                    val intent = Intent(this@ScanActivity, CameraActivity::class.java)
                    this@ScanActivity.startActivity(intent)
                },
                modifier.size(60.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(Color.Gray)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.camera),
                    contentDescription = "",
                    modifier = modifier.requiredSize(30.dp),
                    tint = Color.White
                )
            }
        }

        Box(
            modifier
                .fillMaxWidth()
                .wrapContentSize()
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

                Navbar(modifier.align(Alignment.End), PageColor.Scan)
            }
        }
    }

    @Preview(showBackground = true, widthDp = 360, heightDp = 800)
    @Composable
    private fun ScanUiPreview() = ScanUi()
}