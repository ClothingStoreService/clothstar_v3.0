package org.store.clothstar.product.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.store.clothstar.product.domain.entity.Product
import org.store.clothstar.product.domain.type.DisplayStatus
import org.store.clothstar.product.domain.type.SaleStatus

class ProductCreateRequest(
    @Positive(message = "회원 id 는 0보다 큰 양수입니다.")
    val memberId: Long,
    @Positive(message = "카테고리 id는 0보다 큰 양수입니다.")
    val categoryId: Long,

    @NotBlank(message = "상품 이름을 입력해주세요")
    val name: String,
    @NotBlank(message = "상품 설명을 입력해주세요")
    val content: String,
    @Positive(message = "상품 가격은 0보다 커야 합니다.")
    val price: Int,
    @NotNull(message = "진열 상태를 설정해주세요")
    val displayStatus: DisplayStatus,
    @NotNull(message = "판매 상태를 설정해주세요")
    val saleStatus: SaleStatus,

    val productColors: List<ProductColorDTO>? = emptyList(),
    val imageList: List<ProductImageCreateRequest> = emptyList(),
    val productOptions: List<ProductOptionCreateRequest> = emptyList(),
    val items: List<ItemCreateRequest> = emptyList(),
    ) {
    fun toProductEntity(): Product {
        return Product(
            memberId = memberId,
            categoryId = categoryId,
            name = name,
            content = content,
            price = price,
            displayStatus = displayStatus,
            saleStatus = saleStatus,
            productColors = productColors?.map { it.toProductColor() }?.toMutableList(),
            imageList = imageList.map { it.toProductImage() }.toMutableList(),
            productOptions = productOptions.map { it.toProductOption() }.toMutableSet(),
            items = items.map { it.toItem() }.toMutableList()
        )
    }
}