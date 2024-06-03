package cn.icheny.channel_pkg.gui;

import static cn.icheny.channel_pkg.gui.PropertiesUtil.KEY_CHANNEL_FILE;
import static cn.icheny.channel_pkg.gui.PropertiesUtil.KEY_INPUT_FILE;
import static cn.icheny.channel_pkg.gui.PropertiesUtil.KEY_OUTPUT_DIR;
import static cn.icheny.channel_pkg.gui.PropertiesUtil.KEY_SIGN_ALIAS_PWD;
import static cn.icheny.channel_pkg.gui.PropertiesUtil.KEY_SIGN_FILE;
import static cn.icheny.channel_pkg.gui.PropertiesUtil.KEY_SIGN_STORE_PWD;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileFilter;

/**
 * @author www.icheny.cn
 * @since 2024.05.29
 */
public class Main {
    private static JTextField signFileField;
    private static JTextField signStorePwdField;
    private static JComboBox<String> signAliasBox;
    public static String sJarPath;

    public static void main(String[] args) {

        // 获取JAR文件的路径
        String jarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        // 转换成标准的文件路径
        File jarFile = new File(jarPath);
        // 如果是JAR文件，获取其所在目录
        if (jarFile.isFile()) {
            sJarPath = jarFile.getParent();
        }

        createAndShowWindow((inputPath, outputPath, chanelPath, sign, v3Sign, storeFilePath, storePassword, alias, aliasPassword) -> {
            if (Util.isEmpty(inputPath)) {
                showMsg("请选择输入文件！");
                return;
            }
            if (Util.isEmpty(outputPath)) {
                showMsg("请选择输出目录！");
                return;
            }
            if (Util.isEmpty(chanelPath)) {
                showMsg("请选择渠道文件！");
                return;
            }
            if (sign) {
                if (Util.isEmpty(storeFilePath)) {
                    showMsg("请选择签名文件！");
                    return;
                }
                if (Util.isEmpty(storePassword)) {
                    showMsg("请输入Store密码！");
                    return;
                }
                if (Util.isEmpty(alias)) {
                    showMsg("Store密码不正确！");
                    return;
                }
                if (Util.isEmpty(aliasPassword)) {
                    showMsg("请输入别名Alias密码！");
                    return;
                }
                final String signApkPath = ApkSigner.sign(inputPath, outputPath, storeFilePath, storePassword, alias, aliasPassword, v3Sign);
                if (!Util.isEmpty(signApkPath)) {
                    inputPath = signApkPath;
                }
            }
            //渠道信息
            File channelFile = new File(chanelPath);
            List<Channel> channelList = null;
            //渠道文件
            if (channelFile.exists() && !channelFile.isDirectory()) {
                channelList = Util.readChannelFile(channelFile);
            }
            if (channelList == null || channelList.isEmpty()) {
                showMsg("请选择正确格式的渠道文件！");
                return;
            }
            Util.writeChannel(new File(inputPath), channelList, new File(outputPath), false, false);
            showMsg("共完成" + channelList.size() + "个渠道打包！");
        });
    }

    private static void createAndShowWindow(OnConfirmListener listener) {

        final PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();
        String inputFilePath = propertiesUtil.get(KEY_INPUT_FILE);
        String outputDirPath = propertiesUtil.get(KEY_OUTPUT_DIR);
        String channelFilePath = propertiesUtil.get(KEY_CHANNEL_FILE);
        String signFilePath = propertiesUtil.get(KEY_SIGN_FILE);
        String signStorePwd = propertiesUtil.get(KEY_SIGN_STORE_PWD);
        String signAliasPwd = propertiesUtil.get(KEY_SIGN_ALIAS_PWD);


        JFrame frame = new JFrame("多渠道打包工具v1.0.1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(620, 400);
        frame.setMinimumSize(new Dimension(620, 300));

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);


        /*
         * 输入文件
         */
        JLabel inputFileLabel = new JLabel("输入文件：");
        constraints.gridx = 0;
        constraints.gridy = 0;
        contentPane.add(inputFileLabel, constraints);

        JTextField inputFileField = new JTextField(inputFilePath, 25);
        constraints.gridx = 1;
        constraints.gridy = 0;
        inputFileField.setEditable(false);
        contentPane.add(inputFileField, constraints);

        /*
          输入apk文件
         */
        JButton inputFileButton = new JButton("选择APK文件");
        inputFileButton.addActionListener(e -> {
            final JFileChooser fileChooser = new JFileChooser();
            final File file = new File(inputFileField.getText());
            if (file.exists()) {
                fileChooser.setCurrentDirectory(file);
            }
            fileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().endsWith(".apk") || f.getName().endsWith(".APK");
                }

                @Override
                public String getDescription() {
                    return "APK文件";
                }
            });
            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                inputFileField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        constraints.gridx = 2;
        constraints.gridy = 0;
        contentPane.add(inputFileButton, constraints);


