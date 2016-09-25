package br.com.beatbot.commands;

import br.com.beatbot.BaseBot;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.managers.AudioManager;
import net.dv8tion.jda.player.MusicPlayer;
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
				channel.sendMessage(reply);
				return;
			}
			
			channel.sendTyping();
			
			String currentTime;
			String audioSourceTime;
			
			if(musicPlayer.getCurrentTimestamp().getMinutes() < 10) {
				currentTime = "0" + musicPlayer.getCurrentTimestamp().getMinutes();
			} else {
				currentTime = String.valueOf(musicPlayer.getCurrentTimestamp().getMinutes());
			}
			if(musicPlayer.getCurrentTimestamp().getSeconds() < 10) {
				currentTime += ":0" + musicPlayer.getCurrentTimestamp().getSeconds();
			} else {
				currentTime += ":" + musicPlayer.getCurrentTimestamp().getSeconds();
			}
			
			if(musicPlayer.getCurrentAudioSource().getInfo().getDuration().getMinutes() < 10) {
				audioSourceTime = "0" + musicPlayer.getCurrentAudioSource().getInfo().getDuration().getMinutes();
			} else {
				audioSourceTime = String.valueOf(musicPlayer.getCurrentAudioSource().getInfo().getDuration().getMinutes());
			}
			if(musicPlayer.getCurrentAudioSource().getInfo().getDuration().getSeconds() < 10) {
				audioSourceTime += ":0" + musicPlayer.getCurrentAudioSource().getInfo().getDuration().getSeconds();
			} else {
				audioSourceTime += ":" + String.valueOf(musicPlayer.getCurrentAudioSource().getInfo().getDuration().getSeconds());
			}
			
			reply = "Está tocando: **" + musicPlayer.getCurrentAudioSource().getInfo().getTitle() 
					+ "** [" + currentTime
					+ "/" + audioSourceTime +"]";
			
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
		
		channel.sendMessage(reply);
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
	
	public String[] getAuths() {
		return null;
	}
	
	public boolean verifyParameters(String[] params) {
		return true;
	}
	
}
