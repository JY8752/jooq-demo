package com.example.jooqdemo.domain

import com.example.jooqdemo.domain.repository.AuthorRepository
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.flywaydb.core.Flyway
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.MySQLContainer

@SpringBootTest
internal class AuthorRepositoryTest(
    private val authorRepository: AuthorRepository
) : FunSpec({
    beforeSpec {
        Flyway.configure()
            .dataSource(container.jdbcUrl, container.username, container.password)
            .load()
            .migrate()
    }
    test("test") {
        println("hello")
    }
    test("save and find") {
        val saved = authorRepository.save("first", "last")
        val find = authorRepository.findById(saved.id)
        saved shouldBe find
    }
}) {
    companion object {
        val container = MySQLContainer("mysql:latest").apply {
            withDatabaseName("library")
            withUsername("root")
            withPassword("root")
            start()
        }
    }
}
