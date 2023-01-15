package com.omtorney.doer.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class LineSeparatorStateUseCase @Inject constructor(repository: Repository) {

    private val scope = CoroutineScope(Dispatchers.Default)

    val execute = repository.getLineSeparatorState.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = true
    )
}