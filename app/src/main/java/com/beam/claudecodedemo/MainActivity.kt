package com.beam.claudecodedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.beam.claudecodedemo.ui.navigation.TodoNavGraph
import com.beam.claudecodedemo.ui.theme.ClaudeCodeDemoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClaudeCodeDemoTheme {
                TodoNavGraph()
            }
        }
    }
}
