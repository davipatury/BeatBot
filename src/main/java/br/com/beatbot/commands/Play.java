package br.com.beatbot.commands;

import br.com.beatbot.BaseBot;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.managers.AudioManager;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.player.source.AudioInfo;
import net.dv8tion.jda.player.source.AudioSource;
import net.dv8tion.jda.player.source.RemoteSource;

public class Play extends Command{

	public void doCommand(BaseBot bot, Message message, String[] params) {
		JDA jda = bot.getJDA();
		String reply = null;
		
		if (message.isPrivate()) {
			return;
		}
			
		TextChannel channel = (TextChannel) message.getChannel();
		User author = message.getAuthor();
		Guild guild = channel.getGuild();
		AudioManager am = guild.getAudioManager();
		
		channel.sendTyping();
		
		if (!am.isConnected()) {
			if (bot.getConfigBooleanValue("autoSummon")) {
				if(jda.getVoiceChannelById(bot.getConfigStringValue("voiceChannelID")) != null) {
					am.openAudioConnection(jda.getVoiceChannelById(bot.getConfigStringValue("voiceChannelID")));
				} else {
					channel.sendMessage(author.getAsMention() + ", n�o consegui me conectar a um canal de voz!");
					return;
				}
			} else {
				channel.sendMessage(author.getAsMention() + ", n�o estou em um canal de voz!");
				return;
			}
		}
		
		MusicPlayer musicPlayer;
		if (am.getSendingHandler() == null) {
			musicPlayer = BaseBot.createPlayer(am);
			am.setSendingHandler(musicPlayer);
		} else {
			musicPlayer = (MusicPlayer) am.getSendingHandler();
		}
			
		AudioSource audioSource = new RemoteSource(params[0]);
		AudioInfo audioInfo = audioSource.getInfo();
			
		if (audioInfo.getError() != null) {
			String err = audioInfo.getError();
			if (err.length() > 1900) {
				System.err.println(err);
				reply = author.getAsMention() + ", um erro inesperado ocorreu!";
			} else {
				reply = author.getAsMention() + ", um erro ocorreu!```\n" + err + "```";
			}
		} else if (audioInfo.isLive()) {
			reply = author.getAsMention() + ", n�o � poss�vel tocar livestreams! Desculpe pela incoveni�ncia.";
		} else {
			if (audioInfo.getError() == null) {
				
				if(musicPlayer.getAudioQueue().size() == 0 && !musicPlayer.isPlaying()) {
					reply = String.format("**%s** foi adicionada � lista de reprodu��o!\n=> Pedido feito por %s", 
							audioInfo.getTitle(), 
							author.getAsMention().replace("`", "\\`"));
				} else {
					reply = String.format("**%s** foi adicionada � lista de reprodu��o!\n=> Pedido feito por %s - Posi��o: [%s]", 
							audioInfo.getTitle(), 
							author.getAsMention().replace("`", "\\`"), 
							String.valueOf(musicPlayer.getAudioQueue().size()));
				}
				
				musicPlayer.getAudioQueue().add(audioSource);

				if (!musicPlayer.isPlaying()) {
					musicPlayer.play();
					jda.getAccountManager().setGame(audioInfo.getTitle());
				} else {
					String err = audioInfo.getError();
					if (err != null) {
						if (err.length() > 1900) {
							reply = author.getAsMention() + ", um erro inesperado ocorreu!";
							System.err.println(err);
						} else {
							reply = "```" + err + "```";
						}
					}
				}
			}
		}
		
		message.updateMessage(reply);
		//channel.sendMessage(reply);
		return;
	}
	
	public String getName() {
		return "Play";
	}
	
	public String getDescription() {
		return "Use este comando para tocar uma m�sica no chat de voz atrav�s de um link do youtube.";
	}
	
	public String getParams() {
		return "<youtube link>";
	}
	
	public String[] getAuths() {
		return null;
	}
	
	public boolean verifyParameters(String[] params) {
		return (params.length == 1);
	}
	
}
