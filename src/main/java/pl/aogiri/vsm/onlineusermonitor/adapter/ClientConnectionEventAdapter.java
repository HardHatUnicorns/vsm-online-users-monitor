package pl.aogiri.vsm.onlineusermonitor.adapter;

import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.ChannelProperty;
import com.github.theholywaffle.teamspeak3.api.event.ClientJoinEvent;
import com.github.theholywaffle.teamspeak3.api.event.ClientLeaveEvent;
import com.github.theholywaffle.teamspeak3.api.event.TS3EventAdapter;
import com.github.theholywaffle.teamspeak3.api.exception.TS3CommandFailedException;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class ClientConnectionEventAdapter extends TS3EventAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClientConnectionEventAdapter.class);

    private final static int RECORD_CHANNEL_ID = 236;
    private final static int ONLINE_CHANNEL_ID = 235;

    private final TS3Api api;
    private boolean RECORD = true;

    public ClientConnectionEventAdapter(TS3Api api, boolean record) {
        this.api = api;
        this.RECORD = record;
        onClientJoin(null);
    }

    @Override
    public void onClientJoin(ClientJoinEvent e) {
        int users = changeChannelName();
        if (RECORD)
            recordChannel(users);
    }

    @Override
    public void onClientLeave(ClientLeaveEvent e) {
        int users = changeChannelName();
        if (RECORD)
            recordChannel(users);
    }

    private int changeChannelName(){
        LOGGER.info("changeChannelName called");
        List<Channel> channelsOnline = api.getChannels().stream().filter(channel -> channel.getId() == ONLINE_CHANNEL_ID).collect(Collectors.toList());
        int users = api.getServerInfo().getClientsOnline();
        if (channelsOnline.size() == 1){
            Channel channel = channelsOnline.get(0);
            String newCName = channel.getName().split(":")[0] + ": " + users;
            if(!newCName.equals(channel.getName()))
                try {
                    api.editChannel(channel.getId(), Collections.singletonMap(ChannelProperty.CHANNEL_NAME, newCName));
                } catch (TS3CommandFailedException e){
                    LOGGER.error(e.getMessage());
                }
        }
        return users;
    }

    private void recordChannel(int users){
        List<Channel> channelsOnline = api.getChannels().stream().filter(channel -> channel.getId() == RECORD_CHANNEL_ID).collect(Collectors.toList());
        if (channelsOnline.size() == 1){
            Channel channel = channelsOnline.get(0);
            String[] c = channel.getName().split(":");
            if(Integer.parseInt(c[1].replaceAll("[\\D]", "")) < users){
                String newCName = c[0] + ": " + users;
                Map<ChannelProperty, String> props = new HashMap<>();
                props.put(ChannelProperty.CHANNEL_NAME, newCName);
                props.put(ChannelProperty.CHANNEL_TOPIC, new Date().toString());
                api.editChannel(channel.getId(), props);
            }

        }
    }

}
