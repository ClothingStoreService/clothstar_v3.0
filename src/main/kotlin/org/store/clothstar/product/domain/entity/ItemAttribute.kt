package org.store.clothstar.product.domain.entity

import jakarta.persistence.Embeddable

/**
 * { "optionId": 1, "name": "색상", "value": "중청", "valueId": 1 }
 */
@Embeddable
data class ItemAttribute (
    var optionId: Long,
    var name: String,
    var value: String,
    var valueId: Long,
)