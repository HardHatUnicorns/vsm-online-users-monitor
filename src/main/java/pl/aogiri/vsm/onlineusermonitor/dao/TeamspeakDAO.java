package pl.aogiri.vsm.onlineusermonitor.dao;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import pl.aogiri.vsm.onlineusermonitor.adapter.ClientConnectionEventAdapter;

@Repository
public class TeamspeakDAO {
    private final Logger logger = LoggerFactory.getLogger(TeamspeakDAO.class);
    private TS3Query query;

    private static boolean WORKING = false;

    public void startCountUser(boolean withRecord){
        if (!isWORKING()){
            connectToServer();
            TS3Api api = query.getApi();
            api.registerAllEvents();
            api.addTS3Listeners(new ClientConnectionEventAdapter(api, withRecord));
            WORKING = true;
        }
    }

    public void stopCountUser(){
        if (isWORKING()) {
            TS3Api api = query.getApi();
            api.unregisterAllEvents();
            disconnectFromServer();
            WORKING = false;
        }
    }

    private void setupQuery(){
        TS3Config config = new TS3Config();
        config.setHost(SecretsDAO.HOST);
        query = new TS3Query(config);
    }

    private void connectToServer(){
        logger.info(SecretsDAO.LOGIN + " logged login");
        if(query == null)
            setupQuery();
        if (!query.isConnected()){
            query.connect();
            query.getApi().selectVirtualServerById(1);
            query.getApi().login(SecretsDAO.LOGIN, SecretsDAO.PASSWORD);
            query.getApi().setNickname(SecretsDAO.LOGIN);
        }
    }

    private void disconnectFromServer(){
        if (query != null && query.isConnected())
            query.exit();;
    }

    public static boolean isWORKING() {
        return WORKING;
    }
}
