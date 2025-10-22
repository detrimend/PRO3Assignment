package slaughterhouse.server;

import grpcslaughterhouse.*;
import io.grpc.stub.StreamObserver;
import slaughterhouse.domain.Animal;
import slaughterhouse.domain.Product;
import slaughterhouse.dto.DTOFactory;
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

  @Override public void getAnimals(GetAnimalsRequest request,
      StreamObserver<GetAnimalsResponse> responseObserver)
  {
    Animal[] animals = animalDao.findAll().toArray(new Animal[0]);
    GetAnimalsResponse response = DTOFactory.createGetAnimalsResponse(animals);

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override public void getProducts(GetProductsRequest request,
      StreamObserver<GetProductsResponse> responseObserver)
  {
    Product[] products = productDao.findAll().toArray(new Product[0]);
    GetProductsResponse response = DTOFactory.createGetProductsResponse(products);

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void getAllAnimalsForProduct(GetAllAnimalsForProductRequest request,
      StreamObserver<GetAllAnimalsForProductResponse> responseObserver)
  {
    Animal[] animals = animalDao.getAllAnimalsByProduct(request.getId());
    GetAllAnimalsForProductResponse response = DTOFactory.createGetAllAnimalsForProductResponse(animals);

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }

  @Override
  public void getAllProductsForAnimal(GetAllProductsForAnimalRequest request,
      StreamObserver<GetAllProductsForAnimalResponse> responseObserver)
  {
    Product[] products = productDao.getAllProductsByAnimal(request.getId());
    GetAllProductsForAnimalResponse response = DTOFactory.createGetAllProductsForAnimalResponse(products);

    responseObserver.onNext(response);
    responseObserver.onCompleted();
  }
}
