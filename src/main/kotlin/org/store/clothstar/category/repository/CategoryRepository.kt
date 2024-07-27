package org.store.clothstar.category.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.store.clothstar.category.domain.Category

@Repository
interface CategoryJpaRepository : JpaRepository<Category, Long>