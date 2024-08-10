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
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.store.clothstar.common.dto.ErrorResponseDTO
import org.store.clothstar.common.dto.MessageDTO
import org.store.clothstar.member.application.SellerServiceApplication
import org.store.clothstar.member.dto.request.CreateSellerRequest
import org.store.clothstar.member.dto.response.MemberResponse
import org.store.clothstar.member.dto.response.SellerResponse

@Tag(name = "Seller", description = "판매자 정보 관리에 대한 API 입니다.")
@RestController
class SellerController(
    private val sellerServiceApplication: SellerServiceApplication,
) {
    private val log = KotlinLogging.logger {}

    @Operation(summary = "판매자 상세정보 조회", description = "판매자 한 명에 대한 상세정보를 가져온다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "판매자 상세정보 조회 성공",
            content = [Content(schema = Schema(implementation = MemberResponse::class))]
        ), ApiResponse(
            responseCode = "404",
            description = "회원 정보를 찾을 수 없습니다.",
            content = [Content(schema = Schema(implementation = ErrorResponseDTO::class))]
        )]
    )
    @GetMapping("/v1/sellers/{id}")
    fun getSeller(@PathVariable("id") memberId: Long): ResponseEntity<SellerResponse> {
        val seller = sellerServiceApplication.getSellerById(memberId)

        val sellerResponse = SellerResponse(
            memberId = seller.memberId,
            brandName = seller.brandName,
            bizNo = seller.bizNo,
            totalSellPrice = seller.totalSellPrice,
            createdAt = seller.createdAt,
        )

        return ResponseEntity.ok(sellerResponse)
    }

    @Operation(summary = "판매자 가입", description = "회원이 판매자를 신청한다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "201",
            description = "판매자 신청이 정상적으로 되었습니다.",
            content = [Content(schema = Schema(implementation = MemberResponse::class))]
        ), ApiResponse(
            responseCode = "404",
            description = """
                            회원 정보를 찾을 수 없습니다. OR 이미 판매자 가입이 되어 있습니다.
                            OR 이미 존재하는 사업자 번호 입니다. OR 이미 존재하는 브랜드 이름 입니다.
                            """,
            content = [Content(schema = Schema(implementation = ErrorResponseDTO::class))]
        )]
    )
    @PostMapping("/v1/sellers/{id}")
    fun saveSeller(
        @Validated @RequestBody createSellerRequest: CreateSellerRequest,
        @PathVariable("id") memberId: Long
    ): ResponseEntity<MessageDTO> {
        log.info { "판매자 가입 요청 데이터 : ${createSellerRequest.toString()}" }
        sellerServiceApplication.sellerSave(memberId, createSellerRequest)

        val messageDTO = MessageDTO(
            HttpStatus.CREATED.value(),
            "판매자 신청이 정상적으로 되었습니다."
        )

        return ResponseEntity(messageDTO, HttpStatus.CREATED)
    }
}