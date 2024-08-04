package org.store.clothstar.product.domain.entity

import jakarta.persistence.*
import org.store.clothstar.product.domain.type.OptionType


/**
 * {
 *   "id": 1,
 *   "name": "색상",
 *   "order": 0,
 *   "required": true,
 *   "optionType": "BASIC",
 *   "catalogProduct": 1,
 *   "optionValues": [
 *     { "id": 1, "code": "#5C88C9", "value": "중청" },
 *     { "id": 2, "code": "#778899", "value": "애쉬블루" },
 *     { "id": 3, "code": null, "value": "연청" },
 *     { "id": 4, "code": null, "value": "(썸머)연청" },
 *     { "id": 5, "code": null, "value": "(썸머)중청" }
 *   ]
 * }
 */
@Entity
class ProductOption(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val productOptionId: Long? = null,

    val name: String,
    val order: Int = 0,
    val required: Boolean = true,
    @Enumerated(EnumType.STRING)
    val optionType: OptionType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="product_id")
    val product: Product,

    @OneToMany(mappedBy = "productOption", cascade = [CascadeType.ALL], orphanRemoval = true)
    var optionValues: MutableList<OptionValue> = mutableListOf(),
    )