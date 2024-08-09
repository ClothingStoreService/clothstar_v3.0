package org.store.clothstar.product.dto.request

import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import jakarta.validation.constraints.PositiveOrZero
import org.springframework.web.multipart.MultipartFile
import org.store.clothstar.product.domain.Product
import org.store.clothstar.product.domain.type.DisplayStatus
import org.store.clothstar.product.domain.type.ImageType
import org.store.clothstar.product.domain.type.SaleStatus

class ProductCreateRequest(
    @Positive(message = "회원 id는 0보다 큰 양수입니다.")
    val memberId: Long,
    @Positive(message = "카테고리 id는 0보다 큰 양수입니다.")
    val categoryId: Long,

    @NotBlank(message = "상품 이름을 입력해주세요.")
    val name: String,
    @NotBlank(message = "상품 설명을 입력해주세요.")
    val content: String,
    @Positive(message = "상품 가격은 0보다 커야 합니다.")
    val price: Int,
    @NotNull(message = "진열 상태를 설정해주세요.")
    val displayStatus: DisplayStatus,
    @NotNull(message = "판매 상태를 설정해주세요.")
    val saleStatus: SaleStatus,

    // TODO: 색상 필터링을 위한 색상 목록 받
//    val productColors: Set<ProductColor>? = emptySet(),
    val productOptions: List<ProductOptionCreateRequest> = emptyList(),
    val items: List<ItemCreateRequest> = emptyList(),
) {

    class ProductColorDTO(
        @NotBlank(message = "색상을 선택해주세요.")
        val color: String
    )

    class ProductOptionCreateRequest(
        @NotBlank(message = "옵션 이름을 입력해주세요.")
        val optionName: String,
        @Positive(message = "옵션 순서는 양수입니다.")
        val optionOrderNo: Int,
        @NotBlank(message = "옵션 종류는 쉼표로 구분해서 작성해주세요.")
        val optionValues: String,
    )

    class ItemCreateRequest(
        @Positive
        val price: Int,
        @PositiveOrZero
        val stock: Int,
        @NotNull
        val displayStatus: DisplayStatus,
        val optionAttributes: List<OptionAttributeRequest> // 옵션 조합을 나타내는 리스트
    ) {
        data class OptionAttributeRequest(
            val optionOrderNo: Int,
            val optionValue: String
        )
    }

    fun toProductEntity(): Product {
        return Product(
            memberId = memberId,
            categoryId = categoryId,
            name = name,
            content = content,
            price = price,
            displayStatus = displayStatus,
            saleStatus = saleStatus,
            saleCount = 0,
        )
    }
}