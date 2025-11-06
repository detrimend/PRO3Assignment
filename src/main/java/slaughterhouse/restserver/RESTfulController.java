package slaughterhouse.restserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import slaughterhouse.domain.Animal;
import slaughterhouse.persistence.AnimalDAO;
import slaughterhouse.persistence.ProductDAO;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@RestController
public class RESTfulController {
    public AnimalDAO animalDao;
    public ProductDAO productDao;

    @Autowired
    public RESTfulController(AnimalDAO animalDao, ProductDAO productDao) {
        this.animalDao = animalDao;
        this.productDao = productDao;
    }

    @PostMapping("/register")
    public ResponseEntity<Animal> registerAnimal(@RequestBody Animal animal)
    {
        Animal saved = animalDao.addAnimal(animal);
        return new ResponseEntity<>(saved, HttpStatus.OK);
    }

    @GetMapping("/animals")
    public synchronized ResponseEntity<List<Animal>> getAllAnimals() {
        List<Animal> allAnimals = animalDao.findAll();
        return new ResponseEntity<>(allAnimals, HttpStatus.OK);
    }

    @GetMapping("/animals/{id}")
    public ResponseEntity<Animal> getAnimal(@PathVariable int id) {
        return animalDao.findById(id)
                .map(animal -> new ResponseEntity<>(animal, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/animals/{arrivalDate}")
    public ResponseEntity<Animal> getAnimalByDate(@PathVariable String arrivalDate) {
        return animalDao.findByDate(arrivalDate)
                .map(animal -> new ResponseEntity<>(animal, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }



}
