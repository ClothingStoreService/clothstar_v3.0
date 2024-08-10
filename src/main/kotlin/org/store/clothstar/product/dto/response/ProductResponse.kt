package org.store.clothstar.product.dto.response

import org.store.clothstar.product.domain.*
import org.store.clothstar.product.domain.type.DisplayStatus
import org.store.clothstar.product.domain.type.ImageType
import org.store.clothstar.product.domain.type.ProductColor
import org.store.clothstar.product.domain.type.SaleStatus

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
class ProductResponse(
    val id: Long,
    val name: String,
    val description: String,
    val price: Int,
    val displayStatus: DisplayStatus,
    val saleStatus: SaleStatus,
    val productColors: Set<ProductColor>,
    val imageList: List<ImageResponse>,
    val productOptions: List<ProductOptionResponse>,
    val items: List<ItemResponse>
) {
    companion object {
        fun from(product: Product): ProductResponse {
            val distinctImages = product.imageList.distinctBy { it.url }
            return ProductResponse(
                id = product.productId!!,
                name = product.name,
                description = product.content,
                imageList = distinctImages.map { ImageResponse.from(it) },
                productColors = product.productColors.toSet(),
                price = product.price,
                displayStatus = product.displayStatus,
                saleStatus = product.saleStatus,
                productOptions = product.productOptions.map { ProductOptionResponse.from(it) },
                items = product.items.map { ItemResponse.from(it) }
            )
        }
    }
}

class ProductOptionResponse(
    val id: Long,
    val name: String,
    val orderNo: Int,
    val values: List<String>
) {
    companion object {
        fun from(option: ProductOption): ProductOptionResponse {
            return ProductOptionResponse(
                id = option.productOptionId!!,
                name = option.optionName,
                orderNo = option.optionOrderNo,
                values = option.optionValues.map { it.value }
            )
        }
    }
}

class ImageResponse(
    val url: String,
    val originUrl: String,
    val imageType: ImageType
) {
    companion object {
        fun from(image: ProductImage): ImageResponse {
            return ImageResponse(
                url = image.url,
                originUrl = image.originUrl,
                imageType = image.imageType
            )
        }
    }
}

class ItemResponse(
    val id: Long,
    val name: String,
    val price: Int,
    val finalPrice: Int,
    val remainStock: Int,
    val saleStatus: SaleStatus,
    val displayStatus: DisplayStatus,
    val attributes: List<ItemAttributeResponse>
) {
    companion object {
        fun from(item: Item): ItemResponse {
            return ItemResponse(
                id = item.itemId!!,
                name = item.name,
                price = item.finalPrice,
                finalPrice = item.finalPrice,
                remainStock = item.stock,
                saleStatus = item.saleStatus,
                displayStatus = item.displayStatus,
                attributes = item.attributes.map { ItemAttributeResponse.from(it) }
            )
        }
    }
}

class ItemAttributeResponse(
    val optionId: Long,
    val name: String,
    val value: String,
    val valueId: Long
) {
    companion object {
        fun from(attribute: ItemAttribute): ItemAttributeResponse {
            return ItemAttributeResponse(
                optionId = attribute.optionId,
                name = attribute.name,
                value = attribute.value,
                valueId = attribute.valueId
            )
        }
    }
}