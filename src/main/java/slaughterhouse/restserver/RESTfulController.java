package slaughterhouse.restserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import slaughterhouse.persistence.AnimalDAO;
import slaughterhouse.persistence.ProductDAO;

@RestController
public class RESTfulController
{
  public AnimalDAO animalDao;
  public ProductDAO productDao;

  @Autowired
  public RESTfulController(AnimalDAO animalDao, ProductDAO productDao) {
   this.animalDao = animalDao;
   this.productDao = productDao;
  }
}
