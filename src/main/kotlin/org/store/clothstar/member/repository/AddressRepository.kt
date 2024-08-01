package org.store.clothstar.member.repository

import io.lettuce.core.dynamic.annotation.Param
import jakarta.mail.Address
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface AddressRepository : JpaRepository<Address, Long> {
    @Query("SELECT addr FROM address addr WHERE addr.member.memberId = :memberId")
    fun findAddressListByMemberId(@Param("memberId") memberId: Long): List<Address>

    @Query("SELECT addr FROM address addr WHERE addr.member.memberId = :memberId AND addr.defaultAddress IS TRUE")
    fun findByMemberIdAndDefaultAddress(memberId: Long): Address?
}