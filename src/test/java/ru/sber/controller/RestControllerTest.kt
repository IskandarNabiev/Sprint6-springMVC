package ru.sber.controller

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import ru.sber.model.AddressBook
import ru.sber.service.AddressBookService
import java.util.concurrent.ConcurrentHashMap

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestControllerTest {

    @LocalServerPort
    private var port: Int = 0

    @Autowired
    private lateinit var restTemplate: TestRestTemplate

    @Autowired
    private lateinit var addressService: AddressBookService


    private val headers = HttpHeaders()


    @BeforeEach
    fun setUp() {

        headers.add("Cookie", login())
        addressService.addAddress(AddressBook("Ivan", "Ivanov", "dom 3", "8934552312"))
        addressService.addAddress(AddressBook("Vadim", "Ivanov", "Lomonosov st.", "897733454765"))
        addressService.addAddress(AddressBook("Alex", "Markov", "SouthEast", "89346547312"))
    }

    private fun login(): String? {
        val request: MultiValueMap<String, String> = LinkedMultiValueMap()
        request.set("username", "admin")
        request.set("password", "adminpass")

        val response = restTemplate.postForEntity(url("login"), HttpEntity(request, HttpHeaders()), String::class.java)

        return response!!.headers["Set-Cookie"]!![0]
    }

    fun url(s: String): String {
        return "http://localhost:${port}/${s}"
    }

    @Test
    fun addAddress() {

        val result = restTemplate.exchange(
            url("api/add"),
            HttpMethod.POST,
            HttpEntity<AddressBook>(AddressBook("Miguel", "Antonio", "Sky 2", "346756867"), headers),
            AddressBook::class.java
        )
        assertEquals(HttpStatus.CREATED, result.statusCode)
        assertNotNull(result.body)

    }

    @Test
    fun getAddressList() {
        val result = restTemplate.exchange(
            url("api/list"),
            HttpMethod.GET,
            HttpEntity(emptyMap<String, String>(), headers),
            ConcurrentHashMap::class.java
        )
        assertEquals(HttpStatus.OK, result.statusCode)
        assertNotNull(result.body)
    }

    @Test
    fun getAddress() {
        val result  = restTemplate.exchange(
            url("api/0/view"),
            HttpMethod.GET,
            HttpEntity<AddressBook>(null, headers),
            AddressBook::class.java)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertEquals("Ivan", result.body!!.firstName)
    }

    @Test
    fun updateAddress() {
        val result = restTemplate.exchange(
            url("api/1/edit"),
            HttpMethod.PUT,
            HttpEntity<AddressBook>(AddressBook("IvanSecond", "Ivanov", "dom 3", "8934552312"), headers),
            String::class.java
        )
        assertEquals("UPDATED", result.body)

    }

    @Test
    fun deleteAddress() {
        restTemplate.exchange(
            url("api/1/delete"),
            HttpMethod.DELETE,
            HttpEntity(null, headers),
            String::class.java
        )
        assertEquals(null, addressService.getAddress(1))
    }
}