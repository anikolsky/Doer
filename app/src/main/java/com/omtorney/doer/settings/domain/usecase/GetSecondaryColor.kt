package com.omtorney.doer.settings.domain.usecase

import com.omtorney.doer.core.domain.Repository
import kotlinx.coroutines.flow.Flow

class GetSecondaryColor(private val repository: Repository) {

    operator fun invoke(): Flow<Long> {
        return repository.getSecondaryColor
    }

//    operator fun invoke(): Flow<Color> {
//        return repository.getSecondaryColor.map { longToColor(it) }
//    }
//
//    private fun longToColor(value: Long): Color {
//        val red = ((value shr 24) and 0xFF).toFloat() / 255f
//        val green = ((value shr 16) and 0xFF).toFloat() / 255f
//        val blue = ((value shr 8) and 0xFF).toFloat() / 255f
//        val alpha = (value and 0xFF).toFloat() / 255f
//        return Color(red, green, blue, alpha)
//    }
}