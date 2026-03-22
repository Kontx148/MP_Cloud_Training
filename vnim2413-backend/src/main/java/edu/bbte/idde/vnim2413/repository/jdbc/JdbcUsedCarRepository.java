package edu.bbte.idde.vnim2413.repository.jdbc;

import edu.bbte.idde.vnim2413.model.UsedCarListing;
import edu.bbte.idde.vnim2413.repository.ConnectionManager;
import edu.bbte.idde.vnim2413.repository.EntityNotFoundException;
import edu.bbte.idde.vnim2413.repository.RepositoryException;
import edu.bbte.idde.vnim2413.repository.UsedCarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class JdbcUsedCarRepository implements UsedCarRepository {

    private final ConnectionManager connectionManager = ConnectionManager.createInstance();
    private static final Logger LOG = LoggerFactory.getLogger(JdbcUsedCarRepository.class);

    @Override
    public long createUsedCarListing(UsedCarListing usedCarListing) throws RepositoryException {
        Connection connection = connectionManager.getConnection();
        try {
            LOG.info("""
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
                    LOG.info("Car listing created successfully with ID {}", id);
                    return id;
                }
                throw new RepositoryException("Failed to retrieve generated ID");
            }

        } catch (SQLException e) {
            LOG.error("Used car listing creation failed", e);
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
            LOG.info("Requested car listings from database");
            assert connection != null;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM used_car;");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                usedCars.add(mapResultToUsedCar(rs));
            }
        } catch (SQLException e) {
            LOG.error("Getting all used cars failed", e);
            throw new RepositoryException("Getting all used cars failed", e);
        } finally {
            if (connection != null) {
                connectionManager.returnConnection(connection);
            }
        }
        return usedCars;
    }

    @Override
    public UsedCarListing getUsedCar(long id) throws RepositoryException {
        Connection connection = connectionManager.getConnection();
        try {
            LOG.info("Requested used car listing from database, with ID: {}", id);
            assert connection != null;
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM used_car WHERE ID = ?;");
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapResultToUsedCar(rs);
            } else {
                LOG.info("No car listing found with ID: {}", id);
                throw new EntityNotFoundException("No car listing found with ID: " + id);
            }
        } catch (SQLException e) {
            LOG.error("Getting Used Car by ID failed", e);
            throw new RepositoryException("Getting Used car by ID failed", e);
        } finally {
            if (connection != null) {
                connectionManager.returnConnection(connection);
            }
        }
    }

    @Override
    public void updateUsedCar(UsedCarListing usedCarListing) throws RepositoryException {
        Connection connection = connectionManager.getConnection();
        try {
            LOG.info("Requested update on car listing with ID: {}", usedCarListing.getId());
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
                LOG.warn("No used car found to update with ID {}", usedCarListing.getId());
                throw new EntityNotFoundException("No used car found to update with ID {} " + usedCarListing.getId());
            }

            LOG.info("Successfully updated car listing with ID: {}", usedCarListing.getId());
        } catch (SQLException e) {
            LOG.error("Updating used car failed", e);
            throw new RepositoryException("Updating used car failed", e);
        } finally {
            if (connection != null) {
                connectionManager.returnConnection(connection);
            }
        }
    }

    @Override
    public void deleteUsedCar(long id) throws RepositoryException {
        Connection connection = connectionManager.getConnection();
        try {
            LOG.info("Requested deletion on car listing with ID: {}", id);
            assert connection != null;
            PreparedStatement ps = connection.prepareStatement("DELETE FROM used_car WHERE ID = ?;");
            ps.setLong(1, id);
            int rows = ps.executeUpdate();

            if (rows == 0) {
                LOG.warn("No used car found to delete with ID {}", id);
                throw new EntityNotFoundException("No used car found to delete with ID " + id);
            }

            LOG.info("Successfully deleted car listing with ID: {}", id);
        } catch (SQLException e) {
            LOG.error("Deleting used car failed", e);
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
            LOG.error("Checking used car failed", e);
            throw new RepositoryException("Checking used car failed", e);
        } finally {
            if (connection != null) {
                connectionManager.returnConnection(connection);
            }
        }
    }
}
