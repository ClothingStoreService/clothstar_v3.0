package org.store.clothstar.payment.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable


@Controller
class PaymentViewController {
    @GetMapping("/payment/{productId}/{itemId}")
    fun paymentPage(@PathVariable itemId: Long, @PathVariable productId: Long, model: Model): String {
        model.addAttribute("productId", productId)
        model.addAttribute("itemId", itemId)
        return "payment"
    }
}