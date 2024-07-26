package org.store.clothstar.dto.request

import org.jetbrains.annotations.NotNull

data class ModifyNameRequest(
    @NotNull
    val name: String
) {
}