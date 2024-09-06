package org.store.clothstar.product.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@Controller
class ProductViewController {
    @GetMapping("/productPagingOffset")
    fun productLinePagingOffset(): String {
        return "productOffsetList"
    }

    @GetMapping("/products/{productId}")
    fun productDetail(@PathVariable productId: Long, model: Model): String {
        model.addAttribute("productId", productId)
        return "productDetail"
    }
}