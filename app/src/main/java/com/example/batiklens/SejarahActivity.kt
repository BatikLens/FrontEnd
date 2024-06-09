package com.example.batiklens

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.batiklens.ui.theme.BatikLensTheme
import components.Navbar

class SejarahActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BatikLensTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                }
            }
        }
    }

    @Composable
    fun SejarahUi(modifier: Modifier = Modifier) = BoxWithConstraints(
        modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column {
            Spacer(modifier.fillMaxWidth().requiredHeight(15.dp))
            Column(
                modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color.DarkGray)
                        .requiredWidth(325.dp)
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sejarah),
                        contentDescription = "",
                        modifier
                            .offset(y = (-60).dp)
                            .requiredSize(300.dp)
                    )

                    Text(
                        text = """
                        Sejarah batik di Indonesia, erat kaitannya dengan  perkembangan dari Kerajaan Majapahit pada era penyebaran ajaran Islam di  Pulau Jawa. Menurut beberapa catatan, pengembangan dari batik banyak  dilakukan pada zaman Kesultanan Mataram, kemudian berlanjut pada zaman  Kasunan Surakarta serta Kesultanan Yogyakarta.
    
                        Keberadaan dari kegiatan batik tertua di Indonesia diketahui berasal  dari Ponorogo dan bernama Wengker, sebelum akhirnya pada abad ketujuh,  Kerajaan di Jawa Tengah belajari batik dari Ponorogo.
                        """.trimIndent(),
                        style = TextStyle(color = Color.White),
                        modifier = modifier.offset(y = (-100).dp)
                    )
                }
                Spacer(modifier.fillMaxWidth().requiredHeight(15.dp))
                Column(
                    modifier
                        .clip(RoundedCornerShape(15.dp))
                        .background(Color.DarkGray)
                        .requiredWidth(325.dp)
                        .padding(15.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.sejarah),
                        contentDescription = "",
                        modifier
                            .offset(y = (-60).dp)
                            .requiredSize(300.dp)
                    )

                    Text(
                        text = """
                        Sejarah batik di Indonesia, erat kaitannya dengan  perkembangan dari Kerajaan Majapahit pada era penyebaran ajaran Islam di  Pulau Jawa. Menurut beberapa catatan, pengembangan dari batik banyak  dilakukan pada zaman Kesultanan Mataram, kemudian berlanjut pada zaman  Kasunan Surakarta serta Kesultanan Yogyakarta.
    
                        Keberadaan dari kegiatan batik tertua di Indonesia diketahui berasal  dari Ponorogo dan bernama Wengker, sebelum akhirnya pada abad ketujuh,  Kerajaan di Jawa Tengah belajari batik dari Ponorogo.
                        """.trimIndent(),
                        style = TextStyle(color = Color.White),
                        modifier = modifier.offset(y = (-100).dp)
                    )
                }
            }
        }

        Box(
            modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
        ) {
            Navbar(modifier.background(Color.Black))
        }
    }

    @Preview(showBackground = true, widthDp = 360, heightDp = 800)
    @Composable
    private fun SejarahPreview() = SejarahUi()
}