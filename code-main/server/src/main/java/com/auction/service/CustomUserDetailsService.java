package com.auction.service;

import com.auction.model.User;
import com.auction.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Service: CustomUserDetailsService
 * Implement UserDetailsService của Spring Security
 *
 * Nhiệm vụ:
 * - Load user từ database theo username
 * - Convert User entity thành UserDetails (của Spring Security)
 * - Cung cấp thông tin authentication cho SecurityContext
 *
 * Service này ĐÃ HOÀN CHỈNH, không cần sửa
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Load user theo username
     * Được gọi tự động bởi Spring Security khi user login
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tìm user trong database
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user: " + username));

        // Convert sang UserDetails của Spring Security
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
