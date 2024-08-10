package org.store.clothstar.order.service.OrderSave

import org.store.clothstar.product.domain.Item

interface StockUpdater {
    fun updateStock(item: Item, quantity: Int)
}