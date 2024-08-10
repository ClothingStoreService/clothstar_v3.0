package org.store.clothstar.order.service.OrderSave

import org.springframework.stereotype.Service
import org.store.clothstar.product.domain.Item
import org.store.clothstar.product.service.ItemService

@Service
class StockUpdaterImpl(
    private val itemService: ItemService,
): StockUpdater {
    override fun updateStock(item: Item, quantity: Int){
        itemService.deductItemStock(item, quantity)
    }
}