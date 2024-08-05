package org.store.clothstar.product.dto.request

import jakarta.persistence.criteria.CriteriaBuilder.In
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive
import org.store.clothstar.product.domain.type.OptionType

class ProductOptionCreateRequest (
    @NotBlank(message = "옵션 이름을 입력해주세요.")
    val optionName: String,
    @Positive(message = "옵션 순서는 양수입니다.")
    val orderNo: Int,
    @NotBlank(message = "옵션 타입을 입력해주세요.")
    val optionType: OptionType = OptionType.BASIC,
)