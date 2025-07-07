package com.misfigus.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.misfigus.R
import com.misfigus.ui.theme.Background

@Composable
fun PresentationScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        
        Text(
            text = "misFigus",
            fontSize = 50.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.regular),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 60.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 100.dp, bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.presentation_illustration),
                contentDescription = "Ilustración presentación",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Ubicá paquetes en los kioskos de tu zona",
                    color = colorResource(id = R.color.red),
                    fontSize = 26.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "¡También podés registrar el progreso de tu colección y canjear con otros usuarios!",
                    color = colorResource(id = R.color.regular),
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { navController.navigate("login") },
                    modifier = Modifier
                        .width(217.dp)
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.purple)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Iniciar sesión",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
                TextButton(onClick = { navController.navigate("register") }) {
                    Text("Registrarse", color = colorResource(id = R.color.regular), fontSize = 16.sp)
                }
            }
        }
    }
}
