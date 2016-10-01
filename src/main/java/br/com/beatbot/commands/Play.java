package br.com.beatbot.commands;

import java.io.IOException;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;

import br.com.beatbot.BaseBot;
import br.com.beatbot.GuildData;
import br.com.beatbot.Utils;
import net.dv8tion.jda.JDA;
import net.dv8tion.jda.entities.Guild;
import net.dv8tion.jda.entities.Message;
import net.dv8tion.jda.entities.TextChannel;
import net.dv8tion.jda.entities.User;
import net.dv8tion.jda.exceptions.PermissionException;
import net.dv8tion.jda.managers.AudioManager;
import net.dv8tion.jda.player.MusicPlayer;
import net.dv8tion.jda.player.source.AudioInfo;
import net.dv8tion.jda.player.source.AudioSource;
import net.dv8tion.jda.player.source.RemoteSource;

public class Play extends Command{

	public void doCommand(BaseBot bot, Message message, String[] params) {
		JDA jda = bot.getJDA();
		String reply = null;
		
		if (message.isPrivate()) {
			return;
		}
			
		TextChannel channel = (TextChannel) message.getChannel();
		User author = message.getAuthor();
		Guild guild = channel.getGuild();
		AudioManager am = guild.getAudioManager();
		
		channel.sendTyping();
		
		if (bot.getConfigStringValue("youtubeAPIkey") != null || bot.getConfigStringValue("youtubeAPIkey") != "") {
			if (params.length >= 1 && Utils.checkLink(params[0])) {
				String completeName = "";
				if (params.length > 1) {
					for(String s : params) {
						completeName += s + " ";
					}
				} else {
					completeName = params[0];
				}
				
				YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
			          public void initialize(HttpRequest request) throws IOException {}})
			        .setApplicationName("youtube-cmdline-search-sample")
			        .build();

			    try {
			    	YouTube.Search.List search = youtube.search().list("id,snippet");
					
			    	search.setKey(bot.getConfigStringValue("youtubeAPIkey"));
					search.setQ(completeName);
					search.setType("video");
					search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
					
					SearchListResponse searchResponse = search.execute();
					
					if (searchResponse.getItems().isEmpty()) {
						reply = author.getAsMention() + ", não encontrei nenhum vídeo!";
						Utils.deletableMessage(reply, channel);
						return;
					}
					
					if (bot.getConfigBooleanValue("debug")) {
						Utils.debugPrint("Pedido de musica feito atraves da API do YouTube. VideoID: " + searchResponse.getItems().get(0).getId().getVideoId());
					}
					
					params[0] = searchResponse.getItems().get(0).getId().getVideoId();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					message.deleteMessage();
				} catch(PermissionException e) {
					Utils.print("Erro ao deletar mensagem!", "PermissionException");
				}
			}
		}
		
		if (!am.isConnected()) {
			if (bot.getConfigBooleanValue("autoSummon")) {
				GuildData gd = new GuildData(guild);
				
				if (gd.file) {
					if(jda.getVoiceChannelById(gd.getString("voiceChannelID")) != null) {
						am.openAudioConnection(jda.getVoiceChannelById(gd.getString("voiceChannelID")));
					} else {
						reply = author.getAsMention() + ", não consegui me conectar a um canal de voz!";
						Utils.deletableMessage(reply, channel);
						return;
					}
				} else {
					reply = "O bot precisa estar configurado para isto!";
					channel.sendMessage(reply);
					return;
				}
			} else {
				reply = author.getAsMention() + ", não estou em um canal de voz!";
				Utils.deletableMessage(reply, channel);
				return;
			}
		}
		
		MusicPlayer musicPlayer;
		if (am.getSendingHandler() == null) {
			musicPlayer = bot.getPlayerByGuild(guild);
			am.setSendingHandler(musicPlayer);
		} else {
			musicPlayer = (MusicPlayer) am.getSendingHandler();
		}
			
		AudioSource audioSource = new RemoteSource(params[0]);
		AudioInfo audioInfo = audioSource.getInfo();
			
		if (audioInfo.getError() != null) {
			String err = audioInfo.getError();
			if (err.length() > 1900) {
				System.err.println(err);
				reply = author.getAsMention() + ", um erro inesperado ocorreu!";
			} else {
				reply = author.getAsMention() + ", um erro ocorreu!```\n" + err + "```";
			}
		} else if (audioInfo.isLive()) {
			reply = author.getAsMention() + ", não é possível tocar livestreams! Desculpe pela incoveniência.";
		} else {
			
			if(musicPlayer.getAudioQueue().size() == 0 && !musicPlayer.isPlaying()) {
				reply = String.format("**%s** foi adicionada à lista de reprodução!\n=> Pedido feito por %s", 
						audioInfo.getTitle(), 
						author.getAsMention().replace("`", "\\`"));
			} else {
				reply = String.format("**%s** foi adicionada à lista de reprodução!\n=> Pedido feito por %s - Posição: [%s]", 
						audioInfo.getTitle(), 
						author.getAsMention().replace("`", "\\`"), 
						String.valueOf(musicPlayer.getAudioQueue().size()));
			}
			
			musicPlayer.getAudioQueue().add(audioSource);
			bot.addAuthors(audioSource, author);

			if (!musicPlayer.isPlaying()) {
				musicPlayer.play();
			}
		}

		Utils.deletableMessage(reply, channel);
		return;
	}
	
	public String getName() {
		return "Play";
	}
	
	public String getDescription() {
		return "Use este comando para tocar uma música no chat de voz através de um link do youtube.";
	}
	
	public String getParams() {
		return "<link ou título do vídeo>";
	}
	
	public String[] getAliases() {
		return new String[]{"tocar"};
	}
	
	public String[] getAuths() {
		return null;
	}
	
	public boolean verifyParameters(String[] params) {
		return (params.length >= 1);
	}
	
}
