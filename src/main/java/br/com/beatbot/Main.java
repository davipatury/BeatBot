package br.com.beatbot;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.login.LoginException;

import br.com.beatbot.listeners.player.*;
import br.com.beatbot.listeners.jda.*;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.JDABuilder;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.player.MusicPlayer;

public class Main {
	
	public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
		Configuration config;
		JDA jda;
		
		try {
			config = new Configuration("config.yml");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			Utils.createConfig();
			config = new Configuration("config.yml");
		}
		
		try {
			jda = new JDABuilder().setBotToken(config.getString("token")).buildBlocking();
		} catch (LoginException | IllegalArgumentException | InterruptedException e) {
			e.printStackTrace();
			return;
		}
		
		BaseBot bot = new BaseBot(config, jda);
		
		bot.getJDA().addEventListener(new MessageListener(bot));
		
		Map<MusicPlayer, Guild> mP = new HashMap<MusicPlayer, Guild>();
		MusicPlayer mp;
		for (Guild g : bot.getJDA().getGuilds()) {
			mp = new MusicPlayer();
			mp.addEventListener(new PlayerStopListener(bot));
			mp.addEventListener(new PlayerNextListener(bot));
			mp.addEventListener(new PlayerPlayListener(bot));
			mp.addEventListener(new PlayerSkipListener(bot));
			mP.put(mp, g);
		}
		
		bot.setMusicPlayers(mP);
		
        bot.loadCommmands();
        
        Utils.print("Bot setup complete.", "Setup");
	}

}
