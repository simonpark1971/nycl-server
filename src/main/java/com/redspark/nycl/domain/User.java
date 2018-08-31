package com.redspark.nycl.domain;

import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {

  private boolean enabled;
  private String username;
  private String emailAddress;


}
