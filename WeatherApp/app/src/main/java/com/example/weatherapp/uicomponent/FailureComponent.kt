package com.example.weatherapp.uicomponent

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.weatherapp.ui.theme.White

@Composable
fun Failure(txt:String){
    Text(
        text =txt,
        fontSize = 24.sp,
        color = White,
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(),
    )
}
