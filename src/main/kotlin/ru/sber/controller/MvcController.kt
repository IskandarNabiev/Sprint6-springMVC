package ru.sber.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import ru.sber.model.AddressBook
import ru.sber.service.AddressBookService



@Controller
@RequestMapping("/app")
class MvcController {

    @Autowired
    lateinit var addressService: AddressBookService

    @GetMapping("/add")
    fun addRecord(addressBook: AddressBook): String {
        return "add-form"
    }

    @PostMapping("/add")
    fun addAddress(model: Model, @ModelAttribute("address") addressBook: AddressBook): String {
        addressService.addAddress(addressBook)
        return "add-address"
    }

    @GetMapping("/list")
    fun getAddressList(model: Model, @RequestParam (required = false)  allAddress: Map<String, String>): String {
        model.addAttribute("list", addressService.getAllAddress(allAddress))
        return "show-addresses"
    }


    @GetMapping("/{id}/view")
    fun getAddress(@PathVariable("id") id: Int, model: Model): String {
        model.addAttribute("address", addressService.getAddress(id))
        return "show-address"
    }


    @PostMapping("/{id}/edit")
    fun updateAddress(@ModelAttribute("address") addressBook: AddressBook,
                      @PathVariable("id") id: Int, model: Model): String {
        model.addAttribute("address", addressService.updateAddress(id, addressBook))
        return "edit-address"
    }


    @GetMapping("/{id}/delete")
    fun deleteAddress(@PathVariable("id") id: Int, model: Model): String {
        model.addAttribute("address", addressService.getAddress(id))
        addressService.deleteAddress(id)
        return "delete-address"
    }




}