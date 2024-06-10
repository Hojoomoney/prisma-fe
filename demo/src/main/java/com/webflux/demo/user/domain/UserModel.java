package com.webflux.demo.user.domain;

import lombok.Data;

import java.util.List;

@Data
public class UserModel {
    Long userId ;
    String username;
    String firstName ;
    String lastName ;
    String email;
    String password ;


    List<RoleModel> roles ;


    public UserModel (String email , String password , List<RoleModel> roles) {
        this.email= email ;
        this.password=password ;
        this.roles=roles ;}
}
