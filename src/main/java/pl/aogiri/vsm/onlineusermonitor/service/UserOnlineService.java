package pl.aogiri.vsm.onlineusermonitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.aogiri.vsm.onlineusermonitor.dao.TeamspeakDAO;

@Service
public class UserOnlineService {

    @Autowired
    TeamspeakDAO dao;

    private static final Boolean STATUS = false;

    public void start(){
        dao.startCountUser(true);
    }

    public void stop(){
        dao.stopCountUser();
    }

    public boolean getStatus(){
        return TeamspeakDAO.isWORKING();
    }
}
