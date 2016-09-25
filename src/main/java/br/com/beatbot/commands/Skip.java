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
				reply = message.getAuthor().getAsMention() + ", n�o estou tocando nada no momento!";
			}
			
			if (params != null && params.length > 0) {
				try {
					if (musicPlayer.getAudioQueue().size() > Integer.parseInt(params[0])) {
						musicPlayer.getAudioQueue().remove(musicPlayer.getAudioQueue().get(Integer.parseInt(params[0])));
						reply = message.getAuthor().getAsMention() + ", m�sica na posi��o " + params[0] + " removida!";
					} else {
						reply = message.getAuthor().getAsMention() + ", n�o encontrei nenhuma m�sica na posi��o " + params[0] + ".";
					}
				} catch (NumberFormatException e) {
					reply = message.getAuthor().getAsMention() + ", n�o foi poss�vel remover uma m�sica da lista de reprodu��o, digite um n�mero correto.";
				}
			} else {
				musicPlayer.skipToNext();
			}
		} else {
			reply = message.getAuthor().getAsMention() + ", n�o estou tocando nada no momento!";
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
		return "Use este comando para pular a m�sica atual(caso exista).";
	}
	
	public String getParams() {
		return "[n�mero da m�sica na fila]";
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
