package org.store.clothstar.product.domain

import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import org.store.clothstar.product.domain.type.ImageType

@Embeddable
class ProductImage(
    val url: String,
    val originUrl: String,
    @Enumerated(EnumType.STRING)
    val imageType: ImageType
)
