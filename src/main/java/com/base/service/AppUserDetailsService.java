package com.base.service;

import com.base.model.entity.User;
import com.base.model.enumeration.UserStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
//    private final UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User u = userRepo.findByEmail(username).orElseThrow(() ->
//                new UsernameNotFoundException("Not found: " + username));
//
//        var authorities = u.getRoles().stream()
//                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName().name()))
//                .collect(Collectors.toSet());
//
//        boolean enabled = u.getStatus() == UserStatus.ACTIVE;
//        boolean accountNonLocked = u.getStatus() != UserStatus.BLOCKED;
//
//        return org.springframework.security.core.userdetails.User
//                .withUsername(u.getEmail())
//                .password(u.getPasswordHash())
//                .authorities(authorities)
//                .accountLocked(!accountNonLocked)
//                .disabled(!enabled)
//                .build();
        return null;
    }
}
