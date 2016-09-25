package br.com.beatbot.commands;

import br.com.beatbot.BaseBot;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.managers.AudioManager;
import net.dv8tion.jda.player.MusicPlayer;

public class Stop extends Command{

	public void doCommand(BaseBot bot, Message message, String[] params) {
		String reply = null;
		
		if (message.isPrivate()) {
			return;
		}
		
		TextChannel channel = (TextChannel) message.getChannel();
		Guild guild = channel.getGuild();
		AudioManager am = guild.getAudioManager();
		
		MusicPlayer musicPlayer;
		if (am.getSendingHandler() != null) {
			musicPlayer = (MusicPlayer) am.getSendingHandler();
			if(!musicPlayer.isPlaying()) {
				reply = message.getAuthor().getAsMention() + ", não estou tocando nada no momento!";
			}
			
			musicPlayer.stop();
		} else {
			reply = message.getAuthor().getAsMention() + ", não estou tocando nada no momento!";
		}
		
		if (reply != null) {
			bot.deletableMessage(reply, channel);
		}
		return;
	}
	
	public String getName() {
		return "Stop";
	}
	
	public String getDescription() {
		return "Use este comando para parar e apagar a playlist que está tocando.";
	}
	
	public String getParams() {
		return "";
	}
	
	public String[] getAliases() {
		return new String[]{"parar"};
	}
	
	public String[] getAuths() {
		return null;
	}
	
	public boolean verifyParameters(String[] params) {
		return true;
	}
	
}
