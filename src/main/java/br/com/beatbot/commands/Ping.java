package br.com.beatbot.commands;

import java.time.temporal.ChronoUnit;

import br.com.beatbot.BaseBot;
import net.dv8tion.jda.entities.Message;

public class Ping extends Command{

	public void doCommand(BaseBot bot, Message message, String[] params) {
        message.getChannel().sendMessageAsync("Ping: ...", msg -> {
            if(msg != null) {
                msg.updateMessageAsync("Ping: " + message.getTime().until(msg.getTime(), ChronoUnit.MILLIS) + "ms", null);
            }
        });
	}
	
	public String getName() {
		return "Ping";
	}
	
	public String getDescription() {
		return "Esse comando checa a diferença de tempo entre quando o Discord recebe um comando e quando o Discord recebe uma resposta.";
	}
	
	public String getParams() {
		return "";
	}
	
	public String[] getAliases() {
		return new String[]{"pong", "pang", "peng", "pung"};
	}
	
	public String[] getAuths() {
		return null;
	}
	
	public boolean verifyParameters(String[] params) {
		return true;
	}
	
	public boolean onlyGuild() {
		return false;
	}
}
