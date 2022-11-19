package com.engilyin.usefularticles.dao.mappers;

import java.time.Instant;

import com.engilyin.usefularticles.util.Util;


public interface BaseMapper {

    default Instant map(String dateValue) {
        return Util.instantFromString(dateValue);
    }
	
}
