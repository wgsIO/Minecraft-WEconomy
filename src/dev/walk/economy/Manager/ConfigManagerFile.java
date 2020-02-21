package dev.walk.economy.Manager;

import java.io.*;

import dev.walk.economy.Economy;
import org.apache.commons.io.IOUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class ConfigManagerFile {

	private File folder;
	private String file;

	private File archive;
	private FileConfiguration config;

	public ConfigManagerFile(Plugin plugin, String file){
		this.folder = plugin.getDataFolder();
		this.file = file;
		archive = new File(folder, file);
	}
	public ConfigManagerFile(File folder, String file){
		this.folder = folder;
		this.file = file;
		archive = new File(folder, file);
	}
	public void save(){
		try{ this.config.save(archive); }catch (Exception localException1) {}
		this.config = YamlConfiguration.loadConfiguration(archive);
	}
	public void save(String file){
		Economy economy = Economy.getEconomy();
		try {
			config.options().copyDefaults(true);
			config.load(economy.getResource(file + ".yml"));
			save();
		} catch (Exception e) {System.out.println("Não consegui salvar a template");}
	}
	public void prepare(){
		if (folder.exists()){
			folder.mkdirs();
		}
		if (!archive.exists()) {
			try{
				archive.createNewFile();
			}catch (Exception e) {}
		}
		this.config = YamlConfiguration.loadConfiguration(archive);
		try{ this.config.save(archive); }catch (Exception e) {}
		this.config = YamlConfiguration.loadConfiguration(archive);
	}
	public void reload(){
		this.config = YamlConfiguration.loadConfiguration(archive);
	}
	public boolean exists(){ return archive.exists(); }
	public FileConfiguration getConfig(){
		return config;
	}
	public File getFile(){
		return archive;
	}
	public String getFileName(){ return file; }
	public File getDiretory(){
		return folder;
	}
	
}
