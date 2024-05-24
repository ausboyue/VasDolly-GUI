package cn.icheny.channel_pkg.gui;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesUtil {
    public static final String KEY_INPUT_FILE = "inputFile";
    public static final String KEY_OUTPUT_DIR = "outputDir";
    public static final String KEY_CHANNEL_FILE_KEY = "channelFile";
    private static final String PROPERTIES_FILE = "config_cache.properties";
    private static PropertiesUtil sInstance;
    private final Properties mProperties = new Properties(5);

    private PropertiesUtil() {

    }

    public static PropertiesUtil getInstance() {
        if (sInstance == null) {
            sInstance = new PropertiesUtil();
            try {
                sInstance.mProperties.load(Files.newInputStream(Paths.get(PROPERTIES_FILE)));
            } catch (Exception ignored) {
            }
        }
        return sInstance;
    }

    public String get(String key) {
        return mProperties.getProperty(key, "");
    }

    public PropertiesUtil set(String key, String value) {
        mProperties.setProperty(key, value);
        return sInstance;
    }

    public void store() {
        try {
            mProperties.store(Files.newOutputStream(Paths.get(PROPERTIES_FILE)), null);
        } catch (Exception ignored) {
        }
    }
}
