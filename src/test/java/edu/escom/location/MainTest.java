package edu.escom.location;

import com.google.maps.errors.ApiException;
import edu.escom.location.model.Location;
import edu.escom.location.service.LocationService;
import org.junit.Test;

import java.io.IOException;
import java.util.Objects;

public class MainTest {

    private final String[] KEYS = {
            "AIzaSyDrv7h4rBsAgLZWlDN7vHEZDcPMSmuQZ-M",
            "AIzaSyD6zxUmLEKLCzD-2H9-LIk1kdNqEzXKEgU",
            "AIzaSyA1axUtDYDERLZAIGv3T4UtjDfmd9Gl1Eo",
            "AIzaSyDhuFclu3A58UhKHfpLMVz6YFH6W2Ys4sM"
    };

    @Test
    public void searchByString(){
        String addresses[] = {
                "Calle Rio lerma 4, Colonia Renacimiento",
                "Calle 34, 100A Maravillas",
                "Constructores 46, Tlatel Xochitenco",
                "Rio Marne 2, Colonia Renacimiento"
        };

        int contadorKeys = 0;

        for(String address: addresses){
            try {
                Location location = LocationService.getInstance().searchByString(address);
                System.out.println("\n\n");
                if(Objects.nonNull(location)){
                    System.out.println(location.toString());
                    System.out.println("CALLE: "  + location.getStreet());
                    System.out.println("NUMERO: " + location.getStreetNumber());
                    System.out.println("NUMERO COMPLETO: " +location.getStreetNumberComplement());
                    System.out.println("COLONIA: " + location.getDistrict());
                    System.out.println("DELEG: " + location.getCity());
                    System.out.println("PRECISION: " + location.getPrecision());
                    System.out.println("LAT: " + location.getCoordinate().getLatitude());
                    System.out.println("LNG: " + location.getCoordinate().getLongitude());
                }else{
                    System.out.println(" NO SE ENCONTRO -> " + address);
                }
            } catch (ApiException | InterruptedException | IOException e) {
                String currentKey = KEYS[contadorKeys];
                LocationService.getInstance().setApiKey(currentKey);
                contadorKeys++;

                if(contadorKeys == KEYS.length-1){
                    System.err.println("YA NO HAY LLAVES PARA PROBAR");
                }
            }
        }
    }
}