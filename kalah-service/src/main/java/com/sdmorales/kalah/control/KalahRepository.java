package com.sdmorales.kalah.control;

import com.sdmorales.kalah.entity.Game;
import org.springframework.data.repository.CrudRepository;

public interface KalahRepository extends CrudRepository<Game, Long> {

}
