package com.midhun.flagchallenge.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.midhun.flagchallenge.ui.theme.primaryGrey
import com.midhun.flagchallenge.ui.theme.primaryLightGrey
import com.midhun.flagchallenge.ui.theme.primaryOrange


@SuppressLint("DefaultLocale")
@Composable
fun SetTime(modifier: Modifier = Modifier,navController:NavController) {

    val context = LocalContext.current

    var hours by remember {
        mutableStateOf("")
    }


    var minutes by remember {
        mutableStateOf("")
    }

    var seconds by remember {
        mutableStateOf("")
    }


    var hours1 by remember {
        mutableStateOf("")
    }


    var minutes1 by remember {
        mutableStateOf("")
    }

    var seconds1 by remember {
        mutableStateOf("")
    }

    val sharedPreferences = context.getSharedPreferences("MyTime", Context.MODE_PRIVATE)

    var savedTime by remember {
        mutableStateOf(sharedPreferences.getString("saved_time","00:00:00")?:"00:00:00")
    }


    Column(modifier = Modifier,
    ) {
        Column(modifier= Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(primaryOrange)) {
        }

        Spacer(Modifier.height(5.dp))

        Card(
            modifier=modifier.padding(5.dp),
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

            Spacer(Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()) {
                Text("SCHEDULE", textAlign = TextAlign.Center, style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
            }
            Spacer(Modifier.height(15.dp))
            Row(modifier = Modifier, horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {

                //hours
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Hours")

                    Row {
                        TimeBox(hours, onValueChange = {
                            hours = it
                        })

                        TimeBox(hours1, onValueChange = {
                            hours1 = it
                        })
                    }
                }

                Spacer(Modifier.width(12.dp))

                //minutes

                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text("Minutes")

                    Row {
                        TimeBox(minutes, onValueChange = {
                            minutes = it
                        })

                        TimeBox(minutes1, onValueChange = {
                            minutes1 = it
                        })
                    }
                }

                Spacer(Modifier.width(12.dp))

                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text("Seconds", textAlign = TextAlign.Center)

                    Row {
                        TimeBox(seconds, onValueChange = {
                            seconds = it
                        })

                        TimeBox(seconds1, onValueChange = {
                            seconds1 = it
                        })
                    }
                }
            }
            Spacer(Modifier.height(15.dp))

            Button(
                onClick = {

                    val hrs = "${hours.ifEmpty { "0" }}${hours1.ifEmpty { "0" }}".toInt()
                    val min = "${minutes.ifEmpty { "0" }}${minutes1.ifEmpty { "0" }}".toInt()
                    val sec = "${seconds.ifEmpty { "0" }}${seconds1.ifEmpty { "0" }}".toInt()

                    if(hrs in 0..23 && min in 0..59 && sec in 0..59){
                        val formattedTime = String.format("%02d:%02d:%02d",hrs,min,sec)
                        sharedPreferences.edit().putString("saved_time",formattedTime).apply()
                        savedTime = formattedTime

                        navController.navigate("timer_page")
                    }

                }, modifier = Modifier
                    .width(200.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .align(Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(containerColor = primaryOrange)
            )
            {
                Text("Save", style = TextStyle(fontSize = 18.sp))
            }
            Spacer(Modifier.height(15.dp))
        }
    }
}


@Composable
fun TimeBox(value: String, onValueChange: (String) -> Unit) {


    TextField(
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        value = value,
        onValueChange = {
            if (it.length <= 1) {
                onValueChange(it)
            }
        },

        modifier = Modifier
            .padding(5.dp)
            .width(50.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(8.dp)),
        textStyle = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = primaryGrey,
            unfocusedContainerColor = primaryGrey,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )


    )
}

