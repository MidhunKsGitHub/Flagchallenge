package com.midhun.flagchallenge.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import coil.compose.rememberAsyncImagePainter
import com.midhun.flagchallenge.viewModel.ChallengeViewModel

@Composable
fun QuizScreen(viewModel: ChallengeViewModel) {
    val questionIndex by viewModel.currentQuestionIndex.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()
    val selectedAnswer by viewModel.selectedAnswer.collectAsState()
    val gameOver by viewModel.gameOver.collectAsState()

    if (gameOver) {
        Text("GAME OVER", fontSize = 30.sp, fontWeight = FontWeight.Bold)
        return
    }

    val question = viewModel.data.value?.questions?.get(questionIndex)


    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Time Left: $timeLeft", fontSize = 20.sp, color = Color.Red)
        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = rememberAsyncImagePainter("https://flagcdn.com/w320/${question?.country_code?.lowercase()}.png"),
            contentDescription = "Country Flag",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text("Guess the Country by the Flag", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        question?.countries?.forEach { country ->
            Button(
                onClick = {
                    viewModel.selectedAnswer(country.id)
                          },

                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedAnswer == country.id) Color.Gray else Color.DarkGray
                ),
                modifier = Modifier.fillMaxWidth().padding(8.dp)
            ) {
                Text(country.country_name.toString(), fontSize = 18.sp)
            }
        }
    }
}