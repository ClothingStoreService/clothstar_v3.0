package org.store.clothstar.member.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.NotFoundAddressException
import org.store.clothstar.common.error.exception.NotFoundMemberException
import org.store.clothstar.member.domain.Address
import org.store.clothstar.member.domain.vo.AddressInfo
import org.store.clothstar.member.dto.request.CreateAddressRequest
import org.store.clothstar.member.dto.response.AddressResponse
import org.store.clothstar.member.repository.AddressRepository
import org.store.clothstar.member.repository.MemberRepository

@Service
class AddressServiceImpl(
    private val addressRepository: AddressRepository,
    private val memberRepository: MemberRepository,
) : AddressService {
    @Transactional(readOnly = true)
    override fun getMemberAllAddress(memberId: Long): List<AddressResponse> {
        memberRepository.findByMemberId(memberId)
            ?: throw NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER)

        return addressRepository.findAddressListByMemberId(memberId).map { address ->
            address?.let {
                AddressResponse(
                    memberId = address.memberId,
                    receiverName = it.receiverName,
                    telNo = address.telNo,
                    addressBasic = address.addressInfo.addressBasic,
                    addressDetail = address.addressInfo.addressDetail,
                    zipNo = address.addressInfo.zipNo,
                    deliveryRequest = address.deliveryRequest
                )
            } ?: throw IllegalArgumentException("요청한 회원의 주소가 없습니다.")
        }
    }

    @Transactional(readOnly = true)
    override fun getAddressById(addressId: Long): Address {
        return addressRepository.findByAddressId(addressId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "배송지 정보를 찾을 수 없습니다.")
    }

    @Transactional
    override fun addrSave(memberId: Long, createAddressRequest: CreateAddressRequest): Long {
        memberRepository.findByMemberId(memberId)
            ?: throw NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER)

        val address = Address(
            receiverName = createAddressRequest.receiverName,
            addressInfo = AddressInfo(
                addressBasic = createAddressRequest.addressBasic,
                addressDetail = createAddressRequest.addressDetail,
                zipNo = createAddressRequest.zipNo,
            ),
            deliveryRequest = createAddressRequest.deliveryRequest,
            telNo = createAddressRequest.telNo,
            memberId = memberId
        )
        addressRepository.save(address)

        return address.addressId!!
    }

    override fun getAddressById(addressId: Long): Address {
        return addressRepository.findByIdOrNull(addressId)
            ?: throw NotFoundAddressException(ErrorCode.NOT_FOUND_ADDRESS)
    }
}