package org.store.clothstar.member.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.store.clothstar.common.dto.ErrorResponseDTO
import org.store.clothstar.common.dto.MessageDTO
import org.store.clothstar.member.dto.request.CreateAddressRequest
import org.store.clothstar.member.dto.response.AddressResponse
import org.store.clothstar.member.dto.response.MemberResponse
import org.store.clothstar.member.service.AddressService

@Tag(name = "Member")
@Controller
class AddressController(
    private val addressService: AddressService
) {
    private val log = KotlinLogging.logger {}

    @Operation(summary = "회원 배송지 전체 리스트 조회", description = "회원 한명에 대한 배송지를 전부 가져온다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "회원 한명에 대한 배송지 전체 조회 성공",
            content = [Content(schema = Schema(implementation = MemberResponse::class))]
        ), ApiResponse(
            responseCode = "404",
            description = "회원 정보를 찾을 수 없습니다.",
            content = [Content(schema = Schema(implementation = ErrorResponseDTO::class))]
        )]
    )
    @GetMapping("/v1/members/addresses/{id}")
    fun getMemberAllAddressgetMemberAllAddress(@PathVariable("id") memberId: Long): ResponseEntity<List<AddressResponse>> {
        val memberList: List<AddressResponse> = addressService.getMemberAllAddress(memberId)
        return ResponseEntity.ok(memberList)
    }

    @Operation(summary = "회원 배송지 저장", description = "회원 한명에 대한 배송지를 저장한다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "201",
            description = "주소가 정상적으로 저장 되었습니다.",
            content = [Content(schema = Schema(implementation = MemberResponse::class))]
        ), ApiResponse(
            responseCode = "404",
            description = "회원 정보를 찾을 수 없습니다.",
            content = [Content(schema = Schema(implementation = ErrorResponseDTO::class))]
        )]
    )
    @PostMapping("/v1/members/addresses/{id}")
    fun addrSave(
        @Validated @RequestBody createAddressRequest: CreateAddressRequest,
        @PathVariable("id") memberId: Long
    ): ResponseEntity<MessageDTO> {
        log.info { "회원 배송지 저장 요청 데이터 : ${createAddressRequest.toString()}" }

        addressService.addrSave(memberId, createAddressRequest)

        val messageDTO = MessageDTO(
            HttpStatus.CREATED.value(),
            "주소가 정상적으로 저장 되었습니다."
        )

        return ResponseEntity(messageDTO, HttpStatus.CREATED)
    }
}