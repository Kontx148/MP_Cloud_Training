package edu.bbte.idde.repository.jdbc;

import edu.bbte.idde.model.UsedCarListing;
import edu.bbte.idde.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

@Repository
@Profile("jdbc")
@Slf4j
public class JdbcUsedCarRepository implements UsedCarRepository {

    @Autowired
    private ConnectionManager connectionManager;

    @Override
    public long createUsedCarListing(UsedCarListing usedCarListing) throws RepositoryException {
        Connection connection = connectionManager.getConnection();
        try {
            log.info("""
                            Requested car listing creation for
                            Make: {}
                            Model: {}
                            Prod Year: {}
                            Price: {}
                            Listing date: {}""",
                    usedCarListing.getMake(), usedCarListing.getModel(), usedCarListing.getFabricationYear(),
                    usedCarListing.getPrice(), usedCarListing.getUploadDate()
            );
            assert connection != null;
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO used_car (UID, make, model, fabrication_year, price, upload_date) "
                           + "VALUES (?,?,?,?,?,?);",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, usedCarListing.getUuid());
            ps.setString(2, usedCarListing.getMake());
            ps.setString(3, usedCarListing.getModel());
            ps.setInt(4, usedCarListing.getFabricationYear());
            ps.setDouble(5, usedCarListing.getPrice());
            ps.setString(6, usedCarListing.getUploadDate());
            ps.execute();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    log.info("Car listing created successfully with ID {}", id);
                    return id;
                }
                throw new RepositoryException("Failed to retrieve generated ID");
            }

        } catch (SQLException e) {
            log.error("Used car listing creation failed", e);
            throw new RepositoryException("Used car listing creation failed", e);
        } finally {
            if (connection != null) {
                connectionManager.returnConnection(connection);
            }
        }
    }

    private UsedCarListing mapResultToUsedCar(ResultSet rs) throws SQLException {
        UsedCarListing usedCar = new UsedCarListing();
        usedCar.setUuid(rs.getString("UID"));
        usedCar.setId(rs.getLong("ID"));
        usedCar.setMake(rs.getString("make"));
        usedCar.setModel(rs.getString("model"));
        usedCar.setFabricationYear(rs.getInt("fabrication_year"));
        usedCar.setPrice(rs.getDouble("price"));
        usedCar.setUploadDate(rs.getString("upload_date"));
        return usedCar;
    }

    @Override
    public Collection<UsedCarListing> getUsedCars() throws RepositoryException {
        Connection connection = connectionManager.getConnection();
        Collection<UsedCarListing> usedCars = new ArrayList<>();
        try {
            log.info("Requested car listings from database");
            assert connection != null;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM used_car;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                usedCars.add(mapResultToUsedCar(rs));
            }
        } catch (SQLException e) {
            log.error("Getting all used cars failed", e);
            throw new RepositoryException("Getting all used cars failed", e);
        } finally {
            if (connection != null) {
                connectionManager.returnConnection(connection);
            }
        }
        return usedCars;
    }

    @Override
    public Collection<UsedCarListing> findByMake(String make) {
        Connection connection = connectionManager.getConnection();
        Collection<UsedCarListing> usedCars = new ArrayList<>();
        try {
            log.info("Requested car listings from database, with make: {}", make);
            assert connection != null;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM used_car WHERE make = ?;");
            ps.setString(1, make);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                usedCars.add(mapResultToUsedCar(rs));
            }
        } catch (SQLException e) {
            log.error("Getting used cars by make failed", e);
            throw new RepositoryException("Getting used cars by make failed", e);
        } finally {
            if (connection != null) {
                connectionManager.returnConnection(connection);
            }
        }
        return usedCars;
    }

    @Override
    public UsedCarListing getUsedCar(long id) throws RepositoryException, EntityNotFoundException {
        Connection connection = connectionManager.getConnection();
        try {
            log.info("Requested used car listing from database, with ID: {}", id);
            assert connection != null;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM used_car WHERE ID = ?;");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultToUsedCar(rs);
            } else {
                log.info("No car listing found with ID: {}", id);
                throw new EntityNotFoundException("No car listing found with ID: " + id);
            }
        } catch (SQLException e) {
            log.error("Getting Used Car by ID failed", e);
            throw new RepositoryException("Getting Used car by ID failed", e);
        } finally {
            if (connection != null) {
                connectionManager.returnConnection(connection);
            }
        }
    }

    @Override
    public void updateUsedCar(UsedCarListing usedCarListing) throws RepositoryException, EntityNotFoundException {
        Connection connection = connectionManager.getConnection();
        try {
            log.info("Requested update on car listing with ID: {}", usedCarListing.getId());
            assert connection != null;
            PreparedStatement ps = connection.prepareStatement(
                    "UPDATE used_car SET make = ?, model = ?, "
                            + "fabrication_year = ?, price = ?, upload_date = ? WHERE ID = ?;"
            );
            ps.setString(1, usedCarListing.getMake());
            ps.setString(2, usedCarListing.getModel());
            ps.setInt(3, usedCarListing.getFabricationYear());
            ps.setDouble(4, usedCarListing.getPrice());
            ps.setString(5, usedCarListing.getUploadDate());
            ps.setLong(6, usedCarListing.getId());
            int rows = ps.executeUpdate();
            if (rows == 0) {
                log.warn("No used car found to update with ID {}", usedCarListing.getId());
                throw new EntityNotFoundException("No used car found to update with ID {} " + usedCarListing.getId());
            }

            log.info("Successfully updated car listing with ID: {}", usedCarListing.getId());
        } catch (SQLException e) {
            log.error("Updating used car failed", e);
            throw new RepositoryException("Updating used car failed", e);
        } finally {
            if (connection != null) {
                connectionManager.returnConnection(connection);
            }
        }
    }

    @Override
    public void deleteUsedCar(long id) throws RepositoryException, EntityNotFoundException {
        Connection connection = connectionManager.getConnection();
        try {
            log.info("Requested deletion on car listing with ID: {}", id);
            assert connection != null;
            PreparedStatement ps = connection.prepareStatement("DELETE FROM used_car WHERE ID = ?;");
            ps.setLong(1, id);
            int rows = ps.executeUpdate();

            if (rows == 0) {
                log.warn("No used car found to delete with ID {}", id);
                throw new EntityNotFoundException("No used car found to delete with ID " + id);
            }

            log.info("Successfully deleted car listing with ID: {}", id);
        } catch (SQLException e) {
            log.error("Deleting used car failed", e);
            throw new RepositoryException("Deleting used car failed", e);
        } finally {
            if (connection != null) {
                connectionManager.returnConnection(connection);
            }
        }
    }

    @Override
    public boolean existsUsedCar(long id) throws RepositoryException {
        Connection connection = connectionManager.getConnection();
        try {
            assert connection != null;
            PreparedStatement ps = connection.prepareStatement(
                    "SELECT 1 FROM used_car WHERE ID = ?;"
            );
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                // Returns false if cursor is positioned after the last row
                return rs.next();
            }
        } catch (SQLException e) {
            log.error("Checking used car failed", e);
            throw new RepositoryException("Checking used car failed", e);
        } finally {
            if (connection != null) {
                connectionManager.returnConnection(connection);
            }
        }
    }
}
