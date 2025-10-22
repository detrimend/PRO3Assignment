package slaughterhouse.dto;

import grpcslaughterhouse.*;
import slaughterhouse.domain.Animal;
import slaughterhouse.domain.Product;

import java.util.ArrayList;

public class DTOFactory
{
  public static DTOAnimal createDTOAnimal(Animal animal)
  {
    return DTOAnimal.newBuilder()
        .setId(animal.getId())
        .setWeight(animal.getWeight())
        .build();
  }

  public static DTOProduct createDTOProduct(Product product)
  {
    return DTOProduct.newBuilder()
        .setProductType(product.getType())
        .setId(product.getId())
        .build();
  }

  public static GetAllAnimalsForProductRequest createGetAllAnimalsForProductRequest(int productId)
  {
    return GetAllAnimalsForProductRequest.newBuilder()
        .setId(productId)
        .build();
  }

 public static GetAllAnimalsForProductResponse createGetAllAnimalsForProductResponse(Animal[] animals)
 {
   ArrayList<DTOAnimal> list = new ArrayList<>();
   for(Animal a: animals)
     list.add(DTOAnimal.newBuilder()
         .setId(a.getId())
         .setWeight(a.getWeight())
         .build());

   return GetAllAnimalsForProductResponse.newBuilder().addAllAnimals(list).build();
 }

 public static GetAnimalsRequest createGetAnimalsRequest()
 {
   return GetAnimalsRequest.newBuilder().build();
 }

  public static GetAnimalsResponse createGetAnimalsResponse(Animal[] animals)
  {
    ArrayList<DTOAnimal> list = new ArrayList<>();
    for(Animal a: animals)
      list.add(DTOAnimal.newBuilder()
          .setId(a.getId())
          .setWeight(a.getWeight())
          .build());

    return GetAnimalsResponse.newBuilder().addAllAnimals(list).build();
  }

 public static Animal createAnimal(DTOAnimal dtoAnimal)
    {
        return new Animal(
            dtoAnimal.getWeight()
        );
    }
    public static Animal createAnimal(GetAnimalsResponse r)
    {
        return createAnimal(r.getAnimals(0));
    }

  public static Animal [] createAnimals(GetAnimalsResponse r)
  {
      Animal[] res= new Animal[r.getAnimalsCount()];
        for(int i=0;i<r.getAnimalsCount();i++)
            res[i] = createAnimal(r.getAnimals(i));
      return res;

  }

    public static Animal [] createAnimalsForProduct(GetAllAnimalsForProductResponse r)
    {
        Animal[] res= new Animal[r.getAnimalsCount()];
        for(int i=0;i<r.getAnimalsCount();i++)
            res[i] = createAnimal(r.getAnimals(i));
        return res;

    }

  public static GetProductsRequest createGetProductsRequest()
  {
    return GetProductsRequest.newBuilder().build();
  }

  public static GetProductsResponse createGetProductsResponse(Product[] products)
  {
    ArrayList<DTOProduct> list = new ArrayList<>();
    for(Product p: products)
      list.add(DTOProduct.newBuilder()
          .setProductType(p.getType())
          .setId(p.getId())
          .build());

    return GetProductsResponse.newBuilder().addAllProducts(list).build();
  }

  public static GetAllProductsForAnimalRequest createGetAllProductsForAnimalRequest(
      int animalId)
  {
    return GetAllProductsForAnimalRequest.newBuilder()
        .setId(animalId)
        .build();
  }

  public static GetAllProductsForAnimalResponse createGetAllProductsForAnimalResponse(Product[] products)
  {
    ArrayList<DTOProduct> list = new ArrayList<>();
    for(Product p: products)
      list.add(DTOProduct.newBuilder()
          .setProductType(p.getType())
          .setId(p.getId())
          .build());

    return GetAllProductsForAnimalResponse.newBuilder().addAllProducts(list).build();
  }
}
