package br.com.beatbot.listeners.player;

import br.com.beatbot.BaseBot;
import net.dv8tion.jda.player.hooks.PlayerListenerAdapter;
import net.dv8tion.jda.player.hooks.events.PlayEvent;

public class PlayerPlayListener extends PlayerListenerAdapter{
	
	public static BaseBot bot;
	
	public PlayerPlayListener(BaseBot baseBot) {
		bot = baseBot;
	}
	
	@Override
	public void onPlay(PlayEvent event) {
		bot.getJDA().getAccountManager().setGame(event.getPlayer().getCurrentAudioSource().getInfo().getTitle());
	}
}
