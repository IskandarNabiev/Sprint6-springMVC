package ru.sber.service

import ru.sber.model.AddressBook
import java.util.concurrent.ConcurrentHashMap

interface AddressBookService {

    fun getAddress(id: Int): AddressBook?

    fun addAddress(address: AddressBook) : AddressBook?

    fun getAllAddress(query: Map<String, String>): ConcurrentHashMap<Int, AddressBook>

    fun updateAddress(id: Int, address: AddressBook): AddressBook?

    fun deleteAddress(id: Int)
}