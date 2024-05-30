package cn.icheny.channel_pkg.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author www.icheny.cn
 * @since 2024.05.29
 */
public class ApkSigner {
    @SuppressWarnings("CheckResult")
    public static String sign(String apkFilePath, String apkOutFileDir, String storeFilePath, String storePassword, String alias, String aliasPassword, boolean v3Sign) {
        try {
            String currentPath = FileUtil.getCurrentDir();
            final String toolsPath = currentPath + "/tools/";
            final File file = new File(apkFilePath);
            final String fileName = file.getName();
            String alignedMame = Util.appendFileName(fileName, "aligned");
            String alignedApkFile = apkOutFileDir + "/" + alignedMame;
            // 先删除已有文件
            delFile(alignedApkFile);
            // 构建对齐命令
            String[] alignCommand = {
                    toolsPath + "zipalign", "-f", "4",
                    apkFilePath,
                    alignedApkFile
            };
            // 执行对齐命令
            executeCommand(alignCommand);

            String singedApkFile = apkOutFileDir + "/" + Util.appendFileName(fileName, "signed");
            // 先删除已有文件
            delFile(singedApkFile);
            // 构建签名命令
            String[] signCommand = {
                    toolsPath + "apksigner", "sign",
                    "--ks", storeFilePath,
                    "--ks-pass", "pass:" + storePassword,
                    "--ks-key-alias", alias,
                    "--key-pass", "pass:" + aliasPassword,
                    "--out", singedApkFile,
                    "--v1-signing-enabled", "true",
                    "--v2-signing-enabled", "true",
                    "--v3-signing-enabled", Boolean.toString(v3Sign),
                    alignedApkFile
            };
            // 执行签名命令
            executeCommand(signCommand);

            // 删除非必要的文件
            delFile(alignedApkFile);
            delFile(singedApkFile + ".idsig");
            return singedApkFile;
        } catch (Exception ignored) {
        }
        return null;
    }

    private static void delFile(String filePath) {
        final File aligedFile = new File(filePath);
        if (aligedFile.exists()) {
            aligedFile.delete();
        }
    }

    private static void executeCommand(String[] command) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        process.waitFor();
        if (process.exitValue() != 0) {
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }
            throw new RuntimeException("命令执行失败: " + String.join(" ", command));
        }
    }
}
