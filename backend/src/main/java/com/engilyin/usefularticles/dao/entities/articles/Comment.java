package com.engilyin.usefularticles.dao.entities.articles;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

public class Comment {
	
	@Id
	long id;
	
    long articleId;
    
    long authorId;
    
    String description;
    
    String attachment;
    
    @CreatedDate
    Instant created;

}
