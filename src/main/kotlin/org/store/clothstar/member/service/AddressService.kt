package org.store.clothstar.member.service

import org.store.clothstar.member.domain.Address
import org.store.clothstar.member.dto.request.CreateAddressRequest
import org.store.clothstar.member.dto.response.AddressResponse

interface AddressService {
    fun getMemberAllAddress(memberId: Long): List<AddressResponse>

    fun addrSave(memberId: Long, createAddressRequest: CreateAddressRequest): Long

    fun getAddressById(addressId: Long): Address
}