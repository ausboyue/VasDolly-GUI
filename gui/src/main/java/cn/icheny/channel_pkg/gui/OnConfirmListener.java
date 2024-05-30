package cn.icheny.channel_pkg.gui;

interface OnConfirmListener {
    void onConfirm(String inputPath, String outputPath, String chanelPath, boolean sign, boolean v3Sign, String keystoreFilePath, String keystorePassword, String alias, String aliasPassword);
}
