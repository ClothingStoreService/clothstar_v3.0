package org.store.clothstar.product.service

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import org.springframework.web.multipart.MultipartFile
import org.store.clothstar.member.service.MemberService
import org.store.clothstar.product.domain.Item
import org.store.clothstar.product.domain.Product
import org.store.clothstar.product.domain.type.DisplayStatus
import org.store.clothstar.product.domain.type.SaleStatus
import org.store.clothstar.product.dto.request.ProductCreateRequest

@ExtendWith(MockitoExtension::class)
class ProductApplicationServiceTest {

    @Mock
    private lateinit var productService: ProductService

    @Mock
    private lateinit var productOptionService: ProductOptionService

    @Mock
    private lateinit var itemService: ItemService

    @Mock
    private lateinit var memberService: MemberService

    @InjectMocks
    private lateinit var productApplicationService: ProductApplicationService

    @Disabled
    @DisplayName("유효한 ProductCreateRequest가 들어오면 상품 생성에 성공한다.")
    @Test
    fun givenValidProductCreateRequest_whenCreateProduct_ThenProductCreated() {
        // given
        val productCreateRequest = ProductCreateRequest(
            memberId = 1L,
            categoryId = 2L,
            name = "오구 슬리퍼",
            content = "푹신 푹신한 귀여운 오구 슬리퍼",
            price = 19900,
            displayStatus = DisplayStatus.VISIBLE,
            saleStatus = SaleStatus.ON_SALE,
            productOptions = listOf(),
            items = listOf()
        )

        val product = productCreateRequest.toProductEntity()

        whenever(productService.createProduct(any())).thenReturn(product)

        // when
        productApplicationService.createProduct(mock(MultipartFile::class.java), null, productCreateRequest)

        // then
        verify(productService, times(1)).createProduct(any(Product::class.java))
        verify(product, times(1)).updateDisplayStatus(eq(DisplayStatus.VISIBLE))
    }

    @DisplayName("유효한 productId와 displayStatus가 주어지면 상품의 진열 상태 변경에 성공한다.")
    @Test
    fun givenValidProductIdAndDsplayStatus_whenUpdateProductDisplayStatusThenProductDisplayStatusUpdated() {
        // given
        val productId: Long = 1
        val displayStatus = DisplayStatus.HIDDEN
        val mockProduct = mock(Product::class.java)

        given(productService.getProductById(productId)).willReturn(mockProduct)

        // when
        productApplicationService.updateProductDisplayStatus(productId, displayStatus)

        // then
        verify(productService).getProductById(productId)
        verify(mockProduct).updateDisplayStatus(displayStatus)
    }

    @DisplayName("유효한 productId, itemId, displayStatus가 주어지면 아이템의 진열 상태 변경에 성공한다.")
    @Test
    fun givenValidProductIdItemIdAndDisplayStatus_whenUpdateItemDisplayStatus_thenItemDisplayStatusUpdated() {
        // given
        val productId: Long = 1
        val itemId: Long = 2
        val displayStatus = DisplayStatus.VISIBLE
        val mockItem = mock(Item::class.java)

        given(itemService.getItemByIdAndProductId(itemId, productId)).willReturn(mockItem)

        // when
        productApplicationService.updateItemDisplayStatus(productId, itemId, displayStatus)

        // then
        verify(itemService).getItemByIdAndProductId(itemId, productId)
        verify(mockItem).updateDisplayStatus(displayStatus)
    }
}