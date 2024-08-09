package org.store.clothstar.member.dto.response

class AddressResponse(
    val memberId: Long,
    val receiverName: String,
    val zipNo: String,
    val addressBasic: String,
    val addressDetail: String,
    val telNo: String,
    val deliveryRequest: String,
)