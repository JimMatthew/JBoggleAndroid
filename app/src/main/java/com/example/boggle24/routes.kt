package com.example.boggle24

sealed class Routes(val route: String) {
    object Play : Routes("Play")
    object Signup : Routes("Signup")
}