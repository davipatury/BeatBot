package br.com.beatbot.commands;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import br.com.beatbot.BaseBot;
import net.dv8tion.jda.entities.Message;

public class PingServer extends Command{

	public void doCommand(BaseBot bot, Message message, String[] params) {
		
		if (!message.getAuthor().getId().equalsIgnoreCase(bot.getConfigStringValue("ownerID"))) {
			return;
		}
		
		String question;
		String ip;
		int port;
		if (params.length != 3) {
			question = params[0];
			ip = "51.254.36.189";
			port = 9530;
		} else {
			question = params[2];
			ip = params[0];
			try {
				port = Integer.parseInt(params[1]);
			} catch (NumberFormatException e) {
				message.getChannel().sendMessage("Porta inválida!");
				return;
			}
		}
		
		String reply = null;
		String response = null;
		
		try {
			String s = null;
			Process p = Runtime.getRuntime().exec("python ping_server.py --host=" + ip + " --port=" + port + " " + question);
			
			BufferedReader stdInput = new BufferedReader(new 
	                 InputStreamReader(p.getInputStream()));

			BufferedReader stdError = new BufferedReader(new 
	                 InputStreamReader(p.getErrorStream()));

			while ((s = stdInput.readLine()) != null) {
				response = s;
			}
	            
			while ((s = stdError.readLine()) != null) {
				System.out.println(s);
			}
			
			reply = "Resposta: ```\n" + response + "```";
			
		} catch (IOException e) {
			e.printStackTrace();
		}
        
		if (reply.length() > 2000) {
			try {
				PrintWriter writer;
				writer = new PrintWriter("ping.txt", "UTF-8");
				writer.write(reply);
				writer.close();
			} catch (FileNotFoundException | UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			message.getChannel().sendMessage(reply);
		}
	}
	
	public String getName() {
		return "PingServer";
	}
	
	public String getDescription() {
		return "Esse comando retorna o número de players online no servidor.";
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
		return (params.length >= 1);
	}
	
	public boolean onlyGuild() {
		return false;
	}
}
