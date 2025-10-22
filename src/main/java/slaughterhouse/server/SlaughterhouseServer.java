package slaughterhouse.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import slaughterhouse.persistence.AnimalDAO;
import slaughterhouse.persistence.AnimalDAOImpl;
import slaughterhouse.persistence.ProductDAO;
import slaughterhouse.persistence.ProductDAOImpl;

public class SlaughterhouseServer
{
  private AnimalDAO animalDAO;
  private ProductDAO productDAO;

  public static void main(String[] args) throws Exception
  {
    new SlaughterhouseServer().run();
  }

  private void run() throws Exception
  {
    AnimalDAO animalDAO = AnimalDAOImpl.getInstance();
    ProductDAO productDAO = ProductDAOImpl.getInstance();

    Server server = ServerBuilder.forPort(9090)
        .addService(new SlaughterhouseServiceImpl(animalDAO, productDAO)).build();

    server.start();
    server.awaitTermination();
  }
}
