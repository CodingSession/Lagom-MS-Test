package de.ewks.funcdemo.service;

import de.ewks.funcdemo.domain.API_Reponse;
import io.reactivex.Flowable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class API_Service {

    public abstract Flowable<API_Reponse> getAPI_Response();
    public abstract API_Reponse getAPI_Response_Normal();


}
