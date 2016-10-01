package br.com.beatbot.commands;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import br.com.beatbot.BaseBot;
import br.com.beatbot.GuildData;
import br.com.beatbot.Utils;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;

public class Configurate extends Command{

	public void doCommand(BaseBot bot, Message message, String[] params) {
		
		String reply;
		TextChannel channel = (TextChannel) message.getChannel();
		Guild guild = channel.getGuild();
		
		if (!message.getAuthor().getId().equalsIgnoreCase(bot.getConfigStringValue("ownerID"))) {
			return;
		}
		
		try {
			Utils.createGuildData(guild, params[0], params[1]);
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		GuildData gd = new GuildData(guild);
		if (gd.file == false) {
			reply = "Erro ao configurar o bot!";
		} else {
			reply = "Bot configurado, segue abaixo as informações:";
			reply += "\n**VoiceChannelID:** `" + gd.getString("voiceChannelID") + "`";
			reply += "\n**TextChannelID:** `" + gd.getString("musicTextChannelID") + "`";
		}
		
        message.getChannel().sendMessage(reply);
        
	}
	
	public String getName() {
		return "Configurate";
	}
	
	public String getDescription() {
		return "Esse comando configura o bot de música para sua Guilda.";
	}
	
	public String getParams() {
		return "<id do canal de voz> <id do canal de texto>";
	}
	
	public String[] getAliases() {
		return null;
	}
	
	public String[] getAuths() {
		return null;
	}
	
	public boolean verifyParameters(String[] params) {
		return (params.length >= 2);
	}
	
	public boolean onlyGuild() {
		return true;
	}
}
