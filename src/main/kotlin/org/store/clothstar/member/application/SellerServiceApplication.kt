package org.store.clothstar.member.application

import org.springframework.stereotype.Service
import org.store.clothstar.member.domain.MemberRole
import org.store.clothstar.member.domain.Seller
import org.store.clothstar.member.dto.request.CreateSellerRequest
import org.store.clothstar.member.service.AccountService
import org.store.clothstar.member.service.SellerService

@Service
class SellerServiceApplication(
    private val sellerService: SellerService,
    private val accountService: AccountService,
) {
    fun getSellerById(memberId: Long): Seller {
        return sellerService.getSellerById(memberId)
    }

    fun sellerSave(memberId: Long, createSellerRequest: CreateSellerRequest) {
        sellerService.sellerSave(memberId, createSellerRequest)
        accountService.updateRole(memberId, MemberRole.USER, MemberRole.SELLER)
    }
}