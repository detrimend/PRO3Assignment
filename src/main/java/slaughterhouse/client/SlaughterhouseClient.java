package slaughterhouse.client;

import grpcslaughterhouse.GetAnimalsRequest;
import grpcslaughterhouse.GetAnimalsResponse;
import grpcslaughterhouse.SlaughterhouseServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import slaughterhouse.domain.Animal;
import slaughterhouse.dto.DTOFactory;

public class SlaughterhouseClient
{

    public static void main(String[] args)
    {
        new SlaughterhouseClient().run();
    }


    private ManagedChannel managedChannel = ManagedChannelBuilder
            .forAddress("localhost", 9090)
            .usePlaintext()
            .build();
    private SlaughterhouseServiceGrpc.SlaughterhouseServiceBlockingStub stub =
            SlaughterhouseServiceGrpc.newBlockingStub(managedChannel);


    private void run()
    {
        Animal[] animals = getPlanets();

        for( Planet p: planets ) {
            System.out.println(p.getName() + " " + p.getDistanceToTheSun() + " " + p.getRadius());

            Moon[] moons = getAllMoonsForPlanet( p.getName() );

            for (Moon m : moons)
                System.out.println("     " + m.getName() + " " + m.getDiscovered() + " " + m.getPlanet());

            System.out.println();
        }

        managedChannel.shutdown();
    }


    private Planet getPlanet(String name )
    {
        try {
            GetPlanetRequest request = DTOFactory.createGetPlanetRequest(name);
            GetPlanetResponse response = stub.getPlanet(request);

            return DTOFactory.createPlanet(response);
        } catch( Exception ex ) {
            ex.printStackTrace();

            return null;
        }
    }


    private Animal[] getAnimals()
    {
        try {
            GetAnimalsRequest request = DTOFactory.createGetAnimalsRequest();
            GetAnimalsResponse response = stub.getAnimals(request);

            return DTOFactory.createAnimals(response);
        } catch( Exception ex ) {
            ex.printStackTrace();

            return new Animal[0];
        }
    }


    private Moon getMoon(String name )
    {
        try {
            GetMoonRequest request = DTOFactory.createGetMoonRequest(name);
            GetMoonResponse response = stub.getMoon(request);

            return DTOFactory.createMoon(response);
        } catch( Exception ex ) {
            ex.printStackTrace();

            return null;
        }
    }


    private Moon[] getMoons()
    {
        try {
            GetMoonsRequest request = DTOFactory.createGetMoonsRequest();
            GetMoonsResponse response = stub.getMoons(request);

            return DTOFactory.createMoons(response);
        } catch( Exception ex ) {
            ex.printStackTrace();

            return new Moon[0];
        }
    }


    private Moon[] getAllMoonsForPlanet( String name )
    {
        try {
            GetAllMoonsForPlanetRequest request = DTOFactory.createGetAllMoonsForPlanetRequest( name );
            GetAllMoonsForPlanetResponse response = stub.getAllMoonsForPlanet(request);

            return DTOFactory.createMoons(response);
        } catch( Exception ex ) {
            ex.printStackTrace();

            return new Moon[0];
        }
    }
}
}
