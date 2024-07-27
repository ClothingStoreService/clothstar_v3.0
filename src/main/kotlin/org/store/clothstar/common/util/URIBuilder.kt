package org.store.clothstar.common.util

import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

// object -> 싱글톤 객체 : java 의 public static class 와 같은 역할
object URIBuilder {
    fun buildURI(id: Long?): URI {
        return ServletUriComponentsBuilder
            .fromCurrentRequest() // 현재 요청의 URI를 사용
            .path("/{id}") // 경로 변수 추가
            .buildAndExpand(id) // {/id} 자리에 실제 id 값을 삽입
            .toUri()
    }
}