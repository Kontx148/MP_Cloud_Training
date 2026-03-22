package gyak.repository.jdbc;

import gyak.model.Car;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import gyak.repository.CarRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

public class CarRepositoryJdbc implements CarRepository  {
    private static Logger Log = LoggerFactory.getLogger(CarRepositoryJdbc.class);

    private final ConnectionPool connectionPool = ConnectionPool.getInstance();


    @Override
    public long createCar(Car car) {
        Connection connection = connectionPool.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement("INSERT INTO db_cars (make, model, year) VALUES (?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, car.getMake());
            ps.setString(2, car.getModel());
            ps.setInt(3, car.getYear());
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()) {
                Log.info("Car created successfully");
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }

            throw new RuntimeException("No ID returned");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(connection != null) {
                connectionPool.returnConnection(connection);
            }
        }
    }

    @Override
    public Collection<Car> getAllCars() {
        Connection connection = connectionPool.getConnection();
        Collection<Car> cars = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM db_cars ORDER BY year DESC");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Car carToAdd = new Car();
                carToAdd.setId(rs.getLong("id"));
                carToAdd.setMake(rs.getString("make"));
                carToAdd.setModel(rs.getString("gyak/model"));
                carToAdd.setYear(rs.getInt("year"));

                cars.add(carToAdd);
            }
            return cars;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(connection != null) {
                connectionPool.returnConnection(connection);
            }
        }
    }

    @Override
    public Collection<Car> filterByYear(Integer min, Integer max) {
        Connection connection = connectionPool.getConnection();
        Collection<Car> cars = new ArrayList<>();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM db_cars WHERE (year >= ? AND year <= ?)");
            ps.setInt(1, min);
            ps.setInt(2, max);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Car carToAdd = new Car();
                carToAdd.setId(rs.getLong("id"));
                carToAdd.setMake(rs.getString("make"));
                carToAdd.setModel(rs.getString("gyak/model"));
                carToAdd.setYear(rs.getInt("year"));

                cars.add(carToAdd);
            }
            return cars;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(connection != null) {
                connectionPool.returnConnection(connection);
            }
        }
    }

    @Override
    public Car update(Car update) {
        return null;
    }

    @Override
    public void delete(Integer year) {
        Connection connection = connectionPool.getConnection();

        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM db_cars WHERE year = ?");
            ps.setInt(1, year);
            int rs = ps.executeUpdate();

            if(rs == 0) {
                throw new RuntimeException("No car found with year given");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            if(connection != null) {
                connectionPool.returnConnection(connection);
            }
        }
    }
}
