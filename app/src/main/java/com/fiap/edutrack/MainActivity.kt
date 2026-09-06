package com.fiap.edutrack

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.fiap.edutrack.navigation.EduTrackNavHost
import com.fiap.edutrack.ui.theme.EduTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EduTrackTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    EduTrackNavHost()
                }
            }
        }
    }
}
