package org.store.clothstar.kakaoLogin.vo

import com.fasterxml.jackson.annotation.JsonProperty

class KakaoAccount(
    // 	사용자 동의 시 닉네임 제공 가능
    // 	필요한 동의항목: 닉네임
    @JsonProperty("profile_nickname_needs_agreement") val profileNicknameNeedsAgreement: Boolean? = null,
    // 사용자 동의 시 프로필 사진 제공 가능
    // 필요한 동의항목: 프로필 사진
    @JsonProperty("profile_image_needs_agreement") val profileImageNeedsAgreement: Boolean? = null,
    // 이메일 유무 여부
    // 필요한 동의항목: 카카오계정(이메일)
    @JsonProperty("has_email") val hasEmail: Boolean? = null,
    // 사용자 동의 시 카카오계정 대표 이메일 제공 가능
    // 필요한 동의항목: 카카오계정(이메일)
    @JsonProperty("email_needs_agreement") val emailNeedsAgreement: Boolean? = null,
    // 이메일 유효 여부
    // 필요한 동의항목: 카카오계정(이메일)
    // true: 유효한 이메일 / false: 이메일이 다른 카카오계정에 사용돼 만료
    @JsonProperty("is_email_valid") val isEmailValid: Boolean? = null,
    // 이메일 인증 여부
    // 필요한 동의항목: 카카오계정(이메일)
    // true: 인증된 이메일 / false: 인증되지 않은 이메일
    @JsonProperty("is_email_verified") val isEmailVerified: Boolean? = null,
    // 카카오계정 대표 이메일
    // 필요한 동의항목: 카카오계정(이메일)
    @JsonProperty("email") val email: String? = null,
    // 프로필 정보
    // 필요한 동의항목: 프로필 정보(닉네임/프로필 사진), 닉네임, 프로필 사진
    @JsonProperty("profile") val profile: Profile? = null,
) {
    data class Profile(
        // 닉네임
        // 필요한 동의항목: 프로필 정보(닉네임/프로필 사진) 또는 닉네임
        @JsonProperty("nickname") val nickname: String? = null,
        // 프로필 미리보기 이미지 URL
        // 110px * 110px 또는 100px * 100px
        // 필요한 동의항목: 프로필 정보(닉네임/프로필 사진) 또는 프로필 사진
        @JsonProperty("thumbnail_image_url") val thumbnailImageUrl: String? = null,
        // 프로필 사진 URL
        // 640px * 640px 또는 480px * 480px
        // 필요한 동의항목: 프로필 정보(닉네임/프로필 사진) 또는 프로필 사진
        @JsonProperty("profile_image_url") val profileImageUrl: String? = null,
        // 프로필 사진 URL이 기본 프로필 사진 URL인지 여부
        // 사용자가 등록한 프로필 사진이 없을 경우, 기본 프로필 사진 제공
        // true: 기본 프로필 사진 / false: 사용자가 등록한 프로필 사진
        // 필요한 동의항목: 프로필 정보(닉네임/프로필 사진) 또는 프로필 사진
        @JsonProperty("is_default_image") val isDefaultImage: Boolean? = null,
        // 닉네임이 기본 닉네임인지 여부
        // 사용자가 등록한 닉네임이 운영정책에 부합하지 않는 경우, "닉네임을 등록해주세요"가 기본 닉네임으로 적용됨
        // true: 기본 닉네임 / false: 사용자가 등록한 닉네임
        // 필요한 동의항목: 프로필 정보(닉네임/프로필 사진) 또는 닉네임
        @JsonProperty("is_default_nickname") val isDefaultNickname: Boolean? = null
    )
}