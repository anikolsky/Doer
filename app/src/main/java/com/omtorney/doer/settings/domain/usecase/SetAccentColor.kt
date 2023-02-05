package com.omtorney.doer.settings.domain.usecase

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.omtorney.doer.core.domain.Repository
import javax.inject.Inject

class SetAccentColor @Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(color: Color) {
        repository.setAccentColor(color.toArgb().toLong())
    }
}