package br.com.beatbot.commands;

import br.com.beatbot.BaseBot;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.managers.AudioManager;
import net.dv8tion.jda.player.MusicPlayer;

public class Volume extends Command{

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
			if (params != null && params.length > 0) {
				float volume = Float.parseFloat(params[0]);
				volume = Float.min(volume, 100);
				volume = Float.max(volume, 0);
				
				try {
					musicPlayer.setVolume(volume/100);
					reply = message.getAuthor().getAsMention() + ", volume definido para: " + volume;
				} catch (NumberFormatException e) {
					reply = message.getAuthor().getAsMention() + ", não foi possível definir o volume, defina um número correto.";
				}
			} else {
				reply = message.getAuthor().getAsMention() + ", o volume atual é: " + (musicPlayer.getVolume() * 100);
			}
		} else {
			reply = message.getAuthor().getAsMention() + ", não estou tocando nada no momento!";
		}
		
		channel.sendMessage(reply);
		return;
	}
	
	public String getName() {
		return "Volume";
	}
	
	public String getDescription() {
		return "Use esse comando para definir o volume das músicas.";
	}
	
	public String getParams() {
		return "[<volume>]";
	}
	
	public String[] getAuths() {
		return null;
	}
	
	public boolean verifyParameters(String[] params) {
		return true;
	}
	
}
