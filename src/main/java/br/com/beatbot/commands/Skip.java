package br.com.beatbot.commands;

import br.com.beatbot.BaseBot;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.managers.AudioManager;
import net.dv8tion.jda.player.MusicPlayer;

public class Skip extends Command{

	public void doCommand(BaseBot bot, Message message, String[] params) {
		String reply = null;
		
		if (message.isPrivate()) {
			return;
		}
		
		TextChannel channel = (TextChannel) message.getChannel();
		Guild guild = channel.getGuild();
		AudioManager am = guild.getAudioManager();
		
		channel.sendTyping();
		
		MusicPlayer musicPlayer;
		if (am.getSendingHandler() != null) {
			musicPlayer = (MusicPlayer) am.getSendingHandler();
			
			musicPlayer.skipToNext();
		} else {
			reply = message.getAuthor().getAsMention() + ", não estou tocando nada no momento!";
		}
		
		if (reply != null) {
			channel.sendMessage(reply);
		}
		return;
	}
	
	public String getName() {
		return "Skip";
	}
	
	public String getDescription() {
		return "Use este comando para pular a música atual(caso exista).";
	}
	
	public String getParams() {
		return "";
	}
	
	public String[] getAuths() {
		return null;
	}
	
	public boolean verifyParameters(String[] params) {
		return true;
	}
	
}
