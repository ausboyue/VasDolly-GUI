package cn.icheny.channel_pkg.gui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * @author www.icheny.cn
 * @since 2024.05.29
 */
public class PropertiesUtil {
    public static final String KEY_INPUT_FILE = "inputFile";
    public static final String KEY_OUTPUT_DIR = "outputDir";
    public static final String KEY_CHANNEL_FILE = "channelFile";
    public static final String KEY_SIGN_FILE = "signFile";
    public static final String KEY_SIGN_STORE_PWD = "signStorePwd";
    public static final String KEY_SIGN_ALIAS_PWD = "signAliasPwd";
    private static final String PROPERTIES_FILE = "/caches/config.properties";
    private static PropertiesUtil sInstance;
    private final Properties mProperties = new Properties(10);

    private PropertiesUtil() {

    }

    public static PropertiesUtil getInstance() {
        if (sInstance == null) {
            sInstance = new PropertiesUtil();
            try {
                sInstance.mProperties.load(Files.newInputStream(Paths.get(FileUtil.getCurrentDir() + PROPERTIES_FILE)));
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
            File file = new File(FileUtil.getCurrentDir() + PROPERTIES_FILE);
            File dir = file.getParentFile();

            // 检查并创建目录
            if (!dir.exists()) {
                boolean dirsCreated = dir.mkdirs();
                if (!dirsCreated) {
                    System.err.println("无法创建缓存目录");
                }
            }
            mProperties.store(Files.newOutputStream(Paths.get(FileUtil.getCurrentDir() + PROPERTIES_FILE)), null);
        } catch (Exception ignored) {
        }
    }
}
