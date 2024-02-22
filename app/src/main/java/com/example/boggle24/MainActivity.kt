package com.example.boggle24

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
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

                    Column {
                        when (configuration.orientation) {
                            Configuration.ORIENTATION_LANDSCAPE -> {
                                isRotated.value = true
                                states.rotate()
                            }

                            // Other wise
                            else -> {
                                isRotated.value = false
                                states.rotate()
                            }
                        }
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
        }
    }

    private fun saveStats(stats: BoggleStats){
        fileHelper.writeStatToFile(stats, "bog.dat")
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
