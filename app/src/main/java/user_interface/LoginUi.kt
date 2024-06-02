package user_interface

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.app.ActivityOptionsCompat
import com.example.batiklens.HomeActivity
import com.example.batiklens.LoginActivity
import com.example.batiklens.R
import com.example.batiklens.RegisterActivity
import components.Form
import components.TypeForm

@Composable
fun LoginUi(modifier: Modifier = Modifier, typeForm: TypeForm = TypeForm.Login) {
    val context = LocalContext.current

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

        Column(
            modifier
                .wrapContentSize()
                .align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally)
        {
            Image(painter = painterResource(id = R.drawable.logo), contentDescription = "")
            Spacer(modifier.requiredHeight(10.dp))
            Form(
                modifier.zIndex(10f),
                typeForm,
                textClick = {
                    when (typeForm) {
                        TypeForm.Register -> {
                            val intent = Intent(context, LoginActivity::class.java)
                            val options = ActivityOptionsCompat.makeCustomAnimation(
                                context,
                                R.anim.fade_in,
                                R.anim.fade_out
                            )
                            context.startActivity(intent, options.toBundle())
                        }
                        TypeForm.Login -> {
                            val intent = Intent(context, RegisterActivity::class.java)
                            val options = ActivityOptionsCompat.makeCustomAnimation(
                                context,
                                R.anim.fade_in,
                                R.anim.fade_out
                            )
                            context.startActivity(intent, options.toBundle())
                        }
                    }
                },
                buttonClick = {
                    when (typeForm) {
                        TypeForm.Register -> {
                            val intent = Intent(context, HomeActivity::class.java)
                            val options = ActivityOptionsCompat.makeCustomAnimation(
                                context,
                                R.anim.fade_in,
                                R.anim.fade_out
                            )
                            context.startActivity(intent, options.toBundle())
                        }
                        TypeForm.Login -> {
                            val intent = Intent(context, HomeActivity::class.java)
                            val options = ActivityOptionsCompat.makeCustomAnimation(
                                context,
                                R.anim.fade_in,
                                R.anim.fade_out
                            )
                            context.startActivity(intent, options.toBundle())
                        }
                    }
                }
            )
        }

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
