package org.store.clothstar.product.dto.request

import jakarta.validation.constraints.NotBlank
import org.store.clothstar.product.domain.ItemAttribute
import org.store.clothstar.product.domain.Product
import org.store.clothstar.product.domain.type.DisplayStatus
import org.store.clothstar.product.domain.type.SaleStatus

class ItemCreateRequest(
    @NotBlank(message = "아이템 이름을 입력해주세요")
    val name: String,
    val price: Int,
    val stock: Int,
    val saleStatus: SaleStatus,
    val displayStatus: DisplayStatus,
    val attributes: MutableSet<ItemAttribute>,
    val product: Product,
)