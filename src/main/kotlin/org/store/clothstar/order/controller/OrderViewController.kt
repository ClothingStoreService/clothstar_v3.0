package org.store.clothstar.order.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class OrderViewController {
    @GetMapping("/ordersPagingOffset")
    fun ordersPagingOffset(): String {
        return "orderOffsetList";
    }

    @GetMapping("/ordersPagingSlice")
    fun ordersPagingSlice(): String {
        return "orderSliceList";
    }
}