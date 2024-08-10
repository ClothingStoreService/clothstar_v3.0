package org.store.clothstar.product.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import org.store.clothstar.member.domain.Member
import org.store.clothstar.member.domain.vo.MemberShoppingActivity
import org.store.clothstar.member.service.MemberService
import org.store.clothstar.product.domain.Product
import org.store.clothstar.product.domain.ProductImage
import org.store.clothstar.product.domain.type.ImageType
import org.store.clothstar.product.dto.request.ProductCreateRequest
import org.store.clothstar.product.dto.response.ProductResponse

@Service
class ProductApplicationService(
    private val productService: ProductService,
    private val productOptionService: ProductOptionService,
    private val itemService: ItemService,
    private val memberService: MemberService
) {
    @Transactional
    fun createProduct(
        mainImage: MultipartFile,
        subImages: List<MultipartFile>?,
        productCreateRequest: ProductCreateRequest
    ) {
        // get current member (principal)
        val memberId: Long = 1  // currentMember.getId()
//        val member: Member = memberService.getMemberByMemberId(memberId)
        val member = Member(memberId, "010-1234-5678", "Ogu", MemberShoppingActivity.init())

        // 1. 상품 생성
        val product = productCreateRequest.toProductEntity()

        // 상품 저장
        val savedProduct = productService.createProduct(product)

        // S3에 이미지 업로드 및 URL 생성
        // 2. 메인 이미지 업로드 및 저장
//        val mainImageUrl = s3Service.uploadFile(mainImage)
//        val mainImageUrl = s3Service.uploadFile(mainImage)
        val mainImageUrl =
            "https://on.com2us.com/wp-content/uploads/2023/12/VxdEKDNZCp9hAW5TU5-3MZTePLGSdlYKzEZUyVLDB-Cyo950Ee19yaOL8ayxgJzEfMYfzfLcRYuwkmKEs2cg0w.webp"
        product.imageList.add(ProductImage(mainImageUrl, mainImageUrl, ImageType.MAIN))

        // 3. 서브 이미지 업로드 및® 저장
        subImages?.forEach { subImage ->
//            val subImageUrl = s3Service.uploadFile(subImage)
            val subImageUrl =
                "https://on.com2us.com/wp-content/uploads/2023/12/%EC%98%A4%EA%B5%AC%EC%99%80-%EB%B9%84%EB%B0%80%EC%9D%98%EC%88%B2-002.jpg"
            product.imageList.add(ProductImage(subImageUrl, subImageUrl, ImageType.SUB))
        }


        // 4. 상품 옵션 생성
        val productOptions = productCreateRequest.productOptions.map { optionRequest ->
            val productOption = productOptionService.createProductOption(product, optionRequest)
            product.productOptions.add(productOption)
        }

        // 5. 상품 아이템 생성
        productCreateRequest.items.forEach { itemRequest ->
            val item = itemService.createItem(product, itemRequest)
            product.items.add(item)
        }
    }

    @Transactional(readOnly = true)
    fun getProductDetails(productId: Long): ProductResponse {

        return productService.getProductDetails(productId)
    }
}