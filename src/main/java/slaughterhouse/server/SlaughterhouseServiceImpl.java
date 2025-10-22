package slaughterhouse.server;

import grpcslaughterhouse.SlaughterhouseServiceGrpc;
import slaughterhouse.persistence.AnimalDAO;
import slaughterhouse.persistence.ProductDAO;

public class SlaughterhouseServiceImpl
    extends SlaughterhouseServiceGrpc.SlaughterhouseServiceImplBase
{
  private AnimalDAO animalDao;
  private ProductDAO productDao;

  public SlaughterhouseServiceImpl(AnimalDAO animalDao, ProductDAO productDao)
  {
    this.animalDao = animalDao;
    this.productDao = productDao;
  }
}
