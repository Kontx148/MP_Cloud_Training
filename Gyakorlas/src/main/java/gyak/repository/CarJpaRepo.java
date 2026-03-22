package gyak.repository;

import gyak.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.lang.annotation.Retention;
import java.util.Collection;

@Repository
public interface CarJpaRepo extends JpaRepository<Car, Long> {
    Collection<Car> findByMake(String make);

    @Query("from Car order by createdAt DESC ")
    Collection<Car> findSorted();
}
