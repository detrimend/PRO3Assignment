package slaughterhouse.persistence;

import slaughterhouse.domain.Product;
import slaughterhouse.domain.Tray;

import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;

public interface ProductDAO
{
  Optional<Product> findById(int id);

  List<Product> findAll();

  List<Product> findByType(String productType);

  // Loads Product and populates trayMap using a provided tray loader by id.
  Optional<Product> findByIdWithTrays(int id, IntFunction<Tray> trayLoader);

  Product[] getAllProductsByAnimal(int animalId);
}