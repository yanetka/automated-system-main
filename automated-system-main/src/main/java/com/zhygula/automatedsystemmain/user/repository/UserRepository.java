package com.zhygula.automatedsystemmain.user.repository;

import com.zhygula.automatedsystemmain.user.model.Ticket;
import com.zhygula.automatedsystemmain.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findById(long id);

    User findByEmail(String email);

    User updateTickets(long userId, List<Ticket> tickets);
}
