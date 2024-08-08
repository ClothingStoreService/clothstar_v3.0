package org.store.clothstar.member.service

import org.springframework.stereotype.Service
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.DuplicatedEmailException
import org.store.clothstar.member.domain.Account
import org.store.clothstar.member.domain.MemberRole
import org.store.clothstar.member.dto.request.CreateMemberRequest
import org.store.clothstar.member.repository.AccountRepository

@Service
class AccountServiceImpl(
    private val accountRepository: AccountRepository,
) : AccountService {
    override fun saveAccount(memberId: Long, createMemberDTO: CreateMemberRequest): Account {
        saveAccountValidCheck(createMemberDTO)

        val account = Account(
            email = createMemberDTO.email,
            password = createMemberDTO.password,
            role = MemberRole.USER,
            userId = memberId,
        )

        return accountRepository.save(account)
    }

    fun saveAccountValidCheck(createMemberDTO: CreateMemberRequest) {
        accountRepository.findByEmail(createMemberDTO.email)?.let {
            throw DuplicatedEmailException(ErrorCode.DUPLICATED_EMAIL)
        }
    }
}