package org.store.clothstar.member.dto.response

class AddressResponse(
    private var memberId: Long,
    private val receiverName: String,
    private val zipNo: String,
    private val addressBasic: String,
    private val addressDetail: String,
    private val telNo: String,
    private val deliveryRequest: String,
)