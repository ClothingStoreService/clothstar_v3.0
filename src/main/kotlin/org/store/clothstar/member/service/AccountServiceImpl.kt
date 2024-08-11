package org.store.clothstar.member.service

import jakarta.transaction.Transactional
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.DuplicatedEmailException
import org.store.clothstar.common.error.exception.NotFoundAccountException
import org.store.clothstar.member.domain.Account
import org.store.clothstar.member.domain.MemberRole
import org.store.clothstar.member.dto.request.CreateMemberRequest
import org.store.clothstar.member.repository.AccountRepository

@Service
class AccountServiceImpl(
    private val accountRepository: AccountRepository,
    private val passwordEncoder: PasswordEncoder
) : AccountService {
    @Transactional
    override fun saveAccount(memberId: Long, createMemberDTO: CreateMemberRequest): Account {
        saveAccountValidCheck(createMemberDTO)

        val account = Account(
            email = createMemberDTO.email,
            password = passwordEncoder.encode(createMemberDTO.password),
            role = MemberRole.USER,
            userId = memberId,
        )

        return accountRepository.save(account)
    }

    @Transactional
    override fun updateRole(memberId: Long, findRole: MemberRole, updateRole: MemberRole) {
        val account = accountRepository.findByUserIdAndRole(memberId, findRole)
            ?: throw NotFoundAccountException(ErrorCode.NOT_FOUND_ACCOUNT)

        account.updateRole(updateRole)
    }

    @Transactional
    override fun updateDeletedAt(memberId: Long, findRole: MemberRole) {
        val account = accountRepository.findByUserIdAndRole(memberId, findRole)
            ?: throw NotFoundAccountException(ErrorCode.NOT_FOUND_ACCOUNT)

        account.updateDeletedAt()
    }

    fun saveAccountValidCheck(createMemberDTO: CreateMemberRequest) {
        accountRepository.findByEmail(createMemberDTO.email)?.let {
            throw DuplicatedEmailException(ErrorCode.DUPLICATED_EMAIL)
        }
    }
}