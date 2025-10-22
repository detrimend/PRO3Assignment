package slaughterhouse.persistence;

import slaughterhouse.domain.Animal;

import java.util.List;
import java.util.Optional;

public interface AnimalDAO
{
  Optional<Animal> findById(int id);

  List<Animal> findAll();

  List<Animal> findByWeightBetween(double minInclusive, double maxInclusive);

  Animal[] getAllAnimalsByProduct(int productId);
}