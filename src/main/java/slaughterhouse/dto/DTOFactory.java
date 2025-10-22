package slaughterhouse.dto;

import grpcslaughterhouse.DTOAnimal;
import grpcslaughterhouse.DTOProduct;
import slaughterhouse.domain.Animal;
import slaughterhouse.domain.Part;

public class DTOFactory
{
  public static DTOAnimal createDTOAnimal(Animal animal)
  {
    return DTOAnimal.newBuilder()
        .setId(animal.getId())
        .setWeight(animal.getWeight())
        .build();
  }

  public static DTOProduct createDTOProduct(Part part)
  {
    return DTOProduct.newBuilder()
        .setProductType(part.getType())
        .setId(part.getId())
        .build();
  }

}
