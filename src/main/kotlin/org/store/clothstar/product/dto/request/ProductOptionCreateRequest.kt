package org.store.clothstar.product.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import org.store.clothstar.product.domain.OptionValue

class ProductOptionCreateRequest(
    @NotBlank(message = "옵션 이름을 입력해주세요.")
    val optionName: String,
    @Positive(message = "옵션 순서는 양수입니다.")
    val optionOrderNo: Int,
    val optionValues: MutableList<OptionValue>,
)