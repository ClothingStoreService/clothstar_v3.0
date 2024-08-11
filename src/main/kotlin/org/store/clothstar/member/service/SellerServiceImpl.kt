package org.store.clothstar.member.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.DuplicatedBizNoException
import org.store.clothstar.common.error.exception.DuplicatedBrandNameException
import org.store.clothstar.common.error.exception.DuplicatedSellerException
import org.store.clothstar.common.error.exception.NotFoundMemberException
import org.store.clothstar.member.domain.Seller
import org.store.clothstar.member.dto.request.CreateSellerRequest
import org.store.clothstar.member.repository.MemberRepository
import org.store.clothstar.member.repository.SellerRepository

@Service
class SellerServiceImpl(
    private val sellerRepository: SellerRepository,
    private val memberRepository: MemberRepository,
) : SellerService {
    @Transactional(readOnly = true)
    override fun getSellerById(memberId: Long): Seller {
        return sellerRepository.findById(memberId)
            .orElseThrow { NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER) }
    }

    @Transactional
    override fun sellerSave(memberId: Long, createSellerRequest: CreateSellerRequest): Long {
        validCheck(memberId, createSellerRequest)

        val seller = Seller(
            memberId = memberId,
            brandName = createSellerRequest.brandName,
            bizNo = createSellerRequest.bizNo,
            totalSellPrice = 0,
        )

        sellerRepository.save(seller)

        return memberId
    }

    private fun validCheck(memberId: Long, createSellerRequest: CreateSellerRequest) {
        memberRepository.findByMemberId(memberId)
            ?: throw NotFoundMemberException(ErrorCode.NOT_FOUND_MEMBER)

        sellerRepository.findByMemberId(memberId)?.let {
            throw DuplicatedSellerException(ErrorCode.DUPLICATED_SELLER)
        }

        sellerRepository.findByBizNo(createSellerRequest.bizNo)?.let {
            throw DuplicatedBizNoException(ErrorCode.DUPLICATED_BIZNO)
        }

        sellerRepository.findByBrandName(createSellerRequest.brandName)?.let {
            throw DuplicatedBrandNameException(ErrorCode.DUPLICATED_BRAND_NAME)
        }
    }
}