package com.zhygula.automatedsystemmain.user.service.impl;

import com.zhygula.automatedsystemmain.exhibition.model.Exhibition;
import com.zhygula.automatedsystemmain.exhibition.service.ExhibitionService;
import com.zhygula.automatedsystemmain.user.model.Role;
import com.zhygula.automatedsystemmain.user.model.Ticket;
import com.zhygula.automatedsystemmain.user.model.User;
import com.zhygula.automatedsystemmain.user.repository.UserRepository;
import com.zhygula.automatedsystemmain.user.service.UserService;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ExhibitionService exhibitionService;

    public UserServiceImpl(UserRepository userRepository, ExhibitionService exhibitionService) {
        this.userRepository = userRepository;
        this.exhibitionService = exhibitionService;
    }


    @Override
    public boolean checkIsEmailAvailable(String email) {
        if (email == null || email.isEmpty()) {
            System.out.println("Email is empty!");
            return false;
        }
        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            System.out.println("Email is not valid!");
            return false;
        }
        for (User user : userRepository.findAll()) {
            if (user.getEmail().equals(email)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void register(String email, String password, String firstName, String lastName, String phone) {
        User user = new User(getNextId(), email, password, firstName, lastName, phone, Role.AUTHORIZED_USER);
        userRepository.save(user);
    }

    private long getNextId() {
        long maxId = 0;
        for (User user : userRepository.findAll()) {
            if (user.getId() > maxId) {
                maxId = user.getId();
            }
        }
        return maxId + 1;
    }

    @Override
    public boolean checkIsEmailPresent(String email) {
        if (email == null || email.isEmpty()) {
            System.out.println("Email is empty!");
            return false;
        }
        if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            System.out.println("Email is not valid!");
            return false;
        }
        for (User user : userRepository.findAll()) {
            if (user.getEmail().equals(email)) {
                return true;
            }
        }
        System.out.println("Can't find user with this email. try again!");
        return false;
    }

    @Override
    public boolean login(String email, String password) {
        for (User user : userRepository.findAll()) {
            if (user.getEmail().equals(email)) {
                if (user.getPassword().equals(password)) {
                    System.out.println("Login successful!");
                    return true;
                }
                System.out.println("Password is incorrect!");
                return false;
            }
        }
        return false;
    }

    @Override
    public void buyTicket(long userId, long exhibitionId) {
        User user = userRepository.findById(userId);
        List<Ticket> tickets = user.getTickets();
        Ticket ticket = new Ticket();
        ticket.setUser(userRepository.findById(userId));
        ticket.setExhibitionId(exhibitionId);
        tickets.add(ticket);
        userRepository.updateTickets(user.getId(), tickets);
    }

    @Override
    public void printTickets(long userId) {
        System.out.println("Your tickets");
        User user = userRepository.findById(userId);
        List<Ticket> tickets = user.getTickets();
        if (tickets.isEmpty()) {
            System.out.println("You don't have any tickets!");
            return;
        }
        for (int i = 1; i <= tickets.size(); i++) {
            long exhibitionId = tickets.get(i - 1).getExhibitionId();
            Exhibition exhibition = exhibitionService.findById(exhibitionId);
            System.out.printf("""
                    %d.Ticket for exhibition with id: %d
                    Ticket price: %f
                    Exhibition theme: %s
                    Date from %s to %s
                    Time from %s to %s
                    """,
                    i,
                    exhibitionId,
                    exhibition.getPrice(),
                    exhibition.getTheme(),
                    exhibition.getFromDate(),
                    exhibition.getToDate(),
                    exhibition.getFromTime(),
                    exhibition.getToTime());
        }
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteTicketsByExhibitionId(long exhibitionId) {
        for (User user : userRepository.findAll()) {
            List<Ticket> tickets = user.getTickets();
            tickets.removeIf(ticket -> ticket.getExhibitionId() == exhibitionId);
            userRepository.updateTickets(user.getId(), tickets);
        }
    }
}

