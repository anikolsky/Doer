package com.omtorney.doer.settings.domain.usecase

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.omtorney.doer.core.domain.Repository
import javax.inject.Inject

class SetSecondaryColor@Inject constructor(
    private val repository: Repository
) {

    suspend operator fun invoke(color: Color) {
        repository.setSecondaryColor(color.toArgb().toLong())
    }
}