package com.engilyin.usefularticles.dao.mappers;

import java.util.Map;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.engilyin.usefularticles.dao.entities.users.User;

@Mapper
@Component
public interface UserMapper extends BaseMapper {

	User fromMap(Map<String, String> source);
	
}
