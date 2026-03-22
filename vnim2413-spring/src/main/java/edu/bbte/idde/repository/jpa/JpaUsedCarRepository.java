package edu.bbte.idde.repository.jpa;

import edu.bbte.idde.model.UsedCarListing;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
@Profile("jpa")
public interface JpaUsedCarRepository extends JpaRepository<UsedCarListing, Long> {
    Collection<UsedCarListing> findByMake(String make);
}
