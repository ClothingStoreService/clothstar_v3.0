package org.store.clothstar.product.domain

import jakarta.persistence.Embeddable
import org.hibernate.annotations.BatchSize

/**
 * { "optionId": 1, "name": "색상", "value": "중청", "valueId": 1 }
 */
@BatchSize(size = 20)
@Embeddable
data class ItemAttribute(
    var optionId: Long,
    var name: String,
    var value: String,
    var valueId: Long,
)