package com.zhygula.automatedsystemmain.exhibition.repository;

import com.zhygula.automatedsystemmain.exhibition.model.Exhibition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {

    Exhibition findById(long id);

}
