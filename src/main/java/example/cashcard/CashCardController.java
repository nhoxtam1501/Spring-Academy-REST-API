package example.cashcard;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;


@RestController
@RequestMapping("/cashcards")
class CashCardController {

    private final CashCardRepository cashCardRepository;

    private CashCardController(CashCardRepository cashCardRepository) {
        this.cashCardRepository = cashCardRepository;
    }

    /* @PathVariable makes Spring Web aware of the requestedId supplied in the HTTP request.
        Now it’s available for us to use in our handler method.*/
    @GetMapping("{id}")
    private ResponseEntity<CashCard> findById(@PathVariable Long id) {
        Optional<CashCard> cashCardOptional = cashCardRepository.findById(id);

        if (cashCardOptional.isPresent()) {
            return ResponseEntity.ok(cashCardOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping                /*The POST expects a request "body". This contains the data submitted to the API.
                                  Spring Web will deserialize the data into a CashCard for us.*/
//  UriComponentsBuilder is injected by IoC Container
    private ResponseEntity<Void> createCashCard(@RequestBody CashCard newCashCardRequest, UriComponentsBuilder ucb) {
        CashCard savedCashCard = cashCardRepository.save(newCashCardRequest);

        /*This is constructing a URI to the newly created CashCard.
         This is the URI that the caller can then use to GET the newly-created CashCard*/
        URI locationOfNewCashCard = ucb.path("cashcards/{id}").buildAndExpand(savedCashCard.id()).toUri();

        return ResponseEntity.created(locationOfNewCashCard).build();


    }
}