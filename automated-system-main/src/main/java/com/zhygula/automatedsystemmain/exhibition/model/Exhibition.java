package com.zhygula.automatedsystemmain.exhibition.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Exhibition implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String theme;
    @ElementCollection
    private List<String> hall;
    private LocalDate fromDate;
    private LocalDate toDate;
    private LocalTime fromTime;
    private LocalTime toTime;
    private double price;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exhibition that = (Exhibition) o;
        return id == that.id && Double.compare(price, that.price) == 0 && Objects.equals(theme, that.theme) && Objects.equals(hall, that.hall) && Objects.equals(fromDate, that.fromDate) && Objects.equals(toDate, that.toDate) && Objects.equals(fromTime, that.fromTime) && Objects.equals(toTime, that.toTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, theme, hall, fromDate, toDate, fromTime, toTime, price);
    }

    @Override
    public String toString() {
        return "Exhibition{" +
                "id=" + id +
                ", theme='" + theme + '\'' +
                ", hall='" + hall + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", fromTime=" + fromTime +
                ", toTime=" + toTime +
                ", price=" + price +
                '}';
    }

}
