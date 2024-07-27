package org.store.clothstar.member.dto.request

import org.jetbrains.annotations.NotNull

data class ModifyNameRequest(
    @NotNull
    val name: String
) {
}