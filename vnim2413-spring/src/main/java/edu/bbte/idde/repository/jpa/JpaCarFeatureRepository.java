package edu.bbte.idde.repository.jpa;

import edu.bbte.idde.model.CarFeature;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Profile("jpa")
public interface JpaCarFeatureRepository extends JpaRepository<CarFeature, Long> {
}