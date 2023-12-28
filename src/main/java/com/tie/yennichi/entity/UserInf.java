package com.tie.yennichi.entity;

public interface UserInf {

    Long getUserId();

    String getUsername();
    
    String getName();
    
    void setUserId(Long id);

    void setUsername(String username);
    
    void setName(String name);
    
    void setPassword(String password);
}