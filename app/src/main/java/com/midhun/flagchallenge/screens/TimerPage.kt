package com.midhun.flagchallenge.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.midhun.flagchallenge.ui.theme.primaryLightGrey
import com.midhun.flagchallenge.ui.theme.primaryOrange

@SuppressLint("DefaultLocale")
@Composable
fun TimerPage (modifier: Modifier = Modifier,navController: NavController) {
    val context = LocalContext.current

    val sharedPreferences = context.getSharedPreferences("MyTime", Context.MODE_PRIVATE)

    val savedTime by remember {
        mutableStateOf(sharedPreferences.getString("saved_time","00:00:00")?:"00:00:00")
    }

  val totalSeconds = savedTime.split(":").let {
      val hours = it[0].toInt()*3600
      val minutes = it[1].toInt()*60
      val seconds = it[2].toInt()
      hours+minutes+seconds
  }
var countDown by remember {
    mutableIntStateOf(totalSeconds)
}
    LaunchedEffect(Unit) {
        while(countDown > 0){
          kotlinx.coroutines.delay(1000L)
            countDown -= 1
        }
        if (countDown == 0){
          navController.navigate("start_challenge"){
            popUpTo("timer_page"){
                inclusive = true
            }
          }
        }
    }

    val hours =countDown/3600
    val minutes = (countDown%3600)/60
    val seconds = countDown%60
    val formattedTime = String.format("%02d:%02d:%02d",hours,minutes,seconds)

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
            modifier = modifier.padding(5.dp),
            colors = CardColors(
                containerColor = primaryLightGrey,
                contentColor = Color.Black,
                disabledContainerColor = Color.White,
                disabledContentColor = Color.White
            )
        ) {
            Spacer(Modifier.height(5.dp))

            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()) {
                Text("FLAGS CHALLENGE", textAlign = TextAlign.Center, style = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Normal, color = primaryOrange))
            }

            Spacer(Modifier.height(50.dp))
           Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
               Text("CHALLENGE", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
               Text("WILL START IN", fontSize = 20.sp, fontWeight = FontWeight.Bold)
               Spacer(Modifier.height(10.dp))
               Text(formattedTime, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Gray)}

            Spacer(Modifier.height(25.dp))
        }
    }
}