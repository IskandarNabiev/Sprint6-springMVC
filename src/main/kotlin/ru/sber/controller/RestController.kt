package ru.sber.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController
import ru.sber.model.AddressBook
import ru.sber.service.AddressBookService
import java.util.concurrent.ConcurrentHashMap

@RestController
@RequestMapping("/api")
class RestController {

    @Autowired
    lateinit var addressService: AddressBookService

    @PostMapping("/add")
    fun addAddress(@RequestBody address: AddressBook): ResponseEntity<AddressBook> {
        addressService.addAddress(address)
        return ResponseEntity(address, HttpStatus.CREATED)
    }

    @GetMapping("/list")
    fun getAllAddresses(@RequestParam query: Map<String, String>): ResponseEntity<ConcurrentHashMap<Int, AddressBook>> {
        val result = addressService.getAllAddress(query)
        return ResponseEntity(result, HttpStatus.OK)
    }

    @GetMapping("/{id}/view")
    fun getAddress(@PathVariable("id") id: Int): ResponseEntity<AddressBook> {
        val result = addressService.getAddress(id)
        return ResponseEntity(result, HttpStatus.OK)
    }

    @PutMapping("/{id}/edit")
    fun updateAddress(@PathVariable id: Int, @RequestBody address: AddressBook): ResponseEntity<String> {
        addressService.updateAddress(id, address)
        return ResponseEntity("UPDATED", HttpStatus.OK)
    }

    @DeleteMapping("/{id}/delete")
    fun deleteAddress(@PathVariable id: Int): ResponseEntity<String> {
        addressService.deleteAddress(id)
        return ResponseEntity("DELETED", HttpStatus.OK)
    }
}