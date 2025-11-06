package slaughterhouse.persistence;

import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Repository;
import slaughterhouse.domain.Animal;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class AnimalDAOImpl implements AnimalDAO {
    private static AnimalDAOImpl instance;

    private AnimalDAOImpl() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to register PostgreSQL driver", e);
        }
    }

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/postgres?currentSchema=slaughterhouse",
                "postgres", "123");
    }

    public static AnimalDAOImpl getInstance() throws SQLException {
        if (instance == null) {
            instance = new AnimalDAOImpl();
        }
        return instance;
    }

    @Override
    public Optional<Animal> findById(int id) {
        final String sql = "SELECT id, weight, origin, arrival_date FROM animals WHERE id = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch Animal by id=" + id, e);
        }
    }

    @Override
    public Optional<Animal> findByDate(String date) {
        final String sql = "SELECT id, weight, origin, arrival_date FROM animals WHERE arrival_date = ?";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, date);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch Animal by date=" + date, e);
        }
    }


    @Override
    public List<Animal> findAll() {
        final String sql = "SELECT id, weight, origin FROM animals ORDER BY id";
        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Animal> result = new ArrayList<>();
            while (rs.next()) {
                result.add(mapRow(rs));
            }
            return result;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch all Animals", e);
        }
    }

    @Override
    public List<Animal> findByWeightBetween(double minInclusive, double maxInclusive) {
        final String sql = "SELECT id, weight FROM animals WHERE weight BETWEEN ? AND ? ORDER BY weight, id";
        try (Connection con = instance.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, minInclusive);
            ps.setDouble(2, maxInclusive);
            try (ResultSet rs = ps.executeQuery()) {
                List<Animal> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
                return result;
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    "Failed to fetch Animals by weight range [" + minInclusive + ", " + maxInclusive + "]", e);
        }
    }

    @Override
    public Animal[] getAllAnimalsByProduct(int productId) {
        final String sql =
                "SELECT DISTINCT a.id, a.weight " +
                        "FROM product_trays pt " +
                        "JOIN tray_parts tp ON tp.tray_id = pt.tray_id " +
                        "JOIN parts p ON p.id = tp.part_id " +
                        "JOIN animals a ON a.id = p.animal_id " +
                        "WHERE pt.product_id = ? " +
                        "ORDER BY a.id";

        try (Connection con = instance.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                List<Animal> result = new ArrayList<>();
                while (rs.next()) {
                    result.add(mapRow(rs));
                }
                return result.toArray(new Animal[0]);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch Animals for product id=" + productId, e);
        }
    }

    @Override
    public Animal addAnimal(Animal animal) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO animals ( weight, origin) VALUES (?,?)");) {

            statement.setDouble(1, animal.getWeight());
            statement.setString(2, animal.getOrigin());
            statement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return animal;
    }

    private Animal mapRow(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        double weight = rs.getDouble("weight");
        String origin = rs.getString("origin");

        return new Animal(id, weight, origin);
    }
}
