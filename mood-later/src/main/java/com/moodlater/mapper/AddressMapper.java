package com.moodlater.mapper;

import com.moodlater.entity.Address;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface AddressMapper {

    int insert(Address address);

    List<Address> selectByUserId(@Param("userId") Long userId);

    Address selectById(@Param("id") Long id);

    Address selectDefaultByUserId(@Param("userId") Long userId);

    int updateById(Address address);

    int deleteById(@Param("id") Long id);

    int setDefault(@Param("id") Long id);

    int cancelDefault(@Param("userId") Long userId);
}
