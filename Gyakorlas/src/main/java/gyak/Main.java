package gyak;

import gyak.model.Car;
import gyak.repository.CarJpaRepo;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import lombok.extern.slf4j.Slf4j;
import gyak.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SpringBootApplication
@Slf4j
public class Main {

    public static void jpaExample() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("asd");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();
        log.info("Making new entites");
        Car car1 = new Car("Audi", "A6", 2003, null, null);
        log.info("Entity before persist {}", car1);
        entityManager.persist(car1);
        log.info("Entity before persist {}", car1);
        entityManager.getTransaction().commit();
        log.info("");

        log.info("Selecting by ID");
        Car retrievedEntity = entityManager.find(Car.class, 1L);
        log.info("Retrieved entity: {}", retrievedEntity);

        // nem létező ID lekérése = null
        // figyelem a logokra: itt van select, míg a fentinél nincs
        Car retrievedNonExistentEntity = entityManager.find(Car.class, 42L);
        log.info("Retrieved non-existent entity: {}", retrievedNonExistentEntity);

    }

    public static void jpaOperationsExample() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("asd");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

        // 1. PERSIST: Új entitás mentése (Managed állapotba kerül)
        log.info("--- PERSIST ---");
        Car car = new Car("Honda", "Civic", 2018, null, null);
        entityManager.persist(car);
        log.info("Persisted: {}", car);

        // 2. FLUSH: SQL utasítások kikényszerítése az adatbázisba azonnal
        log.info("--- FLUSH ---");
        entityManager.flush();

        log.info("Is managed?");
        log.info(String.valueOf(entityManager.contains(car)));

        // 3. DETACH: Entitás leválasztása a kontextusról (módosítások nem kerülnek mentésre)
        log.info("--- DETACH ---");
        entityManager.detach(car);

        log.info("Is managed?");
        log.info(String.valueOf(entityManager.contains(car)));

        // 4. MERGE: Detached entitás visszacsatolása (UPDATE)
        log.info("--- MERGE ---");
        car.setYear(2020); // Módosítás detached állapotban
        Car managedCar = entityManager.merge(car); // Visszatér a Managed példánnyal
        log.info("Merged (Managed): {}", managedCar);

        // 5. REFRESH: Entitás visszaállítása az adatbázis állapotra (Visszavonás)
        log.info("--- REFRESH ---");
        managedCar.setModel("RosszModell"); // Nem kívánt módosítás a memóriában
        entityManager.refresh(managedCar);  // Adatbázisból újratöltés
        log.info("Refreshed (Original Model): {}", managedCar.getModel());

        // 6. GET REFERENCE: Proxy lekérése (Lazy, nincs azonnali SELECT)
        log.info("--- GET REFERENCE ---");
        // Csak akkor dob kivételt, ha hozzányúlunk és nincs ilyen ID
        Car carProxy = entityManager.getReference(Car.class, managedCar.getId());
        log.info("Proxy class type: {}", carProxy.getClass().getName());

        // 7. REMOVE: Entitás törlése
        log.info("--- REMOVE ---");
        entityManager.remove(managedCar);

        // 8. CLEAR: Teljes kontextus ürítése (Minden entitás Detached lesz)
        log.info("--- CLEAR ---");
        entityManager.clear();

        entityManager.getTransaction().commit();
        entityManager.close();
        entityManagerFactory.close();
    }

    public static void jpqlExample() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("asd");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        log.info("Persisting the cars");
        entityManager.getTransaction().begin();
        for(int i=2010;i<=2020;i++) {
            entityManager.persist(new Car("Audi", "a6", i, null, null));
        }

        entityManager.clear();
        Car createdOne = entityManager.find(Car.class, 1);
        log.info("This one i found : {}", createdOne);


        log.info("Most csinalunk egy masikat");
        Car newOne = new Car("Dacia", "Sanders", 2034, null, null);
        log.info("Ez volt {}", newOne);
        entityManager.persist(newOne);
        entityManager.getTransaction().commit();

        newOne = entityManager.find(Car.class, newOne.getId());
        log.info("Ez lett {}", newOne);

        log.info("Printing the query results");
//        TypedQuery<Car> carsQuery = entityManager.createQuery("from Car where year <= :max and year >= :min",Car.class);
//        carsQuery.setParameter("max", 2019);
//        carsQuery.setParameter("min", 2017);
//        List<Car> cars = carsQuery.getResultList();

        // ===============

//        TypedQuery<Car> carsQuery = entityManager.createQuery("from Car where year <= ?1 and year >= ?2",Car.class);
//        carsQuery.setParameter(1, 2019);
//        carsQuery.setParameter(2, 2017);
//        List<Car> cars = carsQuery.getResultList();

        // ============

