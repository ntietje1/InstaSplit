package com.hypeapps.instasplit.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun SplashScreen(goToLogin: () -> Unit = {}) {
    Surface(color = MaterialTheme.colorScheme.primary) {
        Column(
            modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1.25f))
            Text("InstaSplit", style = MaterialTheme.typography.displayLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
//            Spacer(modifier = Modifier.weight(0.3f))
//            Icon(imageVector = Icons.Default.InsertEmoticon, contentDescription = "Logo", modifier = Modifier.size(150.dp))
            Spacer(modifier = Modifier.weight(1.0f))
            Surface(
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .padding(bottom = 50.dp)
                    .fillMaxWidth()
                    .aspectRatio(1.05f), shape = MaterialTheme.shapes.extraLarge, shadowElevation = 4.dp,
            ) {
                BoxWithConstraints {
                    Text(
                        "Manage your Bills with InstaSplit",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(32.dp)
                    )
                    Text(
                        "Easily manage your debt, simplify shared expenses, and enjoy stress-free group financials!",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp)
                    )
//                    Surface(
//                        modifier = Modifier
//                            .size(maxWidth / 3.4f)
//                            .aspectRatio(0.5f)
//                            .align(Alignment.BottomCenter)
//                            .offset(y = maxHeight / 7.0f),
//                        color = MaterialTheme.colorScheme.primary,
//                        shape = RoundedCornerShape(1000.dp),
//                    ) {
//
//                    }
                    Icon(imageVector = Icons.Default.ArrowCircleRight,
                        contentDescription = "Proceed",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier
                            .size(maxWidth / 3.5f)
                            .align(Alignment.BottomCenter)
                            .clickable { goToLogin() })
                }
            }
        }
    }
}