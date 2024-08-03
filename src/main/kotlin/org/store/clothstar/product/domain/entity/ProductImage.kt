package org.store.clothstar.product.domain.entity

import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.store.clothstar.product.domain.type.ImageType

@Embeddable
class ProductImage (
    val imageUrl: String,
    @Enumerated(EnumType.STRING)
    val imageType: ImageType
)
