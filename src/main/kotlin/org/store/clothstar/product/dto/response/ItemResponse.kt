package org.store.clothstar.product.dto.response

import org.store.clothstar.product.domain.Item

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
class ItemResponse(
    private val itemId: Long,
    private val productId: Long,
    private val name: String,
    private val price: Int,
    private val stock: Int,
) {
    companion object {
        fun from(item: Item): ItemResponse {
            return ItemResponse(
                itemId = item.itemId!!,
                productId = item.product.productId!!,
                name = item.name,
                price = item.price,
                stock = item.stock,
            )
        }
    }
}