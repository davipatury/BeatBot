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
		
		MusicPlayer musicPlayer;
		if (am.getSendingHandler() != null) {
			musicPlayer = (MusicPlayer) am.getSendingHandler();
			if(!musicPlayer.isPlaying()) {
				reply = message.getAuthor().getAsMention() + ", não estou tocando nada no momento!";
			}
			
			if (params != null && params.length > 0) {
				try {
					if (musicPlayer.getAudioQueue().size() > Integer.parseInt(params[0])) {
						musicPlayer.getAudioQueue().remove(musicPlayer.getAudioQueue().get(Integer.parseInt(params[0])));
						reply = message.getAuthor().getAsMention() + ", música na posição " + params[0] + " removida!";
					} else {
						reply = message.getAuthor().getAsMention() + ", não encontrei nenhuma música na posição " + params[0] + ".";
					}
				} catch (NumberFormatException e) {
					reply = message.getAuthor().getAsMention() + ", não foi possível remover uma música da lista de reprodução, digite um número correto.";
				}
			} else {
				musicPlayer.skipToNext();
			}
		} else {
			reply = message.getAuthor().getAsMention() + ", não estou tocando nada no momento!";
		}
		
		if (reply != null) {
			bot.deletableMessage(reply, channel);
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
		return "[número da música na fila]";
	}
	
	public String[] getAliases() {
		return new String[]{"passar", "next"};
	}
	
	public String[] getAuths() {
		return null;
	}
	
	public boolean verifyParameters(String[] params) {
		return true;
	}
	
}
