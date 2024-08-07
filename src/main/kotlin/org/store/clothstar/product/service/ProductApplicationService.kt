package org.store.clothstar.product.service

import org.springframework.stereotype.Service
import org.store.clothstar.category.service.CategoryService
import org.store.clothstar.member.domain.Member
import org.store.clothstar.product.domain.entity.Product
import org.store.clothstar.product.dto.request.ProductCreateRequest
import org.store.clothstar.product.repository.ProductOptionRepository

@Service
class ProductApplicationService(
    private val productService: ProductService,
    private val optionService: ProductOptionService,
    private val itemService: ItemService,
    private val categoryService: CategoryService,
    private val productOption: ProductOptionRepository,
) {
    fun createProduct(val productCreateRequest: ProductCreateRequest) {
        // get current member (principal)
        val memberId: Long = 1  // currentMember.getId()
        val member: Member = memberService.getMember(memberId)
        // create product
        var product: Product = productService.createProduct(productCreateRequest.toEntity())
        // create product option
        optionService.createProductOption(product)
        // optionValue 에

        // create option Value

        // create Item

    }
}