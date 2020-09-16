package com.sdmorales.kalah;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface KalahRepository extends CrudRepository<Game, Long> {

    List<Game> findAll();

}
