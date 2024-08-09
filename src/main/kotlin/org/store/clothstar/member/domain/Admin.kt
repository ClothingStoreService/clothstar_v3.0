package org.store.clothstar.member.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity(name = "admin")
class Admin(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var adminId: Long,
    private val name: String,
)