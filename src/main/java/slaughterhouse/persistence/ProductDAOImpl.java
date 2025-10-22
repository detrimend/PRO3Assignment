package slaughterhouse.persistence;

import slaughterhouse.domain.Product;
import slaughterhouse.domain.Tray;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;

public class ProductDAOImpl implements ProductDAO
{
  private static ProductDAOImpl instance;

  private ProductDAOImpl() throws SQLException
  {
    DriverManager.registerDriver(new org.postgresql.Driver());
  }

  private static Connection getConnection() throws SQLException
  {
    return DriverManager.getConnection(
        "jdbc:postgresql://localhost:5432/postgres?currentSchema=slaughterhouse",
        "postgres", "123");
  }

  public static ProductDAOImpl getInstance() throws SQLException
  {
    if (instance == null)
    {
      instance = new ProductDAOImpl();
    }
    return instance;
  }

  @Override public Optional<Product> findById(int id)
  {
    final String sql = "SELECT id, product_type FROM products WHERE id = ?";
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
      throw new RuntimeException("Failed to fetch Product by id=" + id, e);
    }
  }

  @Override public List<Product> findAll()
  {
    final String sql = "SELECT id, product_type FROM products ORDER BY id";
    try (Connection con = instance.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery())
    {
      List<Product> result = new ArrayList<>();
      while (rs.next())
      {
        result.add(mapRow(rs));
      }
      return result;
    }
    catch (SQLException e)
    {
      throw new RuntimeException("Failed to fetch all Products", e);
    }
  }

  @Override public List<Product> findByType(String productType)
  {
    final String sql = "SELECT id, product_type FROM products WHERE product_type = ? ORDER BY id";
    try (Connection con = instance.getConnection();
        PreparedStatement ps = con.prepareStatement(sql))
    {
      ps.setString(1, productType);
      try (ResultSet rs = ps.executeQuery())
      {
        List<Product> result = new ArrayList<>();
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
          "Failed to fetch Products by type=" + productType, e);
    }
  }

  @Override public Optional<Product> findByIdWithTrays(int id,
      IntFunction<Tray> trayLoader)
  {
    Optional<Product> opt = findById(id);
    opt.ifPresent(p -> loadTrayMap(p, trayLoader));
    return opt;
  }

  @Override public Product[] getAllProductsByAnimal(int animalId)
  {
    final String sql =
        "SELECT DISTINCT pr.id, pr.product_type " +
            "FROM parts pa " +
            "JOIN tray_parts tp ON tp.part_id = pa.id " +
            "JOIN product_trays pt ON pt.tray_id = tp.tray_id " +
            "JOIN products pr ON pr.id = pt.product_id " +
            "WHERE pa.animal_id = ? " +
            "ORDER BY pr.id";

    try (Connection con = instance.getConnection();
        PreparedStatement ps = con.prepareStatement(sql))
    {
      ps.setInt(1, animalId);
      try (ResultSet rs = ps.executeQuery())
      {
        List<Product> result = new ArrayList<>();
        while (rs.next())
        {
          result.add(mapRow(rs));
        }
        return result.toArray(new Product[0]);
      }
    }
    catch (SQLException e)
    {
      throw new RuntimeException("Failed to fetch Products for animal id=" + animalId, e);
    }
  }

  private Product mapRow(ResultSet rs) throws SQLException
  {
    int id = rs.getInt("id");
    String type = rs.getString("product_type");
    Product product = new Product(type); // constructor increments in-memory id
    product.setId(id);                   // override with DB id
    return product;
  }

  private void loadTrayMap(Product product, IntFunction<Tray> trayLoader)
  {
    final String sql = "SELECT tray_id, label FROM product_trays WHERE product_id = ? ORDER BY tray_id";
    try (Connection con = instance.getConnection();
        PreparedStatement ps = con.prepareStatement(sql))
    {
      ps.setInt(1, product.getId());
      try (ResultSet rs = ps.executeQuery())
      {
        HashMap<Tray, String> map = getOrInitTrayMap(product);
        while (rs.next())
        {
          int trayId = rs.getInt("tray_id");
          String label = rs.getString("label");
          Tray tray = trayLoader.apply(trayId);
          if (tray != null)
          {
            map.put(tray, label);
          }
        }
      }
    }
    catch (SQLException e)
    {
      throw new RuntimeException(
          "Failed to load trayMap for Product id=" + product.getId(), e);
    }
  }

  @SuppressWarnings("unchecked") private HashMap<Tray, String> getOrInitTrayMap(
      Product product)
  {
    try
    {
      Field f = Product.class.getDeclaredField("trayMap");
      f.setAccessible(true);
      Object current = f.get(product);
      if (current == null)
      {
        HashMap<Tray, String> map = new HashMap<>();
        f.set(product, map);
        return map;
      }
      return (HashMap<Tray, String>) current;
    }
    catch (ReflectiveOperationException e)
    {
      throw new RuntimeException(
          "Failed to access Product.trayMap via reflection", e);
    }
  }
}