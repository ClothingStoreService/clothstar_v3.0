package org.store.clothstar.product.dto.request


import jakarta.validation.constraints.NotNull
import org.store.clothstar.product.domain.type.DisplayStatus

class UpdateDisplayStatusRequest(

    @NotNull(message = "진열 상태를 설정해주세요.")
    val displayStatus: DisplayStatus,
)
