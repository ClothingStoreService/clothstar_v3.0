package org.store.clothstar.kakaoLogin.dto

import com.fasterxml.jackson.annotation.JsonProperty
import org.store.clothstar.kakaoLogin.vo.KakaoAccount
import org.store.clothstar.kakaoLogin.vo.Properties

class KakaoUserInfoResponseDto (
    // 회원번호
    @JsonProperty("id") val id: Long? = null,
    // 서비스에 연결 완료된 시각, UTC*
    @JsonProperty("connected_at") val connectedAt: String? = null,
    // 카카오계정 정보
    @JsonProperty("kakao_account") val kakaoAccount: KakaoAccount? = null,
    // 사용자 프로퍼티(Property)
    @JsonProperty("properties") val properties: Properties? = null,
)