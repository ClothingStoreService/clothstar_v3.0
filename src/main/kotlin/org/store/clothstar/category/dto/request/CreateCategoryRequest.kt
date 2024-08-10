package org.store.clothstar.category.dto.request

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

class CreateCategoryRequest(
    @Schema(description = "카테고리 타입(이름)", nullable = false)
    @field:NotBlank(message = "카테고리 타입을 입력해주세요.")
    val categoryType: String
)
// 공백, 긴 리스트, 빈 문자열 -> 예외처리가 되어있어야 함
// value, null 허용 X
// dto -> 데이터가 변경되는지 고민
// data -> equals(), hashcode(), toString(), comporeTo() 자동 생성 -> 사용성이 있는지 고민
// toCategory -> 모든 DTO에 Entity로 변환하는 로직이 있어야함 (toEntity)
