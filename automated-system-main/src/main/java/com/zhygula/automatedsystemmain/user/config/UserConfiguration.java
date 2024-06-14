package com.zhygula.automatedsystemmain.user.config;

import com.zhygula.automatedsystemmain.user.model.Role;
import com.zhygula.automatedsystemmain.user.model.User;
import com.zhygula.automatedsystemmain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class UserConfiguration implements CommandLineRunner {

    private final UserRepository userRepository;
    private String superAdminEmail = "admin@mail.com";
    private String superAdminPassword = "12345";

    @Override
    public void run(String... args) throws Exception {

        User user = userRepository.findByEmail(superAdminEmail);
        if(user != null) {
            System.out.println("Super admin already exists");
        } else {
            User superAdmin = new User();
            superAdmin.setEmail(superAdminEmail);
            superAdmin.setPassword(superAdminPassword);
            superAdmin.setRole(Role.ADMIN);
            userRepository.save(superAdmin);
            System.out.println("Super admin created");
        }

    }
}

