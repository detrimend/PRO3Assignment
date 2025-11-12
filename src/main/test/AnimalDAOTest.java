import org.junit.Test;
import slaughterhouse.persistence.AnimalDAOImpl;
import slaughterhouse.domain.Animal;

import java.util.List;

import static org.junit.Assert.*;

public class AnimalDAOTest
{

  @Test public void testFindByWeightBetween_Zero() throws Exception
  {
    AnimalDAOImpl dao = AnimalDAOImpl.getInstance();
    List<Animal> animals = dao.findByWeightBetween(-1000, -500);
    assertNotNull(animals);
    assertTrue(animals.isEmpty());
  }

  @Test public void testFindByWeightBetween_One() throws Exception
  {
    AnimalDAOImpl dao = AnimalDAOImpl.getInstance();
    List<Animal> animals = dao.findByWeightBetween(140.0, 140.0);
    assertNotNull(animals);
    assertTrue(animals.size() <= 1);
    for (Animal animal : animals)
    {
      assertEquals(150.0, animal.getWeight(), 0.001);
    }
  }

  @Test public void testFindByWeightBetween_Many() throws Exception
  {
    AnimalDAOImpl dao = AnimalDAOImpl.getInstance();
    List<Animal> animals = dao.findByWeightBetween(100.0, 1000.0);
    assertNotNull(animals);
    assertTrue(animals.size() > 1);
    for (Animal animal : animals)
    {
      assertTrue(animal.getWeight() >= 100.0 && animal.getWeight() <= 1000.0);
    }
  }

  @Test public void testFindByWeightBetween_Boundary() throws Exception
  {
    AnimalDAOImpl dao = AnimalDAOImpl.getInstance();
    // Assuming there are animals with weights exactly 100.0 and 200.0
    List<Animal> animals = dao.findByWeightBetween(100.0, 200.0);
    assertNotNull(animals);
    boolean foundMin = false, foundMax = false;
    for (Animal animal : animals)
    {
      if (animal.getWeight() == 100.0)
        foundMin = true;
      if (animal.getWeight() == 200.0)
        foundMax = true;
    }
    assertTrue(foundMin || foundMax);
  }

  @Test(expected = RuntimeException.class) public void testFindByWeightBetween_Exception()
      throws Exception
  {
    AnimalDAOImpl dao = AnimalDAOImpl.getInstance();
    // Simulate error by passing NaN
    dao.findByWeightBetween(Double.NaN, Double.NaN);
  }
}