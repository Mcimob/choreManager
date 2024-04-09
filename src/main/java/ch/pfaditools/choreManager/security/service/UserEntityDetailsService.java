package ch.pfaditools.choreManager.security.service;

import ch.pfaditools.choreManager.backend.repository.UserRepository;
import ch.pfaditools.choreManager.exception.UsernameAlreadyExistsException;
import ch.pfaditools.choreManager.model.UserEntity;
import ch.pfaditools.choreManager.model.UserEntityPrinciple;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserEntityDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserEntityDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserEntity user) throws UsernameAlreadyExistsException{
        try {
            loadUserByUsername(user.getUsername());
            throw new UsernameAlreadyExistsException();
        } catch(UsernameNotFoundException e) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setCreatedAt(LocalDateTime.now());
            user.setRole("USER");
            userRepository.save(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new UserEntityPrinciple(user);
    }
}
