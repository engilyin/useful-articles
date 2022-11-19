package com.engilyin.usefularticles.dao.entities.articles;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table("articles")
public class Article {
	
    @Id
    long articleId;
    
    String articleName;
    
    long authorId;
    
    String articleDescription;
    
    String articleAttachment;
    
    @CreatedDate
    Instant articleCreated;

}
