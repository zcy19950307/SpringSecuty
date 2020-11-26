package com.atuigu.securitydemo1.controller;


import com.atuigu.securitydemo1.entity.Users;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("hello")
    public String hello() {
        return "hello security";
    }

    @GetMapping("index")
    public String index() {
        return "hello index";
    }

    @GetMapping("update")
    //@Secured({"ROLE_manager","ROLE_manager"})   // 角色是否包含 ROLE_manager  和 ROLE_manager  这个
    @PreAuthorize("hasAnyAuthority('admins')")    // 进入方法前会调用这个方法
    @PostAuthorize("hasAnyAuthority('admins')")
    public String update() {
        System.out.println("update......");
        return "hello update";
    }

    @GetMapping("getAll")
    @PostAuthorize("hasAnyAuthority('admins')")
    @PostFilter("filterObject.username == 'admin1'")
    public List<Users> getAllUser(){
        ArrayList<Users> list = new ArrayList<>();
        list.add(new Users(11,"admin1","6666"));
        list.add(new Users(21,"admin2","888"));
        System.out.println(list);
        return list;
    }


}
