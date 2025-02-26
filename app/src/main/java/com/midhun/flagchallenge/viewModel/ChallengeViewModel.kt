package com.midhun.flagchallenge.viewModel

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.midhun.flagchallenge.database.Database
import com.midhun.flagchallenge.model.ChallengeModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json



class ChallengeViewModel( context: Context): ViewModel() {

    private val Context.datastore by preferencesDataStore(name = "quiz_prefs")

    private val _data = MutableLiveData<ChallengeModel>()
    val data: MutableLiveData<ChallengeModel> = _data

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex:StateFlow<Int> = _currentQuestionIndex

    private val _score = MutableStateFlow(0)
    val score:StateFlow<Int> = _score

    private val _timeLeft = MutableStateFlow(30)
    val timeLeft:StateFlow<Int> = _timeLeft

    private val _selectedAnswer = MutableStateFlow<Int?>(null)
    val selectedAnswer:StateFlow<Int?> = _selectedAnswer

    private val _gameOver = MutableStateFlow(false)
    val gameOver:StateFlow<Boolean> = _gameOver

    private  val datastore = context.datastore

    init {


        viewModelScope.launch {
            loadQuestion()
            loadGameState()
        }
    }


    private suspend fun loadQuestion() {
        val quizData = Json.decodeFromString<ChallengeModel>(Database.QUESTIONS_JSON)
        _data.postValue(quizData)
    }

   suspend  fun startTimer(){
        viewModelScope.launch {
            for(i in 30 downTo 0){
                _timeLeft.value = i
                delay(1000L)
                if(i==0){
                    startTimer()
                    nextQuestion()
                }
            }
        }
    }

    fun selectedAnswer(answerId:Int){
        _selectedAnswer.value = answerId
    }
    fun scoreUpdate(){
        _score.value++
    }
    private fun nextQuestion() {
        if(_currentQuestionIndex.value< data.value?.questions?.size!!){
            _currentQuestionIndex.value++
            _selectedAnswer.value=null
            _timeLeft.value = 30
            viewModelScope.launch {
                saveGameState()
            }

        }
        else{
            _gameOver.value = true
        }
    }

     suspend fun loadGameState(){

        viewModelScope.launch {
            val preferences= datastore.data.first()
            _currentQuestionIndex.value = preferences[intPreferencesKey("questionIndex")]?:0
            _timeLeft.value = preferences[intPreferencesKey("timeLeft")]?:30
            _score.value = preferences[intPreferencesKey("score")]?:0
        }
    }

    suspend fun saveGameState(){
        viewModelScope.launch {
            datastore.edit {
                preferences->
           preferences[intPreferencesKey("questionIndex")] =_currentQuestionIndex.value
           preferences[intPreferencesKey("timeLeft")] =_timeLeft.value
           preferences[intPreferencesKey("score")] =_score.value
            }
        }
    }
    suspend fun clearGameState(){
        viewModelScope.launch {
            datastore.edit {
                    preferences->
                preferences[intPreferencesKey("questionIndex")] = 0
                preferences[intPreferencesKey("timeLeft")] = 30
                preferences[intPreferencesKey("score")] = 0
            }
        }
    }

}