package br.com.beatbot;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import br.com.beatbot.commands.*;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.player.source.AudioSource;

public class BaseBot{
	
	public static Configuration config;
	public static JDA jda;
	public static float volumeAtual = 0.25f;
	
	public static Map<String, Command> commands = new HashMap<String, Command>();
	public static Map<AudioSource, User> authors = new HashMap<AudioSource, User>();
	public static Map<MusicPlayer, Guild> musicPlayers = new HashMap<MusicPlayer, Guild>();
	
	public BaseBot(Configuration c, JDA j) {
		jda = j;
		config = c;
	}
	
	public void loadCommmands() {
		commands.put("help", new Help());
		commands.put("play", new Play());
		commands.put("queue", new Queue());
		commands.put("skip", new Skip());
		commands.put("volume", new Volume());
		commands.put("stop", new Stop());
		commands.put("info", new Info());
		commands.put("ping", new Ping());
		if (config.getBoolean("debug")) {
			commands.put("pingserver", new PingServer());
		}
		commands.put("configurate", new Configurate());
		for(String name : commands.keySet()) {
			Utils.print("Comando adicionado: " + name, "Setup");
		}
	}
	
	/* Get and Set */
	
	// Volume
	public float getVolume() {
		return volumeAtual;
	}
	
	public void setVolume(float volume) {
		volumeAtual = volume;
	}
	
	// MusicPlayer and Guilds
	public void setMusicPlayers(Map<MusicPlayer, Guild> set) {
		musicPlayers = set;
	}
	
	public Guild getGuildByPlayer(MusicPlayer musicPlayer) {
		return musicPlayers.get(musicPlayer);
	}
	
	public MusicPlayer getPlayerByGuild(Guild guild) {
		if (musicPlayers.containsValue(guild)) {
			for (Entry<MusicPlayer, Guild> e : musicPlayers.entrySet()) {
				if (e.getValue() == guild) {
					return e.getKey();
				}
			}
		}
		return null;
	}
	// Authors
	public Map<AudioSource, User> getAuthors() {
		return authors;
	}
	
	public void addAuthors(AudioSource as, User author) {
		authors.put(as, author);
	}
	
	public void clearAuthors() {
		authors.clear();
	}
	
	public void removeAuthor(AudioSource as) {
		authors.remove(as);
	}
	
	// Commands
	public Map<String, Command> getCommands() {
		return commands;
	}
	
	// Config
	public String getConfigStringValue(String key) {
		return config.getString(key);
	}
	
	public int getConfigIntegerValue(String key) {
		return config.getInt(key);
	}
	
	public boolean getConfigBooleanValue(String key) {
		return config.getBoolean(key);
	}

	public void setConfig(Configuration c) {
		config = c;
	}
	
	// JDA
	public JDA getJDA() {
		return jda;
	}
	
	public void setJDA(JDA j) {
		jda = j;
	}
	
	public void sendMusicMessage(String message, Guild g) {
		GuildData gd = new GuildData(g);
		TextChannel channel;
		
		if (gd.file) {
			channel = jda.getTextChannelById(gd.getString("musicTextChannelID"));
			if (channel == null) {
				channel = jda.getTextChannelById(config.getString("reportErrorsID"));
				message = "O bot na guilda de ID `" + g.getId() + "` não tem o TextChannel de música configurado corretamente. Textchannel configurado: `" + gd.getString("musicTextChannelID") + "`";
			}
		} else {
			channel = jda.getTextChannelById(config.getString("reportErrorsID"));
			message = "O bot na guilda de ID `" + g.getId() + "` não tem o TextChannel de música configurado.";
		}
		
		Utils.deletableMessage(message, channel, 30);
	}
	
	public void checkCommand(String[] words, Message message) {
		Command command = commands.get(words[0].toLowerCase());
		
		String[] params = new String[words.length - 1];
		
		if(words.length > 1) {
			int i = 0;
			for(String index : words) {
				if(index != words[0]) {
					params[i] = index;
					i++;
				}
			}
		}
		
		if(!command.verifyParameters(params)) {
			message.getChannel().sendMessage(String.format("%s, comando falhou ao executar: parâmetros inválidos ou insuficientes. Esse comando requer os seguintes parâmetros: `%s`", message.getAuthor().getAsMention(), command.getParams()));
			return;
		}
		
		if(command.onlyGuild() && message.isPrivate()) {
			message.getChannel().sendMessage(String.format("%s, comando falhou ao executar: esse comando não pode ser usado em chats privados.", message.getAuthor().getAsMention()));
			return;
		}
		
		command.doCommand(this, message, params);
	}
}
