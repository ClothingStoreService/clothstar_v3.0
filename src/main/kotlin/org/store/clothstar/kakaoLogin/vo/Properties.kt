package org.store.clothstar.kakaoLogin.vo

import com.fasterxml.jackson.annotation.JsonProperty

class Properties (
    // 닉네임
    // 필요한 동의항목: 프로필 정보(닉네임/프로필 사진) 또는 닉네임
    @JsonProperty("nickname") val nickname: String? = null,
    // 프로필 사진 URL
    // 640px * 640px 또는 480px * 480px
    // 필요한 동의항목: 프로필 정보(닉네임/프로필 사진) 또는 프로필 사진
    @JsonProperty("profile_image") val profileImage: String? = null,
    // 프로필 미리보기 이미지 URL
    // 110px * 110px 또는 100px * 100px
    // 필요한 동의항목: 프로필 정보(닉네임/프로필 사진) 또는 프로필 사진
    @JsonProperty("thumbnail_image") val thumbnailImage: String? = null,
    // 테스트용 사용자 프로퍼티
    @JsonProperty("test_property") val testProperty: String? = null,
)