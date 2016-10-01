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

public class Info extends Command{

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
			
			if(!musicPlayer.isPlaying() || musicPlayer.getCurrentAudioSource() == null) {
				reply = message.getAuthor().getAsMention() + ", não estou tocando nada no momento!";
				Utils.deletableMessage(reply, channel, 30);
				return;
			}
			
			AudioSource audioSource = musicPlayer.getCurrentAudioSource();
			AudioInfo audioInfo = audioSource.getInfo();
			
			String currentTime;
			String audioSourceTime;
			int minutes;
			int seconds;
			
			minutes = musicPlayer.getCurrentTimestamp().getMinutes();
			seconds = musicPlayer.getCurrentTimestamp().getSeconds();
			currentTime = Utils.formatMusicTime(minutes) + ":" + Utils.formatMusicTime(seconds);
			
			minutes = audioInfo.getDuration().getMinutes();
			seconds = audioInfo.getDuration().getSeconds();
			audioSourceTime = Utils.formatMusicTime(minutes) + ":" + Utils.formatMusicTime(seconds);
			
			reply = "Está tocando: **" + musicPlayer.getCurrentAudioSource().getInfo().getTitle() 
					+ "** `[" + currentTime
					+ "/" + audioSourceTime +"]`";
			
			if (bot.getAuthors().containsKey(audioSource)) {
				reply += "\nPedido por: " + bot.getAuthors().get(audioSource).getUsername() + "#" + bot.getAuthors().get(audioSource).getDiscriminator();
			}
			
			reply += "\nExtraído através do: " + audioInfo.getExtractor();
			reply += "\nID: " + audioInfo.getId();
			
			if (audioInfo.getJsonInfo().has("like_count")) {
				reply += "\nLikes: " + audioInfo.getJsonInfo().get("like_count");
			}
				
			if (audioInfo.getThumbnail() != null) {
				reply += "\nThumbnail: " + audioInfo.getThumbnail();
			}
			
		} else {
			reply = message.getAuthor().getAsMention() + ", não estou tocando nada no momento!";
		}
		
		if (reply != null) {
			Utils.deletableMessage(reply, channel, 30);
		}
		return;
	}
	
	public String getName() {
		return "Info";
	}
	
	public String getDescription() {
		return "Use este comando para adquirir informações sobre a música que está tocando atualmente.";
	}
	
	public String getParams() {
		return "";
	}
	
	public String[] getAliases() {
		return new String[]{"musicinfo", "np"};
	}
	
	public String[] getAuths() {
		return null;
	}
	
	public boolean verifyParameters(String[] params) {
		return true;
	}
	
}
