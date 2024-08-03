package org.store.clothstar.product.dto.request

import org.store.clothstar.product.domain.entity.Item
import org.store.clothstar.product.domain.entity.ProductImage
import org.store.clothstar.product.domain.entity.ProductOption
import org.store.clothstar.product.domain.type.DisplayStatus
import org.store.clothstar.product.domain.type.ProductColor
import org.store.clothstar.product.domain.type.SaleStatus

class UpdateProductRequest (
    val name: String?,
    val content: String?,
    val price: Int?,
    val productColors: List<ProductColor>?,
    val displayStatus: DisplayStatus
    val saleStatus: SaleStatus?,
    val imageList: List<ProductImage>?,
    val productOptions: Set<ProductOption>?,
    val items: List<Item>?
) {

}