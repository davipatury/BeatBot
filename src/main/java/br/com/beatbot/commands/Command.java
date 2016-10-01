package br.com.beatbot.commands;

import br.com.beatbot.BaseBot;
import net.dv8tion.jda.entities.Message;

public class Command {
	
	/**
	 * Executa uma a��o ligada ao comando.
	 *
	 * @param  bot  objeto da classe BaseBot utilizada para acessar suas fun��es
	 * @param  message mensagem utilizada para adquirir informa��es relevantes
	 * @param  params array de strings depois do comando, separada por espa�os
	 */
	public void doCommand(BaseBot bot, Message message, String[] params) {
		return;
	}
	
	public String getName() {
		return "";
	}
	
	public String getDescription() {
		return "";
	}
	
	public String getParams() {
		return "";
	}
	
	public String[] getAliases() {
		return null;
	}
	
	public String[] getAuths() {
		return null;
	}
	
	public boolean verifyParameters(String[] params) {
		if (getParams() == "") {
			return true;
		}
		
		int params_req = getParams().split("> <").length;

		return (params.length == params_req);
	}
	
	public boolean onlyGuild() {
		return true;
	}
}
