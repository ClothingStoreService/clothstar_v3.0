package org.store.clothstar.product.repository

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Component
import org.store.clothstar.product.domain.Product

@Component
class ProductRepositoryCustomImpl(
    private val jpaQueryFactory: JPAQueryFactory,
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
        keyword: String
    ): Page<Product> {
//        val content = getProductLineEntitiesByCategory(categoryId, pageable, keyword)
//
//        var hasNext = false
//        if (content.size > pageable.pageSize) {
//            content.removeAt(content.size - 1)
//            hasNext = true
//        }
//
//        val totalCount: JPAQuery<Long> = jpaQueryFactory
//            .select(qProductLine.countDistinct())
//            .from(qProductLine)
//            .where(
//                qProductLine.category.categoryId.eq(categoryId)
//                    .and(qProductLine.deletedAt.isNull())
//                    .and(getSearchCondition(keyword))
//            )
//
//        return PageableExecutionUtils.getPage(content, pageable, LongSupplier { totalCount.fetchOne()!! })
        TODO("Not yet implemented")

    }

    override fun findEntitiesByCategoryWithSlicePaging(
        categoryId: Long,
        pageable: Pageable,
        keyword: String
    ): Slice<Product> {
        TODO("Not yet implemented")
    }

    private fun getProductLineEntitiesByCategory(
        categoryId: Long,
        pageable: Pageable,
        keyword: String
    ): List<Product> {
        TODO("Not yet implemented")

//        val orderSpecifiers: List<OrderSpecifier<*>> = getOrderSpecifiers(pageable.sort)
//
//        // 카테고리별로 ProductLine 엔티티를 가져옴
//        return jpaQueryFactory
//            .selectDistinct<Any>(qProductLine)
//            .from(qProductLine)
//            .where(
//                qProductLine.category.categoryId.eq(categoryId)
//                    .and(qProductLine.deletedAt.isNull())
//                    .and(getSearchCondition(keyword))
//            )
//            .orderBy(orderSpecifiers.toTypedArray<T>())
//            .offset(pageable.offset)
//            .limit((pageable.pageSize + 1).toLong())
//            .fetch()
    }
}