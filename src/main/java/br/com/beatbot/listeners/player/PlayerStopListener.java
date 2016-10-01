package br.com.beatbot.listeners.player;

import br.com.beatbot.BaseBot;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.player.hooks.PlayerListenerAdapter;
import net.dv8tion.jda.player.hooks.events.StopEvent;

public class PlayerStopListener extends PlayerListenerAdapter{
	
	public static BaseBot bot;
	
	public PlayerStopListener(BaseBot baseBot) {
		bot = baseBot;
	}
	
	@Override
	public void onStop(StopEvent event) {
		if (bot.getConfigBooleanValue("autoSummon")) {
			bot.getJDA().getAudioManager(bot.getGuildByPlayer((MusicPlayer) event.getPlayer())).closeAudioConnection();
		}
		bot.getJDA().getAccountManager().setGame("Paciência Spider");
		bot.clearAuthors();
	}
}
