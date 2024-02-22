package com.example.boggle24

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import bogglegame.BoggleBoard
import bogglegame.BoggleStats
import bogglegame.FileHelper
import com.example.boggle24.ui.theme.Boggle24Theme
import com.example.boggle24.ui.theme.Header
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {

    private var islaunched = mutableStateOf(false)
    private val fileHelper = FileHelper(this)
    private val isRotated = mutableStateOf(false)


    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {

        val boggleStats = fileHelper.readStatFromFile("bog.dat")
        super.onCreate(savedInstanceState)

        val states = StateManager(boggleStats, isRotated.value, saveStats = {saveStats(it)})
        setContent {
            val configuration = LocalConfiguration.current

            val windowSizeClass = calculateWindowSizeClass(this)
            Boggle24Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    //color = MaterialTheme.colorScheme.background
                    color = Color.White
                ) {

                    when (configuration.orientation) {
                        Configuration.ORIENTATION_LANDSCAPE -> {
                            isRotated.value = true
                            states.rotate()
                        } else -> {
                            isRotated.value = false
                            states.rotate()
                        }
                    }
                    Column {
                        if (!islaunched.value) {
                            Launcher(
                                stats = boggleStats,
                                startGame = { islaunched.value = true },
                                isRotated = isRotated.value
                            )
                        } else {
                            states.stateManager()
                        }

                    }
                }

            }
            hideSystemUI()
        }
    }

    private fun saveStats(stats: BoggleStats){
        fileHelper.writeStatToFile(stats, "bog.dat")
    }

    private fun hideSystemUI() {
        actionBar?.hide()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.insetsController?.apply {
            hide(WindowInsets.Type.statusBars())
            systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }


}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    //val board = BoggleBoard(MainActivity(),null, null)
    Boggle24Theme {
        //Greeting(board, "Android")
    }
}

@Composable
fun Greeting(board: BoggleBoard, s: String) {

}
