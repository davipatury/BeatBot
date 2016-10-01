package br.com.beatbot;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Timer;

import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;

public class Utils {
	
	public static void deletableMessage(String message, TextChannel channel) {
		deletableMessage(message, channel, 15);
	}
	
	public static void deletableMessage(String message, TextChannel channel, int time) {
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
	
	public static void print(String message, String tag) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
	    String strDate = sdfDate.format(now);
	    System.out.println("[" + strDate + "] [" + tag + "] [BeatBot]: " + message);
	}
	
	public static void debugPrint(String message) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("HH:mm:ss");
		Date now = new Date();
	    String strDate = sdfDate.format(now);
	    System.out.println("[" + strDate + "] [Debug] [BeatBot]: " + message);
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
	
	public static void createGuildData(Guild guild, String voiceID, String textID) throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer;
		writer = new PrintWriter("data/" + guild.getId() + ".yml", "UTF-8");
		writer.println("voiceChannelID: '" + voiceID + "'");
		writer.println("musicTextChannelID: '" + textID + "'");
		writer.close();
	}
	
}
