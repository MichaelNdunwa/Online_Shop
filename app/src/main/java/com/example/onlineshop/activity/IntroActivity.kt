package com.example.onlineshop.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.onlineshop.R

class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            IntroScreen(onClick = {
                startActivity(Intent(this@IntroActivity, MainActivity::class.java))
            })
        }
    }
}

@Composable
@Preview
fun IntroScreen(onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Intro image:
        Image(
            painter = painterResource(id = R.drawable.intro_logo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().padding(top = 48.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Title text:
        Text(
            text = stringResource(id = R.string.intro_title),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Subtitle text:
        Text(
            text = stringResource(id = R.string.subtitle),
            modifier = Modifier.padding(top = 16.dp),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.DarkGray,
            lineHeight = 24.sp
        )

        // Buttons:
        Button(
            onClick = { onClick() },
            modifier = Modifier
                .padding(horizontal = 32.dp, vertical = 16.dp)
                .fillMaxSize()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.purple)),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = stringResource(id = R.string.button_text),
                color = Color.White,
                fontSize = 18.sp
            )
        }

        // Sign text:
        Text(
            text = stringResource(id = R.string.sign_text),
            modifier = Modifier.padding(top = 16.dp),
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )
    }
}
