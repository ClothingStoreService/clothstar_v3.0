package org.store.clothstar.product.domain

import jakarta.persistence.*
import org.store.clothstar.common.entity.BaseEntity
import org.store.clothstar.product.domain.type.DisplayStatus
import org.store.clothstar.product.domain.type.ProductColor
import org.store.clothstar.product.domain.type.SaleStatus
import org.store.clothstar.product.dto.request.UpdateProductRequest

@Entity
class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val productId: Long? = null,

    // 연관 관계 필드 (N:1)
    @Column(name = "member_id")
    val memberId: Long,

    @Column(name = "category_id")
    val categoryId: Long,

    // 기본 정보 필드
    var name: String,
    var content: String,
    var price: Int,
    @ElementCollection
    @CollectionTable(name = "product_color", joinColumns = [JoinColumn(name = "product_id")])
    var productColors: MutableList<ProductColor>? = mutableListOf(),

    // 이미지 목록
    @ElementCollection
    @CollectionTable(name = "product_image", joinColumns = [JoinColumn(name = "product_line_id")])
    var imageList: MutableList<ProductImage> = mutableListOf(),

    // 기타 정보
    var saleCount: Long = 0,
    @Enumerated(EnumType.STRING)
    var displayStatus: DisplayStatus,  // 진열 여부
    @Enumerated(EnumType.STRING)
    var saleStatus: SaleStatus,  // 판매 상태

    // 연관 관계 (1:N)
    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var productOptions: MutableSet<ProductOption> = mutableSetOf(),

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var items: MutableList<Item> = mutableListOf(),
) : BaseEntity() {


    /**
     * @param UpdateProductRequest
     * @return null
     */
    fun update(request: UpdateProductRequest) {
        this.name = request.name ?: this.name
        this.content = request.content ?: this.content
        this.price = request.price ?: this.price
        this.displayStatus = request.displayStatus
        this.saleStatus = request.saleStatus ?: this.saleStatus

        // imageList 업데이트 (이미지 리스트가 완전히 교체되는 경우)
        request.imageList?.let {
            this.imageList.clear()
            this.imageList.addAll(it)
        }

        // productOptions 업데이트 로직 (삭제 불가)
        request.productOptions?.let {
            val newOptions = it.toSet()
            val existingOptions = this.productOptions.map { it.productOptionId }.toSet()

            // 기존 옵션들에 포함되지 않은 새 옵션 추가
            newOptions.forEach { newOption ->
                if (newOption.productOptionId !in existingOptions) {
                    this.productOptions.add(newOption)
                }
            }

            // 기존 옵션 삭제 방지
            val toRemove = this.productOptions.filterNot { it.productOptionId in newOptions.map { it.productOptionId } }
            if (toRemove.isNotEmpty()) {
                throw IllegalArgumentException("Existing product options cannot be removed")
            }
        }

        // items 업데이트 로직 (삭제 불가)
        request.items?.let {
            val newItems = it.toSet()
            val existingItems = this.items.map { it.itemId }.toSet()

            // 기존 아이템들에 포함되지 않은 새 아이템 추가
            newItems.forEach { newItem ->
                if (newItem.itemId !in existingItems) {
                    this.items.add(newItem)
                }
            }

            // 기존 아이템 삭제 방지
            val toRemove = this.items.filterNot { it.itemId in newItems.map { it.itemId } }
            if (toRemove.isNotEmpty()) {
                throw IllegalArgumentException("Existing items cannot be removed")
            }
        }

        // productColors 업데이트 로직
        request.productColors?.let {
            this.productColors?.clear()
            this.productColors?.addAll(it)
        }
    }

    fun updateSaleStatus(saleStatus: SaleStatus) {
        this.saleStatus = saleStatus
    }
}