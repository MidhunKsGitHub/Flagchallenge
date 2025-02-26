package com.midhun.flagchallenge

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.midhun.flagchallenge.model.ChallengeModel
import com.midhun.flagchallenge.screens.QuizScreen
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

        NavHost(navController, startDestination = "start_challenge"){
            composable("set_time"){
                SetTime(modifier,navController)
            }
            composable("timer_page"){
                TimerPage(modifier,navController)
            }
            composable("start_challenge"){
                StartChallenge(modifier,viewModel)
            }

        }
    }
}

