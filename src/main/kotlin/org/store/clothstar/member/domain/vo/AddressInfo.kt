package org.store.clothstar.member.domain.vo

import jakarta.persistence.Embeddable

@Embeddable
class AddressInfo(
    val addressBasic: String,
    val addressDetail: String,
    val zipNo: String,
) {
    companion object {
        fun init(): AddressInfo {
            return AddressInfo(
                addressBasic = "address1",
                addressDetail = "address2",
                zipNo = "01234",
            )
        }
    }
}