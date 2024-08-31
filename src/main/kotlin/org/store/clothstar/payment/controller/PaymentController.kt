package org.store.clothstar.payment.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping


@Controller
class PaymentController {
    @GetMapping("/payment")
    fun paymentPage(): String {
        return "payment"
    }
}
