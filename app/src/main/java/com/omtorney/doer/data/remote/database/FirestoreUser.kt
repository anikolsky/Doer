package com.omtorney.doer.data.remote.database

data class FirestoreUser(
    val id: String? = null,
    val name: String? = null,
    val notes: String? = null
) {
    fun toMap(): Map<String, Any?> {
        return mapOf("name" to name, "notes" to notes)
    }
}
