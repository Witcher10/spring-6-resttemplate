package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerClientImplTest {

    @Autowired
    BeerClientImpl beerClient;

    @Test
    void listBeersBeerName() {
        beerClient.listBeers("ALE", null, null, null, false);
    }

    @Test
    void listBeersAll() {
        beerClient.listBeers();
    }

    @Test
    void listBeersBeerStyle() {
        beerClient.listBeers(null, BeerStyle.LAGER, null, null, false);
    }

    @Test
    void listBeersPageNumberAndPageSize() {
        beerClient.listBeers(null, null, 2, 50, false);
    }

    @Test
    void listBeersBeerNamePageNumberAndPageSizeShowInventoryTrue() {
        beerClient.listBeers("ALE", null, 2, 30, false);
    }

    @Test
    void listBeersBeerNameBeerStylePageNumberAndPageSizeShowInventoryTrue() {
        beerClient.listBeers("ALE", BeerStyle.PALE_ALE, 1, 50, true);
    }

    @Test
    void getBeerById() {
        Page<BeerDTO> beerDTOS = beerClient.listBeers();
        BeerDTO beerDTO = beerDTOS.getContent().get(0);
        BeerDTO byId = beerClient.getBeerById(beerDTO.getId());
        assertNotNull(byId);

    }

    @Test
    void createBeer() {
        BeerDTO newBeer = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(800)
                .upc("123456")
                .build();

        BeerDTO savedBeer = beerClient.createBeer(newBeer);
        assertNotNull(savedBeer);
    }

    @Test
    void updateBeerById() {
        BeerDTO newBeer = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(800)
                .upc("123456")
                .build();

        BeerDTO savedBeer = beerClient.createBeer(newBeer);

        final String newName = "Beer Name Updated";
        savedBeer.setBeerName(newName);

        BeerDTO updatedBeer = beerClient.updateBeerById(savedBeer);
        assertEquals(newName,updatedBeer.getBeerName());
    }

    @Test
    void deleteBeerById() {
        BeerDTO newBeer = BeerDTO.builder()
                .price(new BigDecimal("10.99"))
                .beerName("Mango Bobs")
                .beerStyle(BeerStyle.IPA)
                .quantityOnHand(800)
                .upc("123456")
                .build();

        BeerDTO savedBeer = beerClient.createBeer(newBeer);

        beerClient.deleteBeerById(savedBeer.getId());

        assertThrows(HttpClientErrorException.class , () ->
                beerClient.getBeerById(savedBeer.getId()));
    }
}