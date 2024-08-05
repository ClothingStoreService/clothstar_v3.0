package org.store.clothstar.common.dto

class ValidErrorResponseDTO(
    private val errorCode: Int,
    private val errorMap: Map<String, String>,
)