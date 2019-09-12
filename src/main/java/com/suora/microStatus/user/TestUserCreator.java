package com.suora.microStatus.user;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TestUserCreator {
    private final UserRepository userRepository;

    @EventListener
    public void createUsers(ApplicationReadyEvent event) {
        var passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        userRepository.save(new User(
            UUID.fromString("586c80e9-6e9e-4434-b4e2-f47c85f71ed2"),
            "test1",
            passwordEncoder.encode("foo")));
        userRepository.save(new User(
            UUID.fromString("f33d118e-622b-4ad4-b9bb-99f5bfe1f57e"),
            "test2",
            passwordEncoder.encode("bar")));
    }
}
