package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.springframework.data.domain.Page;

import java.util.UUID;

public interface BeerClient {

    Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Integer pageNumber, Integer pageSize, Boolean showInventory);

    Page<BeerDTO> listBeers();

    BeerDTO getBeerById(UUID beerId);

    BeerDTO createBeer(BeerDTO newBeer);

    BeerDTO updateBeerById(BeerDTO savedBeer);

    void deleteBeerById(UUID id);
}
