package br.com.beatbot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import br.com.beatbot.commands.*;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.dv8tion.jda.managers.AudioManager;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.player.source.AudioSource;

public class BaseBot extends ListenerAdapter{
	
	public static Configuration config = new Configuration("config.yml");
	public static JDA jda;
	private static final float DEFAULT_VOLUME = 0.25f;
	
	public static Map<String, Command> commands = new HashMap<String, Command>();
	
	public static void main(String[] args) throws LoginException, IllegalArgumentException, InterruptedException
	{
		jda = new JDABuilder().setBotToken(config.getString("token")).buildBlocking();
		jda.addEventListener(new BaseBot());
        loadCommmands();
        print("Bot setup complete.", "Setup");
    }
	
	public static void loadCommmands() {
		commands.put("help", new Help());
		commands.put("play", new Play());
		commands.put("queue", new Queue());
		commands.put("skip", new Skip());
		commands.put("volume", new Volume());
		for(String name : commands.keySet()) {
			print("Comando adicionado: " + name, "Setup");
		}
	}
	
	public Map<String, Command> getCommands() {
		return commands;
	}
	
	public String getConfigStringValue(String key) {
		return config.getString(key);
	}
	
	public int getConfigIntegerValue(String key) {
		return config.getInt(key);
	}
	
	public boolean getConfigBooleanValue(String key) {
		return config.getBoolean(key);
	}
	
	public JDA getJDA() {
		return jda;
	}
	
	public static void print(String message, String tag) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
	    String strDate = sdfDate.format(now);
	    System.out.println("[" + strDate + "] [" + tag + "] [BeatBot]: " + message);
	}
	
	public static boolean checkLink(String string) {
		if(string.startsWith("youtube.com")) {
			return false;
		}
		if(string.startsWith("http://")) {
			return false;
		}
		if(string.startsWith("https://")) {
			return false;
		}
		if(string.startsWith("www.")) {
			return false;
		}
		if(string.startsWith("soundcloud.com")) {
			return false;
		}
		return true;
	}
	
	public static MusicPlayer createPlayer(AudioManager am) {
		MusicPlayer myPlayer = new MusicPlayer() {
			@Override
			public void stop() {
				super.stop();
				am.closeAudioConnection();
			}

			@Override
			public void playNext(boolean b) {
				super.playNext(b);
				super.setVolume(DEFAULT_VOLUME);

				AudioSource src = super.getCurrentAudioSource();
				if (src == null) {
					am.closeAudioConnection();
				} else {
					sendMusicMessage("Tocando agora: **" + src.getInfo().getTitle() + "**");
				}
			}

			@Override
            public void play() {
				super.play();
				super.setVolume(DEFAULT_VOLUME);
			}
		};
		myPlayer.setVolume(DEFAULT_VOLUME);
		return myPlayer;
    }
	
	public static void sendMusicMessage(String message) {
		TextChannel channel = jda.getTextChannelById(config.getString("musicTextChannelID"));
		channel.sendMessage(message);
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		Message message = event.getMessage();
		if (!message.getContent().startsWith(config.getString("prefix")) || message.getContent().length() < 1) {
			return;
		}
		
		String[] words = message.getContent().substring(config.getString("prefix").length()).split("\\s");
		
		if (commands.containsKey(words[0].toLowerCase())) {
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
			
			command.doCommand(this, message, params);
		}
	}
}
