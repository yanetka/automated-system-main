package com.zhygula.automatedsystemmain.user.service;

import com.zhygula.automatedsystemmain.user.model.User;

import java.util.List;

public interface UserService {

    boolean checkIsEmailAvailable(String email);

    boolean checkIsEmailPresent(String email);

    void register(String email, String password, String firstName, String lastName, String phone);

    boolean login(String email, String password);

    void buyTicket(long userId, long exhibitionId);

    void deleteTicketsByExhibitionId(long exhibitionId);

    void printTickets(long userId);

    List<User> findAllUsers();
}
