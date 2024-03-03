package guru.springframework.spring6resttemplate.client;

import guru.springframework.spring6resttemplate.model.BeerDTO;
import guru.springframework.spring6resttemplate.model.BeerDTOPageImpl;
import guru.springframework.spring6resttemplate.model.BeerStyle;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BeerClientImpl implements BeerClient {

    private final RestTemplateBuilder restTemplateBuilder;
   // private static final String BASE_URL = "http://localhost:8080/";
    public static final String GET_BEER_PATH = "/api/v1/beer";
    public static final String GET_BEER_BY_ID_PATH = "/api/v1/beer/{beerId}";


    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Integer pageNumber, Integer pageSize, Boolean showInventory) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromPath(GET_BEER_PATH);

        if(beerName != null)
            uriComponentsBuilder.queryParam("beerName",beerName);
        if(beerStyle != null)
            uriComponentsBuilder.queryParam("beerStyle",beerStyle);
        if(pageNumber != null)
            uriComponentsBuilder.queryParam("pageNumber",pageNumber);
        if(pageSize != null)
            uriComponentsBuilder.queryParam("pageSize",pageSize);
        if(showInventory != null)
            uriComponentsBuilder.queryParam("showInventory",showInventory);

        ResponseEntity<BeerDTOPageImpl> response = restTemplate.getForEntity(uriComponentsBuilder.toUriString(), BeerDTOPageImpl.class);

  /*      ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL + GET_BEER_PATH,String.class);
        ResponseEntity<Map> responseMap = restTemplate.getForEntity(BASE_URL + GET_BEER_PATH,Map.class);
        ResponseEntity<JsonNode> responseJson = restTemplate.getForEntity(BASE_URL + GET_BEER_PATH, JsonNode.class);
       responseJson.getBody().findPath("content").elements()
                .forEachRemaining(node ->  {
                    System.out.println(node.get("beerName").asText());
                });
        System.out.println(response.getBody());*/
        return response.getBody();
    }

    @Override
    public Page<BeerDTO> listBeers() {
      return this.listBeers(null,null,null,null,null);
    }

    @Override
    public BeerDTO getBeerById(UUID beerId) {
        RestTemplate restTemplate = restTemplateBuilder.build();
       return  restTemplate.getForObject(GET_BEER_BY_ID_PATH,BeerDTO.class, beerId);
    }

    @Override
    public BeerDTO createBeer(BeerDTO newBeer) {
        RestTemplate restTemplate = restTemplateBuilder.build();
        //ResponseEntity<BeerDTO> response = restTemplate.postForEntity(GET_BEER_PATH,newBeer,BeerDTO.class);
        URI uri = restTemplate.postForLocation(GET_BEER_PATH,newBeer);
        return restTemplate.getForObject(uri.getPath(),BeerDTO.class);
    }

    @Override
    public BeerDTO updateBeerById(BeerDTO savedBeer) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        restTemplate.put(GET_BEER_BY_ID_PATH,savedBeer,savedBeer.getId());
        return this.getBeerById(savedBeer.getId());
    }

    @Override
    public void deleteBeerById(UUID id) {
        RestTemplate restTemplate = restTemplateBuilder.build();

        restTemplate.delete(GET_BEER_BY_ID_PATH,id);
    }
}
