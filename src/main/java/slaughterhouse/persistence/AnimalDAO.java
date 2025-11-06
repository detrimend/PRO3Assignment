package slaughterhouse.persistence;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import slaughterhouse.domain.Animal;

import java.util.List;
import java.util.Optional;



public interface AnimalDAO
{
  Optional<Animal> findById(int id);

  Optional<Animal> findByDate(String Date);

  List<Animal> findAll();

  List<Animal> findByWeightBetween(double minInclusive, double maxInclusive);

  Animal[] getAllAnimalsByProduct(int productId);

  Animal addAnimal(Animal animal);


}