package org.store.clothstar.product.domain.entity

import jakarta.persistence.*
import org.store.clothstar.product.domain.type.ProductColor


/***
 * {
 *   "id": 1,
 *   "code": "#5C88C9",
 *   "value": "중청",
 *   "productOption": 1
 * }
 */
@Entity
class OptionValue (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val productColor: ProductColor,  // 색상 코드
    var value: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    val productOption: ProductOption
)