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


    fun updateSaleStatus(saleStatus: SaleStatus) {
        this.saleStatus = saleStatus
    }
}