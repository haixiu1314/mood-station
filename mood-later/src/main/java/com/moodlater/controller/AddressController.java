package com.moodlater.controller;

import com.moodlater.common.Result;
import com.moodlater.entity.Address;
import com.moodlater.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/add")
    public Result<Void> addAddress(@RequestBody Address address, @RequestAttribute("userId") Long userId) {
        address.setUserId(userId);
        addressService.addAddress(address);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<Address>> getAddressList(@RequestAttribute("userId") Long userId) {
        List<Address> list = addressService.getUserAddresses(userId);
        return Result.success(list);
    }

    @GetMapping("/default")
    public Result<Address> getDefaultAddress(@RequestAttribute("userId") Long userId) {
        Address address = addressService.getDefaultAddress(userId);
        return Result.success(address);
    }

    @GetMapping("/{id}")
    public Result<Address> getAddress(@PathVariable Long id) {
        Address address = addressService.getAddressById(id);
        return Result.success(address);
    }

    @PutMapping("/update")
    public Result<Void> updateAddress(@RequestBody Address address, @RequestAttribute("userId") Long userId) {
        address.setUserId(userId);
        addressService.updateAddress(address);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result<Void> deleteAddress(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        addressService.deleteAddress(userId, id);
        return Result.success();
    }

    @PutMapping("/setDefault/{id}")
    public Result<Void> setDefaultAddress(@PathVariable Long id, @RequestAttribute("userId") Long userId) {
        addressService.setDefaultAddress(userId, id);
        return Result.success();
    }
}
