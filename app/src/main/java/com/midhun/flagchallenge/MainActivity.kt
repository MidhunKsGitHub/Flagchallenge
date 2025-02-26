package com.midhun.flagchallenge


import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.midhun.flagchallenge.screens.GameOver
import com.midhun.flagchallenge.screens.ScorePage
import com.midhun.flagchallenge.screens.SetTime
import com.midhun.flagchallenge.screens.StartChallenge
import com.midhun.flagchallenge.screens.TimerPage
import com.midhun.flagchallenge.ui.theme.FlagchallengeTheme
import com.midhun.flagchallenge.viewModel.ChallengeViewModel
import com.midhun.flagchallenge.viewModel.ChallengeViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel:ChallengeViewModel = viewModel(factory = ChallengeViewModelFactory(applicationContext))

            FlagchallengeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Navigation(modifier = Modifier.padding(innerPadding),viewModel)
                }
            }
        }

    }

    @Composable
    fun Navigation(modifier: Modifier,viewModel: ChallengeViewModel){
        val navController = rememberNavController()

        val context = LocalContext.current

        val sharedPreferences = context.getSharedPreferences("GAME", Context.MODE_PRIVATE)

        val isStart by remember {
            mutableStateOf(sharedPreferences.getString("isStart","")?:"false")
        }

        var destination by remember {
            mutableStateOf("")
        }

        destination = if(isStart == "true"){
            "start_challenge"
        } else{
            "set_time"

        }


        NavHost(navController, startDestination = destination){
            composable("set_time"){
                SetTime(modifier,navController)
            }
            composable("timer_page"){
                TimerPage(modifier,navController)
            }
            composable("start_challenge"){
                StartChallenge(modifier,viewModel,navController)
            }
            composable("game_over"){
                GameOver(modifier, navController)
            }

            composable("score_page"){
                ScorePage(modifier,navController,viewModel)
            }

        }
    }
}

