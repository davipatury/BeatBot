package br.com.beatbot.commands;

import br.com.beatbot.BaseBot;
import br.com.beatbot.Utils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.managers.AudioManager;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.player.source.AudioInfo;
import net.dv8tion.jda.player.source.AudioSource;

public class Queue extends Command{

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
			
			if (musicPlayer.getCurrentAudioSource() == null) {
				reply = message.getAuthor().getAsMention() + ", não estou tocando nada no momento!";
				Utils.deletableMessage(reply, channel);
				return;
			}
			
			channel.sendTyping();
			
			AudioInfo currentInfo = musicPlayer.getCurrentAudioSource().getInfo();
			
			String currentTime;
			String audioSourceTime;
			int minutes;
			int seconds;
			
			minutes = musicPlayer.getCurrentTimestamp().getMinutes();
			seconds = musicPlayer.getCurrentTimestamp().getSeconds();
			currentTime = Utils.formatMusicTime(minutes) + ":" + Utils.formatMusicTime(seconds);
			
			minutes = currentInfo.getDuration().getMinutes();
			seconds = currentInfo.getDuration().getSeconds();
			audioSourceTime = Utils.formatMusicTime(minutes) + ":" + Utils.formatMusicTime(seconds);
			
			reply = "Está tocando: **" + currentInfo.getTitle() 
					+ "** `[" + currentTime
					+ "/" + audioSourceTime +"]`";
			
			if(musicPlayer.getAudioQueue().size() > 0) {
				reply += "\nLista de espera:";
				int index = 0;
				for(AudioSource audioSource : musicPlayer.getAudioQueue()) {
					reply += "\n[" + String.valueOf(index) + "] - " + audioSource.getInfo().getTitle();
					index++;
				}
			}
		} else {
			reply = message.getAuthor().getAsMention() + ", não estou tocando nada no momento!";
		}
		
		Utils.deletableMessage(reply, channel);
		return;
	}
	
	public String getName() {
		return "Queue";
	}
	
	public String getDescription() {
		return "Use este comando para saber quais músicas estão na lista de reprodução(caso exista).";
	}
	
	public String getParams() {
		return "";
	}

	public String[] getAliases() {
		return new String[]{"lista", "playlist"};
	}
	
	public String[] getAuths() {
		return null;
	}
	
	public boolean verifyParameters(String[] params) {
		return true;
	}
	
}
