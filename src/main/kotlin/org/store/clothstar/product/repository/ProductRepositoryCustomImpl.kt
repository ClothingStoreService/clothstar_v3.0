package org.store.clothstar.product.repository

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Component
import org.store.clothstar.product.domain.Product
import org.store.clothstar.product.domain.QProduct.product
import java.util.function.LongSupplier

@Component
class ProductRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
) : ProductRepositoryCustom {

    override fun getProductLists(pageable: Pageable?): Page<Product?>? {
        TODO("Not yet implemented")
    }

    override fun findAllOffsetPaging(pageable: Pageable): Page<Product> {
        TODO("Not yet implemented")
    }

    override fun findAllSlicePaging(pageable: Pageable): List<Product> {
        TODO("Not yet implemented")
    }

    override fun findEntitiesByCategoryWithOffsetPaging(
        categoryId: Long,
        pageable: Pageable,
        keyword: String?
    ): Page<Product> {
        val content = getProductsByCategory(categoryId, pageable, keyword)

        var hasNext = false
        if (content.size > pageable.pageSize) {
            content.removeAt(content.size - 1)
            hasNext = true
        }

        val totalCount = queryFactory
            .select(product.countDistinct())
            .from(product)
            .where(
                product.categoryId.eq(categoryId)
                    .and(product.deletedAt.isNull())
                    .and(getSearchCondition(keyword))
            )

        return PageableExecutionUtils.getPage(content, pageable, LongSupplier { totalCount.fetchOne()!! })
    }

    override fun findEntitiesByCategoryWithSlicePaging(
        categoryId: Long,
        pageable: Pageable,
        keyword: String?
    ): Slice<Product> {
        val content = getProductsByCategory(categoryId, pageable, keyword)

        var hasNext = false
        if (content.size > pageable.pageSize) {
            content.removeAt(content.size - 1)
            hasNext = true
        }

        return SliceImpl(content, pageable, hasNext)
    }

    private fun getProductsByCategory(
        categoryId: Long,
        pageable: Pageable,
        keyword: String?
    ): MutableList<Product> {
//        val orderSpecifiers = getOrderSpecifiers(pageable.sort)

        // 카테고리별로 Product 엔티티를 가져옴
        return queryFactory
            .selectDistinct(product)
            .from(product)
            .where(
                product.categoryId.eq(categoryId)
                    .and(product.deletedAt.isNull())
                    .and(getSearchCondition(keyword))
            )
//            .orderBy(orderSpecifiers.toTypedArray<T>())
            .offset(pageable.offset)
            .limit((pageable.pageSize + 1).toLong())
            .fetch()
    }

    private fun getSearchCondition(keyword: String?): BooleanExpression {
        if (keyword == null || keyword.isEmpty()) {
            return product.isNotNull() // 조건이 없을 경우 항상 true를 반환
        }
        return product.name.containsIgnoreCase(keyword)
            .or(product.content.containsIgnoreCase(keyword))
    }

//    private fun getOrderSpecifiers(sort: Sort): List<OrderSpecifier<*>> {
//        val orderSpecifiers: List<OrderSpecifier<*>> = ArrayList()
//
//        for (order in sort) {
//            when (order.property) {
//                "saleCount" -> {
//                    orderSpecifiers.add(if (order.isAscending) product.saleCount.asc() else product.saleCount.desc())
//                }
//
//                "createdAt" -> orderSpecifiers.add(if (order.isAscending) product.createdAt.asc() else product.createdAt.desc())
//                "price" -> orderSpecifiers.add(if (order.isAscending) product.price.asc() else product.price.desc())
//                else -> {}
//            }
//        }
//
//        return orderSpecifiers
//    }
}