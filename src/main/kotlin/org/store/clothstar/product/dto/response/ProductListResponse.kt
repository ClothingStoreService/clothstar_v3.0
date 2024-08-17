package org.store.clothstar.product.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import org.store.clothstar.member.dto.response.SellerSimpleResponse
import org.store.clothstar.product.domain.Item
import org.store.clothstar.product.domain.Product
import org.store.clothstar.product.domain.type.SaleStatus

class ProductListResponse(
    @Schema(description = "상품 id", example = "1")
    val productId: Long,

    @Schema(description = "상품 이름", example = "우유 모자")
    val name: String,

    @Schema(description = "상품 설명", example = "우유 모자입니다.")
    val content: String,

    @Schema(description = "상품 가격", example = "10000")
    val price: Int = 0,

    @Schema(description = "상품 상태", example = "FOR_SALE")
    val saleStatus: SaleStatus,

    @Schema(description = "상품 판매량", example = "10")
    val saleCount: Long, // ~개 판매중

    @Schema(description = "판매자 정보")
    val seller: SellerSimpleResponse,
) {
    companion object {
        fun from(product: Product, seller: SellerSimpleResponse): ProductListResponse {
            return ProductListResponse(
                productId = product.productId!!,
                name = product.name,
                price = product.price,
                saleCount = product.saleCount,
                content = product.content,
                saleStatus = product.saleStatus,
                seller = seller
            )
        }
    }
}