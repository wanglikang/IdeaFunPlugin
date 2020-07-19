package com.github.wanglikang.ideafunplugin.ui;

import com.github.wanglikang.ideafunplugin.persistent.MyPersistentData;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.util.Icons;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author WLK
 */
public class ConfigPanel implements SearchableConfigurable {

    private JPanel settingJPanel;
    private JTextField pathField;
    private JLabel pathLabel;
    private JButton browseBtn;
    private MyPersistentData persistentData = MyPersistentData.getInstance();

    @Override
    public @NotNull String getId() {
        return "configPanel_id_1";
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "自定义插件的配置面板";
    }

    @Override
    public @Nullable JComponent createComponent() {
        if(settingJPanel!=null){
            settingJPanel.repaint();
            return  settingJPanel;
        }



        pathField = new JTextField(50);
        pathLabel = new JLabel("自定义搜索");
        settingJPanel = new JPanel();
        browseBtn = new JButton();
        browseBtn.setIcon(Icons.FILE_ICON);
        settingJPanel.add(pathLabel);
        settingJPanel.add(pathField);
        settingJPanel.add(browseBtn);
        pathLabel.setLabelFor(pathField);
        browseBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                browseDir(settingJPanel,pathField);
            }
        });
        return  settingJPanel;
    }

    private void browseDir(JPanel settingJPanel, JTextField pathField) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(1);
        if(chooser.showOpenDialog(settingJPanel)==0){
            pathField.setText(chooser.getSelectedFile().getPath());
        }
    }

    @Override
    public boolean isModified() {
        //内容是否改变了
        return !pathField.getText().equals(persistentData.getState());
    }

    @Override
    public void reset() {
        //用户点击reset，即配置被重置了
        pathField.setText("默认值");
    }

    @Override
    public void apply() throws ConfigurationException {
        //用户点击了OK
        persistentData.setData(pathField.getText());
    }
}
