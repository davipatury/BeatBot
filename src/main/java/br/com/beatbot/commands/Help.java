package br.com.beatbot.commands;

import java.util.StringJoiner;
import java.util.TreeSet;

import br.com.beatbot.BaseBot;
import br.com.beatbot.Utils;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;

public class Help extends Command{

	public void doCommand(BaseBot bot, Message message, String[] params) {
		String reply = null;
		TextChannel channel = (TextChannel) message.getChannel();
		if (params != null && params.length > 0) {
			if(bot.getCommands().containsKey(params[0].toLowerCase())) {
				Command command = bot.getCommands().get(params[0].toLowerCase());
				reply = String.format("%s: \n", command.getName());
				reply += String.format("Uso: `%s%s %s`\n", bot.getConfigStringValue("prefix"), command.getName(), command.getParams());
				reply += String.format("Descrição: %s\n", command.getDescription());
				if (command.getAliases() != null) {
					reply += "Aliases: [";
					
					StringJoiner joiner = new StringJoiner(", ");
					for (String aliase : command.getAliases()) {
						joiner.add(aliase);
					}
					
					reply += joiner.toString().toString();
					reply += "]\n";
				}
			} else {
				boolean encontrou = false;
				for (Command command : bot.getCommands().values()) {
					for (String aliase : command.getAliases()) {
						if (aliase.equalsIgnoreCase(params[0])) {
							reply = String.format("%s: \n", command.getName());
							reply += String.format("Uso: `%s%s %s`\n", bot.getConfigStringValue("prefix"), command.getName(), command.getParams());
							reply += String.format("Descrição: %s\n", command.getDescription());
							if (command.getAliases() != null) {
								reply += "Aliases: [";
								
								StringJoiner joiner = new StringJoiner(", ");
								for (String al : command.getAliases()) {
									joiner.add(al);
								}
								
								reply += joiner.toString().toString();
								reply += "]\n";
							}
							encontrou = true;
							break;
						}
					}
				}
				if(!encontrou) {
					reply = String.format("%s, eu não pude achar este comando!", message.getAuthor().getAsMention());
				}
			}
		} else {
			reply = String.format("%s, reportando!\nEu sou um bot de músicas. Meu código foi muito mal feito, mas com algumas silver tapes, tudo vai ficar em seu devido lugar. De qualquer forma, aqui estão meus comandos:\n", message.getAuthor().getAsMention());
			reply += "---------------------\n";
			
			TreeSet<String> sortedList = new TreeSet<String>();
			
			for (String commandName : bot.getCommands().keySet()) {
				sortedList.add(commandName);
			}
			
			for (String commandName : sortedList) {
				Command command = bot.getCommands().get(commandName);
				reply += String.format("%s%s %s\n", bot.getConfigStringValue("prefix"), command.getName(), command.getParams());
			}
			
			reply += "---------------------\n";
			reply += String.format("Perceba que alguns desses comandos necessitam de certa autorização. Para mais informações sobre um comando, escreva `%sHelp <nome do comando>`!", bot.getConfigStringValue("prefix"));
			reply += "\n\nSaiba mais sobre os comandos em: https://github.com/davipatury/BeatBot/wiki/Commands";
		}

		Utils.deletableMessage(reply, channel, 30);
		return;
	}
	
	public String getName() {
		return "Help";
	}
	
	public String getDescription() {
		return "Mostra informações sobre os comandos deste bot.";
	}
	
	public String getParams() {
		return "[nome do comando]";
	}
	
	public String[] getAliases() {
		return new String[]{"ajuda"};
	}
	
	public String[] getAuths() {
		return null;
	}
	
	public boolean verifyParameters(String[] params) {
		return true;
	}
	
	public boolean onlyGuild() {
		return false;
	}
	
}
