package com.example.boggle24

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import android.provider.Settings
import android.view.WindowInsets
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import bogglegame.BoggleBoard
import bogglegame.BoggleStats
import bogglegame.FileHelper
import com.example.boggle24.ui.theme.Boggle24Theme
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : ComponentActivity() {

    private var islaunched = mutableStateOf(false)
    private val fileHelper = FileHelper(this)
    private val isRotated = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {

        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        val boggleStats = fileHelper.readStatFromFile("bog.dat")
        super.onCreate(savedInstanceState)

        val states = StateManager(boggleStats, isRotated.value,saveStats = {saveStats(it)})
        setContent {
            val configuration = LocalConfiguration.current

            Boggle24Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {

                    when (configuration.orientation) {
                        Configuration.ORIENTATION_LANDSCAPE -> {
                            isRotated.value = true
                            states.rotate(true)
                        } else -> {
                            isRotated.value = false
                            states.rotate(false)
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
