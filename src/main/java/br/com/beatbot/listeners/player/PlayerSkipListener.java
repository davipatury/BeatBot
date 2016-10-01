package br.com.beatbot.listeners.player;

import br.com.beatbot.BaseBot;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.player.hooks.PlayerListenerAdapter;
import net.dv8tion.jda.player.hooks.events.SkipEvent;
import net.dv8tion.jda.player.source.AudioSource;

public class PlayerSkipListener extends PlayerListenerAdapter{
	
	public static BaseBot bot;
	
	public PlayerSkipListener(BaseBot baseBot) {
		bot = baseBot;
	}
	
	@Override
	public void onSkip(SkipEvent event) {
		bot.removeAuthor(event.getPlayer().getPreviousAudioSource());

		AudioSource src = event.getPlayer().getCurrentAudioSource();
		if (src == null) {
			if (bot.getConfigBooleanValue("autoSummon")) {
				bot.getJDA().getAudioManager(bot.getGuildByPlayer((MusicPlayer) event.getPlayer())).closeAudioConnection();
			}
			bot.getJDA().getAccountManager().setGame("Paciência Spider");
			bot.clearAuthors();
		} else {
			bot.sendMusicMessage("Tocando agora: **" + src.getInfo().getTitle() + "**", bot.getGuildByPlayer((MusicPlayer) event.getPlayer()));
			bot.getJDA().getAccountManager().setGame(src.getInfo().getTitle());
		}
	}
}
