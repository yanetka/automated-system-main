package com.zhygula.automatedsystemmain.user.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Ticket implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ticketId;
    @ManyToOne
    private User user;
    private long exhibitionId;

    @Override
    public String toString() {
        return "Ticket{" +
                "userId=" + user +
                ", exhibitionId=" + exhibitionId +
                '}';
    }
}
