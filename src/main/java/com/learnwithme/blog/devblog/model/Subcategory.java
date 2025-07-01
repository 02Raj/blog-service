package com.learnwithme.blog.devblog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Subcategory {

    private String name;

    private String slug;
}
