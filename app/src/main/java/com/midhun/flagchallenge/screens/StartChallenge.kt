package com.midhun.flagchallenge.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.midhun.flagchallenge.model.QuestionModel
import com.midhun.flagchallenge.ui.theme.primaryGreen
import com.midhun.flagchallenge.ui.theme.primaryGrey
import com.midhun.flagchallenge.ui.theme.primaryLightGrey
import com.midhun.flagchallenge.ui.theme.primaryOrange
import com.midhun.flagchallenge.ui.theme.primaryRed
import com.midhun.flagchallenge.viewModel.ChallengeViewModel
import kotlinx.coroutines.launch

@SuppressLint("DefaultLocale")
@Composable
fun StartChallenge(modifier: Modifier = Modifier,viewModel: ChallengeViewModel,navController: NavController) {

    val context = LocalContext.current

    val data = viewModel.data.observeAsState()

    val listState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()


    val questionIndex by viewModel.currentQuestionIndex.collectAsState()
    val timeLeft by viewModel.timeLeft.collectAsState()
    val selectedAnswer by viewModel.selectedAnswer.collectAsState()
    val gameOver by viewModel.gameOver.collectAsState()

    val sharedPreferences = context.getSharedPreferences("GAME", Context.MODE_PRIVATE)



    LaunchedEffect(Unit) {
        sharedPreferences.edit().putString("isStart","true").apply()
        viewModel.startTimer()
    }

    LaunchedEffect(questionIndex) {

        coroutineScope.launch {
                listState.animateScrollToItem(questionIndex)
           }


       if(questionIndex == data.value?.questions?.size){
           navController.navigate("game_over")
       }

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
                    val formattedTime = String.format("%02d",timeLeft)

                    Text(
                        "OO:$formattedTime",
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

            Row(modifier = Modifier.fillMaxWidth()) {

                LazyRow(
                    state = listState,
                    userScrollEnabled = false
                ) {
                    data.value?.questions?.size?.let {
                        items(it){ index->

                            QuestionScreen(context,data.value?.questions!!,index,timeLeft,viewModel)
                        }
                    }
                }

            }

        }
    }
}


@Composable
fun BoxWithText(
    name: String,
    isCorrect: Boolean,
    isSelected: Boolean,
    timeLeft: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.padding(5.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(6.dp),
            colors = ButtonColors(
                containerColor = when {
                    timeLeft == 0 && isCorrect -> primaryGreen
                    timeLeft == 0 && isSelected -> primaryRed
                    isSelected -> primaryOrange
                    else -> Color.White
                },
                contentColor = if (isSelected) Color.White else Color.Black,
                disabledContainerColor = Color.Gray,
                disabledContentColor = Color.Gray
            ),
            contentPadding = PaddingValues(0.dp),
            modifier = Modifier
                .widthIn(min = 100.dp)
                .height(28.dp)
        ) {
            Text(
                name,
                fontSize = 10.sp,
                fontWeight = FontWeight.W500,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (timeLeft == 0) {
            Text(
                if (isCorrect) "CORRECT" else "WRONG",
                fontSize = 8.sp,
                fontWeight = FontWeight.W500,
                color = if (isCorrect) primaryGreen else primaryRed
            )
        }
    }
}

@Composable
fun QuestionScreen(context: Context,flags:ArrayList<QuestionModel>, index: Int, timeLeft: Int,viewModel: ChallengeViewModel) {


    val countryCode = flags[index].country_code
    val answerId = flags[index].answer_id.toString()

    val option0 = flags[index].countries?.get(0)?.country_name.toString()
    val option1 = flags[index].countries?.get(1)?.country_name.toString()
    val option2 = flags[index].countries?.get(2)?.country_name.toString()
    val option3 = flags[index].countries?.get(3)?.country_name.toString()

    val id0 = flags[index].countries?.get(0)?.id.toString()
    val id1 = flags[index].countries?.get(1)?.id.toString()
    val id2 = flags[index].countries?.get(2)?.id.toString()
    val id3 = flags[index].countries?.get(3)?.id.toString()

    var selectedId by remember { mutableStateOf<Int?>(null) }


    Column(modifier = Modifier.padding(10.dp)) {


        Row(modifier = Modifier.fillMaxWidth().padding(10.dp)) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(360.dp))
                    .background(primaryOrange)
                    .width(25.dp)
                    .height(25.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    index.toString(), color = Color.White
                )
            }
            Spacer(modifier = Modifier.width(25.dp))
            Text(
                "GUESS THE COUNTRY FROM THE FLAG ?",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )

        }
        Spacer(modifier = Modifier.height(25.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Spacer(modifier = Modifier.width(15.dp))
            Card {

                Box(modifier = Modifier.padding(5.dp)) {
                    AsyncImage(
                        modifier = Modifier
                            .width(100.dp)
                            .height(60.dp),
                        model = "https://flagcdn.com/w320/${countryCode?.lowercase()}.png",
                        contentDescription = "image",
                    )

                }
            }

            Spacer(modifier = Modifier.width(25.dp))
            Column {
                Row {
                    BoxWithText(
                        name = option0,
                        isCorrect = answerId == id0,
                        isSelected = selectedId == id0.toInt(),
                        timeLeft = timeLeft
                    ) { selectedId = id0.toInt()
                    if(answerId == id0){
                        viewModel.scoreUpdate()
                    }
                    }

                    BoxWithText(
                        name = option1,
                        isCorrect = answerId == id1,
                        isSelected = selectedId == id1.toInt(),
                        timeLeft = timeLeft
                    ) { selectedId = id1.toInt()
                        if(answerId == id1){

                            viewModel.scoreUpdate()
                        }}
                }

                Row {
                    BoxWithText(
                        name = option2,
                        isCorrect = answerId == id2,
                        isSelected = selectedId == id2.toInt(),
                        timeLeft = timeLeft
                    ) { selectedId = id2.toInt()
                        if(answerId == id2){
                            viewModel.scoreUpdate()
                        }
                    }

                    BoxWithText(
                        name = option3,
                        isCorrect = answerId == id3,
                        isSelected = selectedId == id3.toInt(),
                        timeLeft = timeLeft
                    ) { selectedId = id3.toInt()
                        if(answerId == id3){

                            viewModel.scoreUpdate()
                        }
                    }
                }
            }
        }
    }
}

