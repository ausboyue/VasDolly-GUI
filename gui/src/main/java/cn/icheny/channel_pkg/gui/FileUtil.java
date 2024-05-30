package cn.icheny.channel_pkg.gui;

import java.io.File;

/**
 * @author www.icheny.cn
 * @since 2024.05.29
 */
public class FileUtil {
    public static String getCurrentDir() {
        return Util.isEmpty(Main.sJarPath) ? new File("").getAbsolutePath() : Main.sJarPath;
    }
}
