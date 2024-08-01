package org.store.clothstar.member.domain.vo

class AddressInfo(
    private var addressBasic: String,
    private val addressDetail: String,
    private val zipNo: String,
    private val deliveryRequest: String,
)