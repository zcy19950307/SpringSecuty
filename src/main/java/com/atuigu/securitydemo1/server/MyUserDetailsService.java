package com.atuigu.securitydemo1.server;


import com.atuigu.securitydemo1.entity.Users;
import com.atuigu.securitydemo1.mapper.UserMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("userDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserMapper userMapper ;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        QueryWrapper<Users> queryWrapper = new QueryWrapper<Users>();
        queryWrapper.eq("username",username);
        Users users = userMapper.selectOne(queryWrapper);
        if(users == null){
            throw  new UsernameNotFoundException("用户不存在");
        }

        List<GrantedAuthority> list = AuthorityUtils.commaSeparatedStringToAuthorityList("manager,ROLE_sale1");
        return new User(users.getUsername(),new BCryptPasswordEncoder().encode(users.getPassword()),list);

       // return new User("s",new BCryptPasswordEncoder().encode("123"),list);

    }
}
