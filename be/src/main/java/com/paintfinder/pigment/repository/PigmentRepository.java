package com.paintfinder.pigment.repository;

import com.paintfinder.pigment.entity.Pigment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PigmentRepository extends JpaRepository<Pigment, Long> {

    Pigment findByPigmentIdx(int pigmentIdx);

    List<Pigment> findAll();

    Optional<Pigment> findFirstByCiName(String ciName);

    Optional<Pigment> findFirstByCiNumber(String ciNumber);

}
