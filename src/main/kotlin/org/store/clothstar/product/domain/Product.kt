package org.store.clothstar.product.domain

import jakarta.persistence.*
import org.hibernate.annotations.BatchSize
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import org.store.clothstar.common.entity.BaseEntity
import org.store.clothstar.product.domain.type.DisplayStatus
import org.store.clothstar.product.domain.type.ProductColor
import org.store.clothstar.product.domain.type.SaleStatus

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

    // 색상 목록
    @ElementCollection(targetClass = ProductColor::class)
    @CollectionTable(name = "product_colors", joinColumns = [JoinColumn(name = "product_id")])
    @Enumerated(EnumType.STRING)
    @Fetch(FetchMode.SUBSELECT)
    var productColors: MutableSet<ProductColor> = mutableSetOf(),

    // 이미지 목록
    @ElementCollection
    @CollectionTable(name = "product_image", joinColumns = [JoinColumn(name = "product_line_id")])
    var imageList: MutableSet<ProductImage> = mutableSetOf(),

    // 기타 정보
    var saleCount: Long = 0,
    @Enumerated(EnumType.STRING)
    var displayStatus: DisplayStatus,  // 진열 여부
    @Enumerated(EnumType.STRING)
    var saleStatus: SaleStatus,  // 판매 상태

    // 연관 관계 (1:N)
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "product", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var productOptions: MutableList<ProductOption> = mutableListOf(),

    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var items: MutableList<Item> = mutableListOf(),
) : BaseEntity() {


    fun updateSaleStatus(saleStatus: SaleStatus) {
        this.saleStatus = saleStatus
    }

    fun updateDisplayStatus(displayStatus: DisplayStatus) {
        this.displayStatus = displayStatus
    }
}