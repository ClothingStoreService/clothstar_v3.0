package org.store.clothstar.member.authentication.service

interface SignUpService<T> {
    fun signUp(request: T): Long
}