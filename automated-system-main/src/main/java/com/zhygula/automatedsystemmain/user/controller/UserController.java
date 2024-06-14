package com.zhygula.automatedsystemmain.user.controller;

import com.zhygula.automatedsystemmain.user.model.User;
import com.zhygula.automatedsystemmain.user.service.UserService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void register(String email, String password, String firstName, String lastName, String phone) {
        userService.register(email, password, firstName, lastName, phone);
    }

    public boolean login(String email, String password) {
        return userService.login(email, password);
    }

    public void buyTicket(long userId, long exhibitionId) {
        userService.buyTicket(userId, exhibitionId);
    }

    public void printTickets(long userId) {
        userService.printTickets(userId);
    }

    public void deleteTicketsByExhibitionId(long exhibitionId) {
        userService.deleteTicketsByExhibitionId(exhibitionId);
    }

    public List<User> findAllUsers() {
        return userService.findAllUsers();
    }
}

