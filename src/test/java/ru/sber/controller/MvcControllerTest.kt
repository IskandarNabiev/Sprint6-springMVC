package ru.sber.controller

import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import ru.sber.model.AddressBook
import ru.sber.service.AddressBookService

@SpringBootTest
@AutoConfigureMockMvc
class MvcControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var addressService: AddressBookService

    @BeforeEach
    fun setUp() {
        addressService.addAddress(AddressBook("Ivan", "Ivanov", "dom 3", "8934552312"))
        addressService.addAddress(AddressBook("Vadim", "Ivanov", "Lomonosov st.", "897733454765"))
        addressService.addAddress(AddressBook("Alex", "Markov", "SouthEast", "89346547312"))
    }

    @Test
    fun addAddress() {
        mockMvc.perform(post("/app/add")
            .param("firstName", "Alise")
            .param("lastName", "Alise")
            .param("address", "Push st.")
            .param("phone", "34677658"))
            .andExpect(status().isOk)
            .andExpect(model().attributeExists("address"))
            .andExpect(view().name("add-address"))
            .andExpect(content().string(containsString("Alise")))
    }

    @Test
    fun getAllAddresses() {
        mockMvc.perform(get("/app/list"))
            .andExpect(status().isOk)
            .andExpect(view().name("show-addresses"))
            .andExpect(model().attributeExists("list"))
            .andExpect(content().string(containsString("SouthEast")))
    }

    @Test
    fun getAddress() {
        mockMvc.perform(get("/app/1/view"))
            .andExpect(status().isOk)
            .andExpect(view().name("show-address"))
            .andExpect(model().attributeExists("address"))
            .andExpect(content().string(containsString("Vadim")))
    }

    @Test
    fun updateAddress() {
        mockMvc.perform(post("/app/1/edit")
            .param("firstName", "Ivan")
            .param("lastName", "Ivanov")
            .param("address", "Pokrovskii st.")
            .param("phone", "8934552312"))
            .andExpect(status().isOk)
            .andExpect(view().name("edit-address"))

    }

    @Test
    fun deleteAddress() {
        mockMvc.perform(get("/app/2/delete"))
            .andExpect(status().isOk)
            .andExpect(view().name("delete-address"))

    }
}