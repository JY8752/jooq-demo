package com.example.jooqdemo.domain.repository

import com.example.jooqdemo.domain.model.Author

interface AuthorRepository {
    fun findById(id: Int): Author?
    fun findAll(): List<Author>
    fun save(firstName: String, lastName: String): Author
    fun deleteAll()
}