//        TypedQuery<Long> carsQuery = entityManager.createQuery("select id from Car where year <= :max and year >= :min",Long.class);
//        carsQuery.setParameter("max", 2019);
//        carsQuery.setParameter("min", 2017);
//        List<Long> carids = carsQuery.getResultList();
//
//        for(Long id : carids) {
//            System.out.println(id);
//        }

        // =======
//        TypedQuery<Object[]> carsQuery = entityManager.createQuery("select make, model from Car ", Object[].class);
//        List<Object[]> asd = carsQuery.getResultList();
//        for(Object[] a : asd) {
//            System.out.println(a[0] + " " + a[1]);
//        }

//        TypedQuery<Car> carTypedQuery = entityManager.createNamedQuery(Car.FIND_BY_YEAR, Car.class);
//        carTypedQuery.setParameter("min", 2016);
//        carTypedQuery.setParameter("max", 20126);
//        List<Car> cars = carTypedQuery.getResultList();

        TypedQuery<Car> carTypedQuery = entityManager.createQuery("from Car ORDER BY createdAt", Car.class);
        List<Car> cars = carTypedQuery.getResultList();

        for(Car c : cars) {
            System.out.println(c);
        }
    }

    public static void relationExample() {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("asd");
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        log.info("Persisting the cars");
        entityManager.getTransaction().begin();
        Car car = new Car("Audi", "A7", 2017, null, new ArrayList<>());
        Comment comment1 = new Comment("Mi ez");
        Comment comment2 = new Comment("Mi ez csak szebben");
        Comment comment3 = new Comment("Mi ez csak erdekes");

        car.getComments().add(comment1);
        car.getComments().add(comment2);
        car.getComments().add(comment3);

        comment1.setCar(car);
        comment2.setCar(car);
        comment3.setCar(car);

        entityManager.persist(car);
        entityManager.getTransaction().commit();

        log.info("Getting all the comments");
        TypedQuery<Comment> commentTypedQuery = entityManager.createQuery("from Comment", Comment.class);
        List<Comment> comments = commentTypedQuery.getResultList();

        for(Comment comment : comments) {
            System.out.println(comment);
        }

        entityManager.getTransaction().begin();
        entityManager.remove(car);
        entityManager.getTransaction().commit();

        commentTypedQuery = entityManager.createQuery("from Comment", Comment.class);
        comments = commentTypedQuery.getResultList();

        for(Comment comment : comments) {
            System.out.println(comment);
        }
    }

    @Autowired
    CarJpaRepo carJpaRepo;

    @PostConstruct
    public void after() {
        Car car = new Car("Audi", "A7", 2017, null, new ArrayList<>());
        Car car2 = new Car("Audi", "A8", 2012, null, new ArrayList<>());
        Comment comment1 = new Comment("Mi ez");
        Comment comment2 = new Comment("Mi ez csak szebben");
        Comment comment3 = new Comment("Mi ez csak erdekes");

        carJpaRepo.save(car);
        carJpaRepo.save(car2);

        for(int i=2010;i<2020;i++) {
            carJpaRepo.save(new Car("BMQW", "QW", i, null, new ArrayList<>()));
        }

        Collection<Car> cars = carJpaRepo.findAll();
        for(Car c : cars) {
            System.out.println(c);
        }

        cars = carJpaRepo.findByMake("A8");
        log.info("Got car from make for A8");
        for(Car c : cars) {
            System.out.println(c);
        }

        log.info("============================");
        cars = carJpaRepo.findSorted();
        for(Car c : cars) {
            System.out.println(c);
        }

        log.info("=================================");
        log.info("=================================");
        log.info("=================================");
        Pageable pageable = PageRequest.of(2, 4);
        Page<Car> carPage = carJpaRepo.findAll(pageable);
        for(Car c : carPage) {
            System.out.println(c);
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        //relationExample();
            //jpqlExample();
            //jpaExample();
            //jpaOperationsExample();




//        PropertyProvider propertyProvider = new PropertyProvider();
//
//        System.out.println(propertyProvider.getProperty("asd"));
//
//        PropertiesManager propertiesManager = new PropertiesManager();
//
//        System.out.println(propertiesManager.getJdbc());
//        System.out.println(propertiesManager.getPool());
//
//        propertiesManager.toConfigFile();
//        CarRepositoryJdbc repository = new CarRepositoryJdbc();
////        Car car1 = new Car("Audi", "A1", 2003);
////        System.out.println(repository.createCar(car1));
//        Collection<Car> cars =  repository.getAllCars();
//        for(Car c : cars) {
//            System.out.println(c);
//        }
//
//        System.out.println("======================================");
//
//        Collection<Car> cars2 = repository.filterByYear(2013, 2016);
//        for(Car c : cars2) {
//            System.out.println(c);
//        }
//
//        //repository.delete(2014);
//
//        System.out.println("======================================");

//        for(Car c : cars) {
//            System.out.println(c);
//        }
    }
}
