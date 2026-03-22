package gyak.presentation;

import gyak.model.Car;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gyak.repository.jdbc.CarRepositoryJdbc;

@jakarta.servlet.annotation.WebListener
public class WebListener implements ServletContextListener {

    private static Logger logger = LoggerFactory.getLogger(WebListener.class);

    private CarRepositoryJdbc repository = new CarRepositoryJdbc();

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("WAR IS DEPLOYED YEE");
        repository.createCar(new Car("BMW", "X6", 999, null, null));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("WAR IS DELETING NOOO");
        repository.delete(999);
    }
}
