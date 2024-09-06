package org.store.clothstar.payment.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.store.clothstar.payment.domain.Payment

interface PaymentRepository : JpaRepository<Payment, Long> {
}