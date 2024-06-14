package com.zhygula.automatedsystemmain.exhibition.controller;

import com.zhygula.automatedsystemmain.exhibition.model.Exhibition;
import com.zhygula.automatedsystemmain.exhibition.service.ExhibitionService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class ExhibitionController {

    private final ExhibitionService exhibitionService;

    public ExhibitionController(ExhibitionService exhibitionService) {
        this.exhibitionService = exhibitionService;
    }

    public void save(String theme, List<String> hall, LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime, double price) {
        exhibitionService.save(theme, hall, fromDate, toDate, fromTime, toTime, price);
    }

    public Exhibition findById(long id) {
        return exhibitionService.findById(id);
    }

    public List<Exhibition> filter(String criteria) {
        return exhibitionService.filter(criteria);
    }

    public void deleteById(long id) {
        exhibitionService.deleteById(id);
    }

    public List<Exhibition> findAllExhibitions() {
        return exhibitionService.findAllExhibitions();
    }
}

