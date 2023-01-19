package com.omtorney.doer.settings.domain.usecase

import androidx.compose.ui.graphics.Color
import com.omtorney.doer.core.domain.Repository

class SetSecondaryColor(private val repository: Repository) {

    suspend operator fun invoke(color: Color) {
        repository.setSecondaryColor(colorToLong(color))
    }

    private fun colorToLong(color: Color): Long {
        val red = (color.red * 255).toLong() shl 24
        val green = (color.green * 255).toLong() shl 16
        val blue = (color.blue * 255).toLong() shl 8
        val alpha = (color.alpha * 255).toLong()
        return red.or(green).or(blue).or(alpha)
    }
}