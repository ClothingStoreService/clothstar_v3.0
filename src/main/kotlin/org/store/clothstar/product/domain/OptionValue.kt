package org.store.clothstar.product.domain

import jakarta.persistence.*


/***
 * {
 *   "id": 1,
 *   "code": "#5C88C9",
 *   "value": "중청",
 *   "productOption": 1
 * }
 */
@Entity
class OptionValue(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val optionValueId: Long? = null,
//    val productColor: ProductColor,  // 색상 코드
    var value: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id")
    val productOption: ProductOption,
)