        /*
         * 输出目录
         */
        JLabel outputDirLabel = new JLabel("输出目录：");
        constraints.gridx = 0;
        constraints.gridy = 1;
        contentPane.add(outputDirLabel, constraints);

        JTextField outputDirField = new JTextField(outputDirPath, 25);
        constraints.gridx = 1;
        constraints.gridy = 1;
        outputDirField.setEditable(false);
        contentPane.add(outputDirField, constraints);

        JButton outputDirButton = new JButton("选择输出目录");
        outputDirButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            final File file = new File(outputDirField.getText());
            if (file.exists()) {
                fileChooser.setCurrentDirectory(file);
            }

            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                outputDirField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        constraints.gridx = 2;
        constraints.gridy = 1;
        contentPane.add(outputDirButton, constraints);

        /*
         * 渠道文件
         */
        JLabel channelFileLabel = new JLabel("渠道文件：");
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPane.add(channelFileLabel, constraints);

        JTextField channelFileField = new JTextField(channelFilePath, 25);
        constraints.gridx = 1;
        constraints.gridy = 2;
        channelFileField.setEditable(false);
        contentPane.add(channelFileField, constraints);

        JButton chanelFileButton = new JButton("选择渠道文件");
        chanelFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            final File file = new File(channelFileField.getText());
            if (file.exists()) {
                fileChooser.setCurrentDirectory(file);
            }
            // 创建一个只接受文本文件的过滤器
            fileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().endsWith(".txt");
                }

                @Override
                public String getDescription() {
                    return "TXT文本文件";
                }
            });
            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                channelFileField.setText(fileChooser.getSelectedFile().getAbsolutePath());
            }
        });
        constraints.gridx = 2;
        constraints.gridy = 2;
        contentPane.add(chanelFileButton, constraints);


        /*
         * 签名复选框
         */
        JPanel signPanel = new JPanel();
        signPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        // 创建复选框
        JCheckBox signBox = new JCheckBox("使用自定义签名");
        signPanel.add(signBox);

        // 创建复选框
        JCheckBox v3SignBox = new JCheckBox("启用v3签名");
        signPanel.add(v3SignBox);

        // 设置复选框监听器，以响应用户的选择/取消选择事件
        signBox.addActionListener(e -> {
            JCheckBox source = (JCheckBox) e.getSource();
            v3SignBox.setSelected(source.isSelected());
        });

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 3;
        contentPane.add(signPanel, constraints);


        /*
         * 签名文件
         */
        JLabel signFileLabel = new JLabel("签名文件：");
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        contentPane.add(signFileLabel, constraints);

        signFileField = new JTextField(signFilePath, 25);
        constraints.gridx = 1;
        constraints.gridy = 4;
        signFileField.setEditable(false);
        contentPane.add(signFileField, constraints);

        JButton signFileButton = new JButton("选择签名文件");
        signFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            final File file = new File(signFileField.getText());
            if (file.exists()) {
                fileChooser.setCurrentDirectory(file);
            }
            // 创建一个只接受文本文件的过滤器
            fileChooser.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().endsWith(".keystore") || f.getName().endsWith(".jks");
                }

                @Override
                public String getDescription() {
                    return "签名文件";
                }
            });
            int returnValue = fileChooser.showOpenDialog(frame);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                signFileField.setText(fileChooser.getSelectedFile().getAbsolutePath());
                loadAndShowKeystoreAliases();
            }
        });
        constraints.gridx = 2;
        constraints.gridy = 4;
        contentPane.add(signFileButton, constraints);

        /*
         * 签名密码
         */
        JLabel signStorePwdLabel = new JLabel("Store密码：");
        constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 1;
        contentPane.add(signStorePwdLabel, constraints);

        signStorePwdField = new JTextField(signStorePwd, 25);
        constraints.gridx = 1;
        constraints.gridy = 5;
        constraints.gridwidth = 1;
        // 为 JTextField 添加 DocumentListener
        signStorePwdField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                loadAndShowKeystoreAliases();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                loadAndShowKeystoreAliases();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        contentPane.add(signStorePwdField, constraints);

        /*
         * 别名Alias名称
         */
        JLabel signAliasLabel = new JLabel("别名Alias：");
        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 1;
        contentPane.add(signAliasLabel, constraints);

        signAliasBox = new JComboBox<>();
        constraints.gridx = 1;
        constraints.gridy = 6;
        constraints.gridwidth = 1;
        contentPane.add(signAliasBox, constraints);


        /*
         * 别名Alias密码
         */
        JLabel signAliasPwdLabel = new JLabel("Alias密码：");
        constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.gridwidth = 1;
        contentPane.add(signAliasPwdLabel, constraints);

        JTextField signAliasPwdField = new JTextField(signAliasPwd, 25);
        constraints.gridx = 1;
        constraints.gridy = 7;
        constraints.gridwidth = 1;
        contentPane.add(signAliasPwdField, constraints);

        /*
         * 关闭、确定按钮
         */
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton cancelButton = new JButton("关闭");
        cancelButton.addActionListener(e -> {
            frame.dispose();
            System.exit(0);
        });
        buttonPanel.add(cancelButton);

        JButton okButton = new JButton("确定");
        okButton.addActionListener(e -> {
            PropertiesUtil.getInstance()
                    .set(KEY_INPUT_FILE, inputFileField.getText())
                    .set(KEY_OUTPUT_DIR, outputDirField.getText())
                    .set(KEY_CHANNEL_FILE, channelFileField.getText())
                    .set(KEY_SIGN_FILE, signFileField.getText())
                    .set(KEY_SIGN_STORE_PWD, signStorePwdField.getText())
                    .set(KEY_SIGN_ALIAS_PWD, signAliasPwdField.getText())
                    .store();
            listener.onConfirm(inputFileField.getText(), outputDirField.getText(), channelFileField.getText(), signBox.isSelected(), v3SignBox.isSelected(), signFileField.getText(), signStorePwdField.getText(), signAliasBox.getItemAt(0), signAliasPwdField.getText());
        });
        buttonPanel.add(okButton);

        constraints.gridx = 0;
        constraints.gridy = 8;
        constraints.gridwidth = 3;
        contentPane.add(buttonPanel, constraints);

        if (!Util.isEmpty(signStorePwd) && !Util.isEmpty(signFilePath)) {
            loadAndShowKeystoreAliases();
        }

        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    public static void loadAndShowKeystoreAliases() {
        if (signFileField == null) {
            return;
        }
        final String path = signFileField.getText();
        if (Util.isEmpty(path)) {
            return;
        }
        if (signStorePwdField == null) {
            return;
        }
        final String pwd = signStorePwdField.getText();
        if (Util.isEmpty(pwd)) {
            return;
        }
        FileInputStream fis = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            fis = new FileInputStream(path);
            keyStore.load(fis, pwd.toCharArray());
            fis.close();

            final ArrayList<String> aliasList = Collections.list(keyStore.aliases());
            if (signAliasBox != null) {
                signAliasBox.removeAllItems();
                signAliasBox.setModel(new DefaultComboBoxModel<>(aliasList.toArray(new String[0])));
            }
        } catch (Exception e) {
            if (e.getCause() instanceof UnrecoverableKeyException) {
                if (signAliasBox != null) {
                    signAliasBox.removeAllItems();
                }
            }
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    private static void showMsg(String msg) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, msg, "温馨提示", JOptionPane.PLAIN_MESSAGE));
    }
}