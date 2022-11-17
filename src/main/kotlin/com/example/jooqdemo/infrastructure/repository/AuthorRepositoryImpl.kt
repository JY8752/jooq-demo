package com.example.jooqdemo.infrastructure.repository

import com.example.jooqdemo.domain.model.Author
import com.example.jooqdemo.domain.repository.AuthorRepository
import com.example.ktknowledgeTodo.infra.jooq.tables.Author.Companion.AUTHOR
import org.jooq.DSLContext
import org.jooq.Record
import org.springframework.stereotype.Repository

@Repository
class AuthorRepositoryImpl(
    private val dslContext: DSLContext
) : AuthorRepository {
    override fun findById(id: Int): Author? {
        return this.dslContext.select()
            .from(AUTHOR)
            .where(AUTHOR.ID.eq(id))
            .fetchOne()?.let { toModel(it) }
    }

    override fun findAll(): List<Author> {
        return this.dslContext.select()
            .from(AUTHOR)
            .fetch().map { toModel(it) }
    }

    override fun save(firstName: String, lastName: String): Author {
        val record = this.dslContext.newRecord(AUTHOR).also {
            it.firstName = firstName
            it.lastName = lastName
            it.store()
        }
        return Author(record.id!!, record.firstName!!, record.lastName!!)
    }

    override fun deleteAll() {
        this.dslContext.deleteFrom(AUTHOR).execute()
    }

    private fun toModel(record: Record) = Author(
        record.getValue(AUTHOR.ID)!!,
        record.getValue(AUTHOR.FIRST_NAME)!!,
        record.getValue(AUTHOR.LAST_NAME)!!
    )
}
