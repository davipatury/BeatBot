package br.com.beatbot.listeners.jda;

import br.com.beatbot.BaseBot;
import br.com.beatbot.commands.Command;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter{
	
	public BaseBot bot;
	
	public MessageListener(BaseBot b) {
		bot = b;
	}
	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		Message message = event.getMessage();
		if (!message.getContent().startsWith(bot.getConfigStringValue("prefix")) || message.getContent().length() < 1 || message.getAuthor().isBot()) {
			return;
		}
		
		String[] words = message.getContent().substring(bot.getConfigStringValue("prefix").length()).split("\\s");
		String sCommand = words[0].toLowerCase();
		
		if (bot.getCommands().containsKey(sCommand)) {
			bot.checkCommand(words, message);
		} else {
			for (Command c : bot.getCommands().values()) {
				if(c.getAliases() == null) {
					return;
				}
				
				for (String aliase : c.getAliases()) {
					if (aliase.equalsIgnoreCase(sCommand)) {
						words[0] = c.getName();
						bot.checkCommand(words, message);
						return;
					}
				}
			}
		}
	}
}
