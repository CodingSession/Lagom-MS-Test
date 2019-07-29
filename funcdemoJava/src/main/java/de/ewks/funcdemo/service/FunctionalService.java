package de.ewks.funcdemo.service;

import de.ewks.funcdemo.domain.API_Reponse;
import de.ewks.funcdemo.domain.Response;
import de.ewks.funcdemo.repository.API_Collection;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class FunctionalService {

    private final
    API_Collection apis;

    @Autowired
    public FunctionalService(API_Collection apis) {
        this.apis = apis;
    }


    public Flowable<Response> calculateWindowMoving() {
        return Flowable.fromIterable(apis.getAllServices())
                .flatMap(api_service -> {
                    return api_service.getAPI_Response()
                            .map(API_Reponse::getValue)
                            .doOnError(Throwable::printStackTrace)
                            .subscribeOn(Schedulers.io());
                })
                .window(50, TimeUnit.MILLISECONDS)
                .flatMap(doubleFlowable -> {
                    return doubleFlowable.reduce(new Response(), Response::add).toFlowable();
                })
                .scan(new Response(), ((response, response2) -> {
                    response.add(response2);
                    return response;
                }))
                .map(Response::calcAvgAndGet);


    }

    public Single<Response> calculateComplete() {
        return Flowable.fromIterable(apis.getAllServices())
                .flatMap(api_service -> {
                    return api_service.getAPI_Response()
                            .map(API_Reponse::getValue)
                            .doOnError(Throwable::printStackTrace)
                            .subscribeOn(Schedulers.io());
                })
                .reduce(new Response(), Response::add)
                .map(Response::calcAvgAndGet);
    }


    public Response calculateStreamOnly() {
        return apis.getAllServices().stream()
                .map(API_Service::getAPI_Response_Normal)
                .reduce(new Response(), (response, api_reponse) -> {
                    response.add(api_reponse.getValue());
                    return response;
                }, (response, response2) -> {
                    response.add(response2);
                    return response;
                }).calcAvgAndGet();

    }


}
