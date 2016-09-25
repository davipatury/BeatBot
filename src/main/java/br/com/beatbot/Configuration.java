package br.com.beatbot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Configuration {
	
	public Yaml yaml = new Yaml();
	public Map<?, ?> data;
	
	public Configuration(String filepath) {
		try {
			InputStream input = new FileInputStream(new File(filepath));
			this.data = (Map<?, ?>) yaml.load(input);
		} catch (FileNotFoundException e) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("token", "tokenHere");
			map.put("prefix", "!");
			map.put("voiceChannelID", "123");
			map.put("musicTextChannelID", "123");
			map.put("autoSummon", "true");
			map.put("youtubeAPIkey", "123");
			
			PrintWriter writer;
			try {
				writer = new PrintWriter("config.yml", "UTF-8");
				Yaml yaml = new Yaml();
				yaml.dump(map, writer);
			} catch (FileNotFoundException | UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public String getString(String key) {
		return (String) data.get(key);
	}
	
	public int getInt(String key) {
		return (int) data.get(key);
	}
	
	public boolean getBoolean(String key) {
		return (boolean) data.get(key);
	}

}
