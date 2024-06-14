package com.zhygula.automatedsystemmain.exhibition.service.impl;


import com.zhygula.automatedsystemmain.exhibition.model.Exhibition;
import com.zhygula.automatedsystemmain.exhibition.repository.ExhibitionRepository;
import com.zhygula.automatedsystemmain.exhibition.service.ExhibitionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;

@Service
public class ExhibitionServiceImpl implements ExhibitionService {

    private final ExhibitionRepository exhibitionRepository;

    public ExhibitionServiceImpl(ExhibitionRepository exhibitionRepository) {
        this.exhibitionRepository = exhibitionRepository;
    }

    @Override
    public List<Exhibition> findAllExhibitions() {
        return exhibitionRepository.findAll();
    }

    @Override
    public void save(String theme, List<String> hall, LocalDate fromDate, LocalDate toDate, LocalTime fromTime, LocalTime toTime, double price) {
        Exhibition exhibition = new Exhibition(getNextId(), theme, hall, fromDate, toDate, fromTime, toTime, price);
        exhibitionRepository.save(exhibition);
    }

    private long getNextId() {
        long maxId = 1L;
        for (Exhibition exhibition : exhibitionRepository.findAll()) {
            if (exhibition.getId() > maxId) {
                maxId = exhibition.getId();
            }
        }
        return maxId + 1;
    }

    @Override
    public Exhibition findById(long id) {
        return exhibitionRepository.findById(id);
    }

    @Override
    public List<Exhibition> filter(String criteria) {
        List<Exhibition> exhibitions = exhibitionRepository.findAll();
        return switch (criteria) {
            case "1" -> filterByTheme(exhibitions);
            case "2" -> filterByPrice(exhibitions);
            case "3" -> filterByDate(exhibitions);
            default -> {
                System.out.println("Wrong input!");
                yield null;
            }
        };
    }

    @Override
    public List<Exhibition> filterByTheme(List<Exhibition> exhibitions) {
        exhibitions.sort(byTheme);
        return exhibitions;
    }

    @Override
    public List<Exhibition> filterByPrice(List<Exhibition> exhibitions) {
        exhibitions.sort(byPrice);
        return exhibitions;
    }

    @Override
    public List<Exhibition> filterByDate(List<Exhibition> exhibitions) {
        exhibitions.sort(byDate);
        return exhibitions;
    }

    public Comparator<Exhibition> byTheme = Comparator.comparing(Exhibition::getTheme);

    public Comparator<Exhibition> byPrice = Comparator.comparing(Exhibition::getPrice);

    public Comparator<Exhibition> byDate = Comparator.comparing(Exhibition::getFromDate);

    @Override
    public void deleteById(long id) {
        exhibitionRepository.deleteById(id);
    }
}
