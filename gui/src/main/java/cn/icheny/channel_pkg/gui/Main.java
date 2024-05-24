package cn.icheny.channel_pkg.gui;

import static cn.icheny.channel_pkg.gui.PropertiesUtil.KEY_CHANNEL_FILE_KEY;
import static cn.icheny.channel_pkg.gui.PropertiesUtil.KEY_INPUT_FILE;
import static cn.icheny.channel_pkg.gui.PropertiesUtil.KEY_OUTPUT_DIR;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

public class Main {

    public static void main(String[] args) {
        createAndShowWindow((inputPath, outputPath, chanelPath) -> {
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
        String channelFilePath = propertiesUtil.get(KEY_CHANNEL_FILE_KEY);


        JFrame frame = new JFrame("多渠道打包工具");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(620, 300);
        frame.setMinimumSize(new Dimension(620, 300));

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);


        // 输入文件
        JLabel inputFileLabel = new JLabel("输入文件：");
        constraints.gridx = 0;
        constraints.gridy = 0;
        contentPane.add(inputFileLabel, constraints);

        JTextField inputFileField = new JTextField(inputFilePath, 25);
        constraints.gridx = 1;
        constraints.gridy = 0;
        contentPane.add(inputFileField, constraints);

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


        // 输出目录
        JLabel outputDirLabel = new JLabel("输出目录：");
        constraints.gridx = 0;
        constraints.gridy = 1;
        contentPane.add(outputDirLabel, constraints);

        JTextField outputDirField = new JTextField(outputDirPath, 25);
        constraints.gridx = 1;
        constraints.gridy = 1;
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

        // 渠道文件
        JLabel chanelFileLabel = new JLabel("渠道文件：");
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPane.add(chanelFileLabel, constraints);

        JTextField channelFileField = new JTextField(channelFilePath, 25);
        constraints.gridx = 1;
        constraints.gridy = 2;
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
                    .set(KEY_CHANNEL_FILE_KEY, channelFileField.getText())
                    .store();
            listener.onConfirm(inputFileField.getText(), outputDirField.getText(), channelFileField.getText());
        });
        buttonPanel.add(okButton);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 3;
        contentPane.add(buttonPanel, constraints);

        SwingUtilities.invokeLater(() -> frame.setVisible(true));
    }

    private static void showMsg(String msg) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, msg, "温馨提示", JOptionPane.PLAIN_MESSAGE));
    }
}