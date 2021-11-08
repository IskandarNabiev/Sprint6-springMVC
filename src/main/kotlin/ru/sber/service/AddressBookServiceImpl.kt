package ru.sber.service

import org.springframework.stereotype.Service
import ru.sber.model.AddressBook
import java.util.concurrent.ConcurrentHashMap


@Service
class AddressBookServiceImpl : AddressBookService {

    private var addressBook = ConcurrentHashMap<Int, AddressBook>()
    private var index = 0


    override fun getAddress(id: Int): AddressBook? {
        return addressBook.get(id)
    }

    override fun addAddress(address: AddressBook): AddressBook? {
        return addressBook.putIfAbsent(index++, address)
    }

    override fun getAllAddress(query: Map<String, String>): ConcurrentHashMap<Int, AddressBook> {
        val result = ConcurrentHashMap<Int, AddressBook>()
        for ((key, value) in addressBook) {
            result[key] = value
        }
        return result
    }

    override fun updateAddress(id: Int, address: AddressBook): AddressBook? {
        return addressBook[id]
    }

    override fun deleteAddress(id: Int) {
        addressBook.remove(id)
    }
}


