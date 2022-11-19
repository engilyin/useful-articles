package com.engilyin.usefularticles.dao.entities.articles;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

public class Comment {
	
	@Id
	long commentId;
	
    long articleId;
    
    long authorId;
    
    String commentDescription;
    
    String commentAttachment;
    
    @CreatedDate
    Instant commentCreated;

}
