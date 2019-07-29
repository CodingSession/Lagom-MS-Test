package de.ewks.funcdemo.web.rest;


import de.ewks.funcdemo.domain.Response;
import de.ewks.funcdemo.repository.API_Collection;
import de.ewks.funcdemo.service.API_Service;
import de.ewks.funcdemo.service.FunctionalService;
import io.reactivex.Flowable;
import io.reactivex.Single;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/java/")
public class RequestHandler {
    private final FunctionalService functionalService;
    private final API_Collection api_collection;


    @Autowired
    public RequestHandler(FunctionalService functionalService, API_Collection api_collection) {
        this.functionalService = functionalService;
        this.api_collection = api_collection;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/moving")
    public Flowable<Response> getMovingAverage() {
        return functionalService.calculateWindowMoving();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/reactive")
    public Single<Response> getReactvie() {
        return functionalService.calculateComplete();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/functional")
    public Response getFunctional() {
        return functionalService.calculateStreamOnly();
    }

    @PostMapping("/api")
    public void addAPI(@RequestBody String adress) {
        api_collection.addApi(adress);
    }


}
