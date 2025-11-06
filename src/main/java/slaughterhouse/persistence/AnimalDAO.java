package slaughterhouse.persistence;

import org.springframework.stereotype.Component;
import slaughterhouse.domain.Animal;

import java.util.List;
import java.util.Optional;

@Component
public interface AnimalDAO
{
  Optional<Animal> findById(int id);

  List<Animal> findAll();

  List<Animal> findByWeightBetween(double minInclusive, double maxInclusive);

  Animal[] getAllAnimalsByProduct(int productId);
}