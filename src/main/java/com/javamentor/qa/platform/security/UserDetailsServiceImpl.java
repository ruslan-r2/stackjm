package com.javamentor.qa.platform.security;

import com.javamentor.qa.platform.models.entity.user.User;
import com.javamentor.qa.platform.service.abstracts.model.UserService;
import lombok.Getter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service("userDetailsServiceImpl")
@Getter
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;
    private Optional<User> findUser;


    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userService.getByEmail(email);
        findUser = user;
        return user.orElse(null);
    }
}



