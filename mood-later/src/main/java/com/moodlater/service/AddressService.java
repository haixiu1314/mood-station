package com.moodlater.service;

import com.moodlater.entity.Address;
import com.moodlater.mapper.AddressMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Transactional
    public void addAddress(Address address) {
        if (address.getIsDefault() == 1) {
            addressMapper.cancelDefault(address.getUserId());
        }
        addressMapper.insert(address);
    }

    public List<Address> getUserAddresses(Long userId) {
        return addressMapper.selectByUserId(userId);
    }

    public Address getAddressById(Long id) {
        return addressMapper.selectById(id);
    }

    public Address getDefaultAddress(Long userId) {
        return addressMapper.selectDefaultByUserId(userId);
    }

    @Transactional
    public void updateAddress(Address address) {
        if (address.getIsDefault() == 1) {
            addressMapper.cancelDefault(address.getUserId());
        }
        addressMapper.updateById(address);
    }

    @Transactional
    public void deleteAddress(Long userId, Long id) {
        Address address = addressMapper.selectById(id);
        if (address != null && address.getUserId().equals(userId)) {
            addressMapper.deleteById(id);
        }
    }

    @Transactional
    public void setDefaultAddress(Long userId, Long id) {
        Address address = addressMapper.selectById(id);
        if (address != null && address.getUserId().equals(userId)) {
            addressMapper.cancelDefault(userId);
            addressMapper.setDefault(id);
        }
    }
}
