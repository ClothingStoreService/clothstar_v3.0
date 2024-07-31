package org.store.clothstar.member.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.store.clothstar.member.domain.Member

interface MemberRepository : JpaRepository<Member, Long> {
    fun findByTelNo(telNo: String): Member?
}