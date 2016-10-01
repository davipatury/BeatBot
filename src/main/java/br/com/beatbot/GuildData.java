package br.com.beatbot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import net.dv8tion.jda.entities.Guild;

public class GuildData {
	
	public Yaml yaml = new Yaml();
	public Map<?, ?> data;
	public Guild guild;
	
	public boolean file;
	
	public GuildData(Guild g) {
		guild = g;
		
		try {
			InputStream input = new FileInputStream(new File("data/" + guild.getId() + ".yml"));
			this.data = (Map<?, ?>) yaml.load(input);
			file = true;
		} catch (FileNotFoundException e) {
			file = false;
		}
	}
	
	//Get
	public String getString(String key) {
		return (String) this.data.get(key);
	}
	
	public int getInt(String key) {
		return (int) this.data.get(key);
	}
	
	public boolean getBoolean(String key) {
		return (boolean) this.data.get(key);
	}
	
	public Map<?, ?> getList(String key) {
		return (Map<?, ?>) this.data.get(key);
	}

}
