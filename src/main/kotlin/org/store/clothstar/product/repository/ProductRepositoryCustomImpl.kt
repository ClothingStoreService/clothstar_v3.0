package org.store.clothstar.product.repository

import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.*
import org.springframework.data.support.PageableExecutionUtils
import org.springframework.stereotype.Component
import org.store.clothstar.product.domain.Product
import org.store.clothstar.product.domain.QProduct.product
import org.store.clothstar.product.domain.type.DisplayStatus
import java.util.function.LongSupplier

@Component
class ProductRepositoryCustomImpl(
    private val queryFactory: JPAQueryFactory,
) : ProductRepositoryCustom {

    override fun findAllOffsetPaging(pageable: Pageable, keyword: String?): Page<Product> {
        val content: MutableList<Product> = getProductLines(pageable, keyword)

        var hasNext = false
        if (content.size > pageable.pageSize) {
            content.removeAt(content.size - 1)
            hasNext = true
        }

        val totalCount: JPAQuery<Long> = queryFactory
            .select(product.countDistinct())
            .from(product)
            .where(
                product.deletedAt.isNull()
                    .and(getSearchCondition(keyword))
            )

        return PageableExecutionUtils.getPage(content, pageable) { totalCount.fetchOne()!! }
    }

    override fun findAllSlicePaging(pageable: Pageable, keyword: String?): Slice<Product> {
        val content: MutableList<Product> = getProductLines(pageable, keyword)

        var hasNext = false
        if (content.size > pageable.pageSize) {
            content.removeAt(content.size - 1)
            hasNext = true
        }

        return SliceImpl(content, pageable, hasNext)
    }

    private fun getProductLines(pageable: Pageable, keyword: String?): MutableList<Product> {
        val orderSpecifiers = getOrderSpecifiers(pageable.sort)

        // 카테고리별로 ProductLine 엔티티를 가져옴
        return queryFactory
            .selectDistinct(product)
            .from(product)
            .where(
                product.deletedAt.isNull()
                    .and(product.displayStatus.eq(DisplayStatus.VISIBLE))
                    .and(getSearchCondition(keyword))
            )
            .orderBy(*orderSpecifiers.toTypedArray())  // * -> 스프레드 연산자 : 배열의 각 요소를 개별 인자로 전달
            .offset(pageable.offset)
            .limit((pageable.pageSize + 1).toLong())
            .fetch()
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
                    .and(product.displayStatus.eq(DisplayStatus.VISIBLE))
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
        val orderSpecifiers = getOrderSpecifiers(pageable.sort)

        // 카테고리별로 Product 엔티티를 가져옴
        return queryFactory
            .selectDistinct(product)
            .from(product)
            .where(
                product.categoryId.eq(categoryId)
                    .and(product.deletedAt.isNull())
                    .and(product.displayStatus.eq(DisplayStatus.VISIBLE))
                    .and(getSearchCondition(keyword))
            )
            .orderBy(*orderSpecifiers.toTypedArray())
            .offset(pageable.offset)
            .limit((pageable.pageSize + 1).toLong())
            .fetch()
    }

    private fun getSearchCondition(keyword: String?): BooleanExpression {
        if (keyword.isNullOrEmpty()) {
            return product.isNotNull() // 조건이 없을 경우 항상 true를 반환
        }
        return product.name.containsIgnoreCase(keyword)
            .or(product.content.containsIgnoreCase(keyword))
    }

    private fun getOrderSpecifiers(sort: Sort?): List<OrderSpecifier<*>> {
        val orderSpecifiers = mutableListOf<OrderSpecifier<*>>()

        sort?.forEach { order ->
            val orderSpecifier = when (order.property) {
                "saleCount" -> if (order.isAscending) product.saleCount.asc() else product.saleCount.desc()
                "createdAt" -> if (order.isAscending) product.createdAt.asc() else product.createdAt.desc()
                "price" -> if (order.isAscending) product.price.asc() else product.price.desc()
                else -> null
            }
            orderSpecifier?.let { orderSpecifiers.add(it) }
        }

        return orderSpecifiers
    }
}