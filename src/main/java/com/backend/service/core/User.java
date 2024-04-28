package com.backend.service.core;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.UUID;

@lombok.Data
@Document
public class User {

    @Id
    private String id = UUID.randomUUID().toString();
    public String username;
    public String firstName;
    public String lastName;
    public String middleName;
    public int age;
    @Indexed(unique = true)
    private String password;
}
