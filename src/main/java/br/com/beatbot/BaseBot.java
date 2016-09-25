package br.com.beatbot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;
import javax.swing.Timer;

import br.com.beatbot.commands.*;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.hooks.ListenerAdapter;
import net.dv8tion.jda.managers.AudioManager;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.player.source.AudioSource;

public class BaseBot extends ListenerAdapter{
	
	public static Configuration config;
	public static JDA jda;
	public static float volumeAtual = 0.25f;
	
	public static Map<String, Command> commands = new HashMap<String, Command>();
	public static Map<AudioSource, User> authors = new HashMap<AudioSource, User>();
	
	public static void main(String[] args) throws LoginException, IllegalArgumentException, InterruptedException, FileNotFoundException, UnsupportedEncodingException
	{
		
		try {
			config = new Configuration("config.yml");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			createConfig();
			config = new Configuration("config.yml");
		}
		
		jda = new JDABuilder().setBotToken(config.getString("token")).buildBlocking();
		jda.addEventListener(new BaseBot());
		
        loadCommmands();
        
        print("Bot setup complete.", "Setup");
    }
	
	public static void createConfig() throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer;
		writer = new PrintWriter("config.yml", "UTF-8");
		
		writer.println("youtubeAPIkey: '123' # API Key do Youtube, deixe em branco caso não queira usar o sistema de busca. (!play <título do vídeo>)");
		
		writer.println("token: 'tokenHere' # Substitua `tokenHere` pelo token do seu bot.");
		
		writer.println("prefix: '!' # Substitua(ou não) `!` pelo prefixo dos comandos que você deseja.");
		
		writer.println("voiceChannelID: '123' #  Substitua `123` pelo ID do chat de voz que você deseja que o bot reproduza as músicas.");
		
		writer.println("musicTextChannelID: '123' # Substitua `123` pelo ID do chat de texto que você deseja que o bot envie as mensagens.");
		
		writer.println("autoSummon: true #  Coloque `true` para que antes do bot reproduzir uma música, ele automaticamente entre no chat de voz. "
				+ "Ou coloque `false` para que não seja possível reproduzir músicas até que o bot esteja em um chat de voz.");
		
		writer.close();
	}
	
	public static void loadCommmands() {
		commands.put("help", new Help());
		commands.put("play", new Play());
		commands.put("queue", new Queue());
		commands.put("skip", new Skip());
		commands.put("volume", new Volume());
		commands.put("stop", new Stop());
		commands.put("info", new Info());
		for(String name : commands.keySet()) {
			print("Comando adicionado: " + name, "Setup");
		}
	}
	
	// Static...
	public float getVolume() {
		return volumeAtual;
	}
	
	public void setVolume(float volume) {
		volumeAtual = volume;
	}
	
	public Map<AudioSource, User> getAuthors() {
		return authors;
	}
	
	public void addAuthors(AudioSource as, User author) {
		authors.put(as, author);
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
	//
	
	public static void print(String message, String tag) {
		if (tag == "Debug" && config.getString("debug") == null) {
			return;
		}
		SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
	    String strDate = sdfDate.format(now);
	    System.out.println("[" + strDate + "] [" + tag + "] [BeatBot]: " + message);
	}
	
	public static String formatMusicTime(int time) {
		String string;
		if(time < 10) {
			string = "0" + time;
		} else {
			string = String.valueOf(time);
		}
		return string;
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
				jda.getAccountManager().setGame("Paciência Spider");
				authors.clear();
			}

			@Override
			public void playNext(boolean b) {
				super.playNext(b);
				super.setVolume(volumeAtual);
				
				authors.remove(super.getPreviousAudioSource());

				AudioSource src = super.getCurrentAudioSource();
				if (src == null) {
					am.closeAudioConnection();
					jda.getAccountManager().setGame("Paciência Spider");
					authors.clear();
				} else {
					sendMusicMessage("Tocando agora: **" + src.getInfo().getTitle() + "**");
					jda.getAccountManager().setGame(src.getInfo().getTitle());
				}
			}

			@Override
            public void play() {
				super.play();
				super.setVolume(volumeAtual);
				jda.getAccountManager().setGame(super.getCurrentAudioSource().getInfo().getTitle());
			}
		};
		myPlayer.setVolume(volumeAtual);
		return myPlayer;
    }
	
	public static void sendMusicMessage(String message) {
		TextChannel channel = jda.getTextChannelById(config.getString("musicTextChannelID"));
		Message ms = channel.sendMessage(message);
		Timer timer = new Timer(15000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ms.deleteMessage();
			}
		});
		timer.setRepeats(false);
		timer.start();
	}
	
	public void deletableMessage(String message, TextChannel channel) {
		Message ms = channel.sendMessage(message);
		Timer timer = new Timer(15000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ms.deleteMessage();
			}
		});
		timer.setRepeats(false);
		timer.start();
	}
	
	public void deletableMessage(String message, TextChannel channel, int time) {
		Message ms = channel.sendMessage(message);
		Timer timer = new Timer(time * 1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ms.deleteMessage();
			}
		});
		timer.setRepeats(false);
		timer.start();
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
		
		command.doCommand(this, message, params);
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event)
	{
		Message message = event.getMessage();
		if (!message.getContent().startsWith(config.getString("prefix")) || message.getContent().length() < 1) {
			return;
		}
		
		String[] words = message.getContent().substring(config.getString("prefix").length()).split("\\s");
		String sCommand = words[0].toLowerCase();
		
		if (commands.containsKey(sCommand)) {
			checkCommand(words, message);
		} else {
			for (Command c : commands.values()) {
				for (String aliase : c.getAliases()) {
					if (aliase.equalsIgnoreCase(sCommand)) {
						BaseBot.print(sCommand + " = " + aliase.toLowerCase(), "Debug");
						words[0] = c.getName();
						checkCommand(words, message);
						return;
					}
				}
			}
		}
	}
}
