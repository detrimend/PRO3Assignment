package slaughterhouse.persistence;

import slaughterhouse.domain.Animal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AnimalDAOImpl implements AnimalDAO
{
  private static AnimalDAOImpl instance;

  private AnimalDAOImpl() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  private static Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres?currentSchema=slaughterhouse",
        "postgres", "123");
  }

  public static AnimalDAOImpl getInstance() throws SQLException
  {
    if (instance == null)
    {
      instance = new AnimalDAOImpl();
    }
    return instance;
  }

  @Override public Optional<Animal> findById(int id)
  {
    final String sql = "SELECT id, weight FROM animals WHERE id = ?";
    try (Connection con = instance.getConnection();
        PreparedStatement ps = con.prepareStatement(sql))
    {
      ps.setInt(1, id);
      try (ResultSet rs = ps.executeQuery())
      {
        return rs.next() ? Optional.of(mapRow(rs)) : Optional.empty();
      }
    }
    catch (SQLException e)
    {
      throw new RuntimeException("Failed to fetch Animal by id=" + id, e);
    }
  }

  @Override public List<Animal> findAll()
  {
    final String sql = "SELECT id, weight FROM animals ORDER BY id";
    try (Connection con = instance.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery())
    {
      List<Animal> result = new ArrayList<>();
      while (rs.next())
      {
        result.add(mapRow(rs));
      }
      return result;
    }
    catch (SQLException e)
    {
      throw new RuntimeException("Failed to fetch all Animals", e);
    }
  }

  @Override public List<Animal> findByWeightBetween(double minInclusive, double maxInclusive)
  {
    final String sql = "SELECT id, weight FROM animals WHERE weight BETWEEN ? AND ? ORDER BY weight, id";
    try (Connection con = instance.getConnection();
        PreparedStatement ps = con.prepareStatement(sql))
    {
      ps.setDouble(1, minInclusive);
      ps.setDouble(2, maxInclusive);
      try (ResultSet rs = ps.executeQuery())
      {
        List<Animal> result = new ArrayList<>();
        while (rs.next())
        {
          result.add(mapRow(rs));
        }
        return result;
      }
    }
    catch (SQLException e)
    {
      throw new RuntimeException(
          "Failed to fetch Animals by weight range [" + minInclusive + ", " + maxInclusive + "]", e);
    }
  }

  @Override public Animal[] getAllAnimalsByProduct(int productId)
  {
    final String sql =
        "SELECT DISTINCT a.id, a.weight " +
        "FROM product_trays pt " +
        "JOIN tray_parts tp ON tp.tray_id = pt.tray_id " +
        "JOIN parts p ON p.id = tp.part_id " +
        "JOIN animals a ON a.id = p.animal_id " +
        "WHERE pt.product_id = ? " +
        "ORDER BY a.id";

    try (Connection con = instance.getConnection();
         PreparedStatement ps = con.prepareStatement(sql))
    {
      ps.setInt(1, productId);
      try (ResultSet rs = ps.executeQuery())
      {
        List<Animal> result = new ArrayList<>();
        while (rs.next())
        {
          result.add(mapRow(rs));
        }
        return result.toArray(new Animal[0]);
      }
    }
    catch (SQLException e)
    {
      throw new RuntimeException("Failed to fetch Animals for product id=" + productId, e);
    }
  }

  private Animal mapRow(ResultSet rs) throws SQLException
  {
    int id = rs.getInt("id");
    double weight = rs.getDouble("weight");
    return new Animal(id, weight);
  }
}
