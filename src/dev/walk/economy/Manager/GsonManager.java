
package dev.walk.economy.Manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import java.io.File;
import java.io.Reader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.walk.economy.Util.EconomyUtils;
import net.minecraft.server.v1_8_R3.Path;
import org.bukkit.plugin.Plugin;

public class GsonManager {

    private String name;
    private File location;
    private File file;
    private EconomyUtils utils = new EconomyUtils();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private Map<String, Object> data = new HashMap<>();
    boolean prepared = false;

    public GsonManager(File location, String name){
        this.name = name.replace(".json", "")+".json";
        this.location = location;
        file = new File(this.location, this.name);
    }
    public GsonManager(Plugin plugin, String name){
        this.name = name.replace(".json", "")+".json";
        this.location = plugin.getDataFolder();
        file = new File(this.location, this.name);
    }
    public GsonManager(String location, String name){
        this.name = name.replace(".json", "")+".json";
        this.location = new File(location);
        file = new File(this.location, this.name);
    }
    public GsonManager(File location, File name){
        this.name = name.getName().replace(".json", "")+".json";
        this.location = location;
        file = new File(this.location, this.name);
    }
    public GsonManager(Plugin plugin, File name){
        this.name = name.getName().replace(".json", "")+".json";
        this.location = plugin.getDataFolder();
        file = new File(this.location, this.name);
    }
    public GsonManager(String location, File name){
        this.name = name.getName().replace(".json", "")+".json";
        this.location = new File(location);
        file = new File(this.location, this.name);
    }
    public GsonManager(String location, String folder, String name){
        this.name = name.replace(".json", "")+".json";
        this.location = new File(location + "/" + folder, name);
        file = new File(this.location, this.name);
    }
    public GsonManager(Plugin plugin, String folder, String name){
        this.name = name.replace(".json", "")+".json";
        this.location = new File(plugin.getDataFolder() + "/" + folder, name);
        file = new File(this.location, this.name);
    }
    public GsonManager(File location, String folder, String name){
        this.name = name.replace(".json", "")+".json";
        this.location = new File(location + "/" + folder, name);
        file = new File(this.location, this.name);
    }

    public boolean hasPrepared(){ return prepared; }
    public GsonManager prepareGson() {
        try {
            if (!this.location.exists()) {
                this.location.mkdirs();
            }
            if (!this.file.exists()) {
                this.file.createNewFile();
            }
            prepared = true;
            this.load();
        }
        catch (Exception e) {
            utils.println("<c>Nao foi possivel criar o arquivo: " + this.name, "<c>Diretorio: " + this.location.getAbsolutePath());
        }
        return this;
    }

    public boolean exists() {
        return this.file.exists();
    }

    public GsonManager load() {
        try {
            Map update = (Map)this.gson.fromJson((Reader)Files.newBufferedReader(this.file.toPath()), this.data.getClass());
            if (update != null && !update.isEmpty()) {
                this.data = update;
            }
        }
        catch (Exception e) {
            utils.println("<c>Nao foi possivel carregar o arquivo: " + this.name, "<c>Diretorio: " + this.location.getAbsolutePath());
        }
        return this;
    }

