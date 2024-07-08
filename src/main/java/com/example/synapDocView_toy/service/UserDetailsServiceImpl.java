package com.example.synapDocView_toy.service;

import com.example.synapDocView_toy.domain.TempUser;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private TempUser tempUser = new TempUser(1L, "1234", "$2a$12$McITAtjimqLw.8GfjU5a2u8kMQvcN3TUe6W.K1xT0KinKdAW31UOW", "ROLE_USER");

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        if(!username.equals(tempUser.getUsername())){
            throw new UsernameNotFoundException("없는 유저입니다.");
        }
        return new User(
                tempUser.getUsername(),
                tempUser.getPassword(),
                List.of(new SimpleGrantedAuthority(tempUser.getRole()))
        );
    }
}
