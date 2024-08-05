package org.store.clothstar.product.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import org.store.clothstar.product.dto.response.ProductResponse
import org.store.clothstar.product.repository.ItemRepository
import org.store.clothstar.product.repository.OptionValueRepository
import org.store.clothstar.product.repository.ProductOptionRepository
import org.store.clothstar.product.repository.ProductRepository

@Service
class ProductService(
    private val productRepository: ProductRepository,
    private val productOptionRepository: ProductOptionRepository,
    private val optionValueRepository: OptionValueRepository,
    private val itemRepository: ItemRepository,
) {
    @Transactional(readOnly = true)
    fun getProduct(productId: Long): ProductResponse {
        return productRepository.findByIdOrNull(productId)?.let { product ->
            ProductResponse.from(product)
        } ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST,
            "productId :" + productId + "인 상품 옵션 정보를 찾을 수 없습니다."
        )
    }

//    @Transactional
//    fun createProduct(@Validated @RequestBody productCreateRequest: ProductCreateRequest): Long {
//
//        //option -> 색상
//        val options = productCreateRequest.productOptions
//
//
//        for (option in options) {
//            val productOption = ProductOption(
//                optionName = option.optionName,
//
//                )
//
//        }


//        val savedProductOption = productOptionRepository.save(productOption)

    //optionValue -> 빨강,파랑

    //item , item attribute 만들어야함 어떤것들 조합해서 나왔는지?

    //product

//
//        return savedProduct.getProductId()
//    }
//
//    @Transactional
//    fun updateProduct(productId: Long, updateProductRequest: UpdateProductRequest?) {
//        val product: ProductEntity = productRepository.findById(productId)
//            .orElseThrow {
//                ResponseStatusException(
//                    HttpStatus.BAD_REQUEST,
//                    "productId :" + productId + "인 상품 옵션 정보를 찾을 수 없습니다."
//                )
//            }
//
//        product.updateOption(updateProductRequest)
//    }

//    @Transactional
//    fun deleteProduct(productId: Long?) {
//        productRepository.deleteById(productId)
//    }
//
//    @Transactional
//    fun restoreProductStockByOrder(orderDetailList: List<OrderDetail?>) {
//        orderDetailList.forEach(Consumer<OrderDetail> { orderDetail: OrderDetail ->
//            val productEntity: ProductEntity = productRepository.findById(orderDetail.getProductId())
//                .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다.") }
//            productEntity.restoreStock(orderDetail.getQuantity())
//        })
//    }
//
//    fun restoreProductStockByOrderDetail(orderDetail: OrderDetail) {
//        val productEntity: ProductEntity = productRepository.findById(orderDetail.getProductId())
//            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "상품 정보를 찾을 수 없습니다.") }
//        productEntity.restoreStock(orderDetail.getQuantity())
//    }
//
//    fun findByIdIn(productIds: List<Long?>?): List<ProductEntity> {
//        return productJPARepository.findByIdIn(productIds)
//    }
}