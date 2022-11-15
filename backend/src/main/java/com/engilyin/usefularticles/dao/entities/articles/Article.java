package com.engilyin.usefularticles.dao.entities.articles;

import java.time.Instant;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.engilyin.usefularticles.dao.entities.users.User;

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
    String articleId;
    
    User author;
    
    String description;
    
    String attachment;
    
    @CreatedDate
    Instant created;

}
