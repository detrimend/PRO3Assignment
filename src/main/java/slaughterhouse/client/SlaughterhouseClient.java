package slaughterhouse.client;

import grpcslaughterhouse.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import slaughterhouse.domain.Animal;
import slaughterhouse.domain.Product;
import slaughterhouse.dto.DTOFactory;

public class SlaughterhouseClient
{

  public static void main(String[] args)
  {
    new SlaughterhouseClient().run();
  }

  private ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(
      "localhost", 9090).usePlaintext().build();
  private SlaughterhouseServiceGrpc.SlaughterhouseServiceBlockingStub stub = SlaughterhouseServiceGrpc.newBlockingStub(
      managedChannel);

  private void run()
  {
    Animal[] animals = getAllAnimalsForProduct(4);

    for (Animal a : animals)
    {
      System.out.println(a.getId() + " " + a.getWeight());
    }

    Product[] products = getAllProductsForAnimal(12);

    for (Product p : products)
    {
      System.out.println(p.getId() + " " + p.getType());
    }

    managedChannel.shutdown();
  }

  private Animal[] getAnimals()
  {
    try
    {
      GetAnimalsRequest request = DTOFactory.createGetAnimalsRequest();
      GetAnimalsResponse response = stub.getAnimals(request);

      return DTOFactory.createAnimals(response);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();

      return new Animal[0];
    }
  }

  private Animal[] getAllAnimalsForProduct(int productId)
  {
    try
    {
      GetAllAnimalsForProductRequest request = DTOFactory.createGetAllAnimalsForProductRequest(
          productId);
      GetAllAnimalsForProductResponse response = stub.getAllAnimalsForProduct(
          request);

      return DTOFactory.createAnimalsForProduct(response);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();

      return new Animal[0];
    }
  }

  private Product[] getProducts()
  {
    try
    {
      GetProductsRequest request = DTOFactory.createGetProductsRequest();
      GetProductsResponse response = stub.getProducts(request);
      return DTOFactory.createProducts(response);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();

      return new Product[0];
    }
  }

  private Product[] getAllProductsForAnimal(int animalId)
  {
    try
    {
      GetAllProductsForAnimalRequest request = DTOFactory.createGetAllProductsForAnimalRequest(
          animalId);
      GetAllProductsForAnimalResponse response = stub.getAllProductsForAnimal(
          request);
      return DTOFactory.createProductsForAnimal(response);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();

      return new Product[0];
    }
  }

}
