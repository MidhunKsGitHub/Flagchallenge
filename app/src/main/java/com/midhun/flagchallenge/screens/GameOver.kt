package com.midhun.flagchallenge.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.midhun.flagchallenge.ui.theme.primaryGrey
import com.midhun.flagchallenge.ui.theme.primaryLightGrey
import com.midhun.flagchallenge.ui.theme.primaryOrange
import kotlinx.coroutines.delay

@Composable
fun GameOver(modifier: Modifier = Modifier, navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("GAME", Context.MODE_PRIVATE)

    LaunchedEffect(Unit) {

        sharedPreferences.edit().putString("isStart","false").apply()
        delay(3000L)
        navController.navigate("score_page")
    }
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(primaryOrange)
        ) {

        }
        Spacer(Modifier.height(10.dp))

        Card(
            modifier = modifier
                .padding(5.dp)
                .border(1.dp, primaryGrey),
            RoundedCornerShape(8.dp),

            colors = CardColors(
                containerColor = primaryLightGrey,
                contentColor = Color.Black,
                disabledContainerColor = Color.White,
                disabledContentColor = Color.White
            ),
        ) {


            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
            ) {

                Box(
                    modifier = Modifier
                        .background(color = Color.Black)
                        .width(80.dp)
                        .height(40.dp),
                    contentAlignment = Alignment.Center

                ) {

                    Text(
                        "OO:00",
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(5.dp))
                Text(
                    "FLAGS CHALLENGE",
                    modifier = Modifier.fillMaxWidth()
                        .padding(10.dp),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = primaryOrange
                    )
                )

            }

            Column(  modifier = Modifier.fillMaxWidth().height(200.dp).padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {

                Text("GAME OVER", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)

            }
        }
    }
}