    public GsonManager save() {
        try {
            Files.write(this.file.toPath(), Collections.singletonList(this.gson.toJson(this.data)), StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
        }
        catch (Exception e) {
            utils.println("<c>Nao foi possivel salvar o arquivo: " + this.name, "<c>Diretorio: " + this.location.getAbsolutePath());
        }
        return this;
    }

    public void delete() {
        if (!this.file.delete()) {
            this.file.deleteOnExit();
        }
    }

    public File[] getFiles() {
        return this.location.listFiles();
    }

    public List<File> getFilesList() {
        return Arrays.asList(this.location.listFiles());
    }

    public GsonManager reload() {
        this.save();
        this.load();
        return this;
    }

    public String getLocationAtFile() {
        return this.location.getAbsolutePath();
    }

    public Path getLocationPath() {
        return (Path)this.location.toPath();
    }

    public File getFile() {
        return this.file;
    }

    public Path getLocationPathAtFile() {
        return (Path)this.file.toPath();
    }

    public Map<String, Object> getValues() {
        return this.data;
    }

    public Map<String, Object> getSections() {
        return this.data;
    }

    public boolean contains(String path) {
        return this.data.containsKey(path);
    }

    public GettingValue get(String path) {
        return new GettingValue(this.data.get(path));
    }

    public List<String> getStringList(String path) {
        return new GettingValue(this.data.get(path)).asList();
    }

    public String convertClassInString(Object clazz, Type type) {
        return this.gson.toJson(clazz, type);
    }

    public Object convertStringInClass(String data, Type type) {
        return this.gson.fromJson(data, type);
    }

    public List<String> getSection(String path) {
        ArrayList<String> section = new ArrayList<String>();
        for (String iteratorPath : this.data.keySet()) {
            if (!iteratorPath.startsWith(path + ".")) continue;
            String sectionPath = iteratorPath.substring(path.length() + 1);
            if (sectionPath.contains(".")) {
                String finalPath = sectionPath.substring(0, sectionPath.indexOf("."));
                if (section.contains(finalPath)) continue;
                section.add(finalPath);
                continue;
            }
            if (section.contains(sectionPath)) continue;
            section.add(sectionPath);
        }
        return section;
    }

    public GsonManager put(String path, Object value) {
        this.data.put(path, value);
        return this;
    }

    public GsonManager remove(String path) {
        this.data.remove(path);
        return this;
    }


    public /* varargs */ GsonManager put(String path, Object ... value) {
        this.data.put(path, value);
        return this;
    }

    public GsonManager put(String path, String property, String value) {
        JsonObject json = null;
        if (this.data.containsKey(path)) {
            json = (JsonObject)this.data.get(path);
        }
        if (json == null) {
            json = new JsonObject();
        }
        json.addProperty(property, value);
        this.data.put(path, (Object)json);
        return this;
    }

    public GsonManager put(String path, String property, Integer value) {
        JsonObject json = null;
        if (this.data.containsKey(path)) {
            json = (JsonObject)this.data.get(path);
        }
        if (json == null) {
            json = new JsonObject();
        }
        json.addProperty(property, (Number)value);
        this.data.put(path, (Object)json);
        return this;
    }

    public GsonManager put(String path, String property, Double value) {
        JsonObject json = null;
        if (this.data.containsKey(path)) {
            json = (JsonObject)this.data.get(path);
        }
        if (json == null) {
            json = new JsonObject();
        }
        json.addProperty(property, (Number)value);
        this.data.put(path, (Object)json);
        return this;
    }

    public GsonManager put(String path, String property, Long value) {
        JsonObject json = null;
        if (this.data.containsKey(path)) {
            json = (JsonObject)this.data.get(path);
        }
        if (json == null) {
            json = new JsonObject();
        }
        json.addProperty(property, (Number)value);
        this.data.put(path, (Object)json);
        return this;
    }

    public GsonManager put(String path, String property, Byte value) {
        JsonObject json = null;
        if (this.data.containsKey(path)) {
            json = (JsonObject)this.data.get(path);
        }
        if (json == null) {
            json = new JsonObject();
        }
        json.addProperty(property, (Number)value);
        this.data.put(path, (Object)json);
        return this;
    }

    public GsonManager put(String path, String property, Float value) {
        JsonObject json = null;
        if (this.data.containsKey(path)) {
            json = (JsonObject)this.data.get(path);
        }
        if (json == null) {
            json = new JsonObject();
        }
        json.addProperty(property, (Number)value);
        this.data.put(path, (Object)json);
        return this;
    }

    public GsonManager put(String path, String property, Boolean value) {
        JsonObject json = null;
        if (this.data.containsKey(path)) {
            json = (JsonObject)this.data.get(path);
        }
        if (json == null) {
            json = new JsonObject();
        }
        json.addProperty(property, value);
        this.data.put(path, (Object)json);
        return this;
    }

    public GsonManager put(String path, String property, Short value) {
        JsonObject json = null;
        if (this.data.containsKey(path)) {
            json = (JsonObject)this.data.get(path);
        }
        if (json == null) {
            json = new JsonObject();
        }
        json.addProperty(property, (Number)value);
        this.data.put(path, (Object)json);
        return this;
    }

    public class GettingValue {
        private Object obj;

        public GettingValue(Object obj) {
            this.obj = obj;
        }

        public String asString() {
            return this.obj + "";
        }


        public <K, V> Map<K, V> asMap() {
            return (Map)this.obj;
        }

        public <E> List<E> asList() {
            return (List)this.obj;
        }

        public Boolean asBoolean() {
            return Boolean.parseBoolean(this.asString());
        }

        public Integer asInt() {
            return this.asDouble().intValue();
        }

        public Double asDouble() {
            return Double.parseDouble(this.asString());
        }

        public Long asLong() {
            return this.asDouble().longValue();
        }

        public Byte asByte() {
            return this.asDouble().byteValue();
        }

        public Float asFloat() {
            return Float.valueOf(this.asDouble().floatValue());
        }

        public Short asShort() {
            return this.asDouble().shortValue();
        }

        public JsonObject asProperty() {
            return (JsonObject)this.obj;
        }

        public Object getValue() {
            return this.obj;
        }

        public BigDecimal asBigDecimal() {
            return new BigDecimal(this.asDouble());
        }
    }

}

