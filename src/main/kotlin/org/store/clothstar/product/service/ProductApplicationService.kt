package org.store.clothstar.product.service

import org.springframework.stereotype.Service
import org.store.clothstar.category.service.CategoryService
import org.store.clothstar.member.domain.Member
import org.store.clothstar.member.service.MemberService
import org.store.clothstar.product.domain.ProductImage
import org.store.clothstar.product.dto.request.ProductCreateRequest

@Service
class ProductApplicationService(
    private val productService: ProductService,
    private val productOptionService: ProductOptionService,
    private val optionValueService: OptionValueService,
    private val itemService: ItemService,
    private val categoryService: CategoryService,
    private val memberService: MemberService
) {
    fun createProduct(productCreateRequest: ProductCreateRequest) {
        // get current member (principal)
        val memberId: Long = 1  // currentMember.getId()
        val member: Member = memberService.getMemberByMemberId(memberId)

        val product = productCreateRequest.toProductEntity()

        // S3에 이미지 업로드 및 URL 생성
        val productImages = productCreateRequest.imageList.map { request ->
//            val urls = s3Service.uploadFile(request.multipartFile)
            ProductImage(
                url = "https://play-lh.googleusercontent.com/UdBg9MSQgbZS4IJ7VJxdtMBgp2rLbh5fSWX6Aswrj6qgmuwZO2DIgjy_8nvM2gmlq00/",//urls.url
                originUrl = "https://play-lh.googleusercontent.com/UdBg9MSQgbZS4IJ7VJxdtMBgp2rLbh5fSWX6Aswrj6qgmuwZO2DIgjy_8nvM2gmlq00", //urls.originUrl,
                pdpThumbnailUrl = "https://play-lh.googleusercontent.com/UdBg9MSQgbZS4IJ7VJxdtMBgp2rLbh5fSWX6Aswrj6qgmuwZO2DIgjy_8nvM2gmlq00", //urls.pdpThumbnailUrl,
                pdpStaticImageUrl = "https://play-lh.googleusercontent.com/UdBg9MSQgbZS4IJ7VJxdtMBgp2rLbh5fSWX6Aswrj6qgmuwZO2DIgjy_8nvM2gmlq00", //urls.pdpStaticImageUrl,
                imageType = request.imageType
            )
        }

        // 상품 이미지 설정
        product.imageList = productImages.toMutableList()

        // 상품 저장
        val savedProduct = productService.createProduct(product)

        // 옵션 생성
        val productOptions = productCreateRequest.productOptions.map { optionRequest ->
            productOptionService.createProductOption(savedProduct, optionRequest)
        }

        // 아이템 생성
        productCreateRequest.items.forEach { itemRequest ->
            itemService.createItem(savedProduct, itemRequest)
        }
    }

}