package com.example.slidepuzzlegame

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

val Context.dataStore by preferencesDataStore("puzzle_data")
val BEST_SCORE = intPreferencesKey("best_score")

fun saveBestScore(context: Context, moves: Int) {
    runBlocking {
        context.dataStore.edit { prefs ->
            val current = prefs[BEST_SCORE] ?: Int.MAX_VALUE
            if (moves < current) {
                prefs[BEST_SCORE] = moves
            }
        }
    }
}

fun loadBestScore(context: Context): Int {
    return runBlocking {
        val prefs = context.dataStore.data.first()
        prefs[BEST_SCORE] ?: Int.MAX_VALUE
    }
}
