package br.com.beatbot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

public class Configuration {
	
	public Yaml yaml = new Yaml();
	public Map<?, ?> data;
	
	public Configuration(String filepath) throws FileNotFoundException, UnsupportedEncodingException {
		InputStream input = new FileInputStream(new File(filepath));
		this.data = (Map<?, ?>) yaml.load(input);
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