package tip.tasks;

import cn.nukkit.Player;
import tip.messages.BaseMessage;
import tip.messages.defaults.BroadcastMessage;
import tip.utils.Api;

import java.util.LinkedHashMap;

class BroadCastPlayerTask {

    private Player player;
    private LinkedHashMap<String,MessageType> type = new LinkedHashMap<>();


    BroadCastPlayerTask(Player owner) {
        this.player = owner;
    }

    void onRun() {
        if(player == null || !player.isOnline()){
            return;
        }
        BroadcastMessage message;
        message = (BroadcastMessage) Api.getSendPlayerMessage(player.getName(),player.level.getFolderName(),
                BaseMessage.BaseTypes.BROADCAST);
        if (message != null) {
            if (message.isOpen()) {
                if(!type.containsKey(player.getLevel().getFolderName())){
                    type.put(player.getLevel().getFolderName(),new MessageType());
                }
                MessageType m = type.get(player.getLevel().getFolderName());
                if (m.time == -1) {
                    m.time = message.getTime();
                    m.key = true;
                } else if (m.time <= 0) {
                    m.i++;
                    m.time = message.getTime();
                    m.key = true;
                }
                if (m.i >= message.getMessages().size()) {
                    m.i = 0;
                }
                if (m.key) {
                    m.key = false;
                    String text = message.getMessages().get(m.i);
                    Api api = new Api(text, player);
                    text = api.strReplace();
                    player.sendMessage(text);

                }
                if (m.time > 0) {
                    m.time--;
                }
            }
        }
    }

    private class MessageType{
        public int i = 0;

        public int time = -1;

        boolean key = false;

    }
}
