package com.omtorney.doer.domain.usecases.settings

import com.omtorney.doer.domain.repository.Repository
import javax.inject.Inject

class SetNoteSeparatorSize @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(size: Int) {
        repository.setNoteSeparatorSize(size)
    }
}
