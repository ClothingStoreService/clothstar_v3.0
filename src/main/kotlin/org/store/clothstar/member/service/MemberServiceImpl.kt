package org.store.clothstar.member.service

import jakarta.transaction.Transactional
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.store.clothstar.dto.request.CreateMemberRequest
import org.store.clothstar.member.repository.AccountRepository

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
class MemberServiceImpl(
    private val accountRepository: AccountRepository
) : MemberService {
    override fun signUp(createMemberDTO: CreateMemberRequest) {
        val account = createMemberDTO.toAccount()
        
    }
}