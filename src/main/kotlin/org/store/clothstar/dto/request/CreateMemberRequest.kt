package org.store.clothstar.dto.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor

@AllArgsConstructor
@NoArgsConstructor
data class CreateMemberRequest(
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    val email: String,

    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    val password: String
) {

//    fun toAccount(): Account {
//        return Account(
//            email = email,
//            password = password
//        )
//        return Account(email, password)
//    }
}