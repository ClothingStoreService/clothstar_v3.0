package org.store.clothstar.member.repository

import io.lettuce.core.dynamic.annotation.Param
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.store.clothstar.member.domain.Address

@Repository
interface AddressRepository : JpaRepository<Address, Long> {
    @Query("SELECT addr FROM address addr WHERE addr.memberId = :memberId")
    fun findAddressListByMemberId(@Param("memberId") memberId: Long): List<Address?>

    fun findByAddressId(addressId: Long): Address?
}