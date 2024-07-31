package org.store.clothstar.order.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.transaction.annotation.Transactional
import org.store.clothstar.order.domain.Order

interface OrderSellerRepository : JpaRepository<Order, Long> {
}
