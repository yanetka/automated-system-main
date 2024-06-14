package com.zhygula.automatedsystemmain.exhibition.service;

import com.zhygula.automatedsystemmain.exhibition.model.Exhibition;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ExhibitionService {

    void save(String theme, List<String> hall, LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime, double price);

    Exhibition findById(long id);

    List<Exhibition> filter(String criteria);

    List<Exhibition> filterByTheme(List<Exhibition> exhibitions);

    List<Exhibition> filterByPrice(List<Exhibition> exhibitions);

    List<Exhibition> filterByDate(List<Exhibition> exhibitions);

    void deleteById(long id);

    List<Exhibition> findAllExhibitions();
}
