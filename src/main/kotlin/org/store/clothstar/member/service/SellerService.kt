package org.store.clothstar.member.service

import org.store.clothstar.member.domain.Seller
import org.store.clothstar.member.dto.request.CreateSellerRequest

interface SellerService {
    fun getSellerById(memberId: Long): Seller

    fun sellerSave(memberId: Long, createSellerRequest: CreateSellerRequest): Long
}