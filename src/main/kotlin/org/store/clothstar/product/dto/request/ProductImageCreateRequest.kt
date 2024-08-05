package org.store.clothstar.product.dto.request

import org.springframework.web.multipart.MultipartFile

class ProductImageCreateRequest (
    val multipartFile: MultipartFile
)