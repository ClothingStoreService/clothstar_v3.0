package org.store.clothstar.product.domain.entity

import jakarta.persistence.*
import org.store.clothstar.product.domain.type.DisplayStatus
import org.store.clothstar.product.domain.type.SaleStatus

/**
 * {
 *   "id": 1,
 *   "name": "중청/롱/S",
 *   "price": 29900,
 *   "finalPrice": 19900,
 *   "itemCode": null,
 *   "deliveryType": "GENERAL",
 *   "remainStock": 4,
 *   "productLine": 1,
 *   "displayStatus": "VISIBLE", // 또는 "HIDDEN" 등 해당되는 값을 사용
 *   "itemAttributes": [
 *     { "optionId": 1, "name": "색상", "value": "중청", "valueId": 1 },
 *     { "optionId": 2, "name": "기장", "value": "롱", "valueId": 6 },
 *     { "optionId": 3, "name": "사이즈", "value": "S", "valueId": 8 }
 *   ]
 * }
 */
@Entity
class Item (
    @Column
    var name: String,
    @Column
    var price: Int,
    @Column
    var stock: Int,
    @Column
    var saleStatus: SaleStatus,
    @Column
    var displayStatus: DisplayStatus,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_line_id")
    val product: Product

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @ElementCollection
    @CollectionTable(name = "item_attributes", joinColumns = [JoinColumn(name = "item_id")])

}