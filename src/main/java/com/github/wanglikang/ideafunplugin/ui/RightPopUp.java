package com.github.wanglikang.ideafunplugin.ui;

import com.github.wanglikang.ideafunplugin.core.GenerateContext;
import com.github.wanglikang.ideafunplugin.util.ThreadUtil;
import com.github.wanglikang.ideafunplugin.classloader.MyClassLoader;
import com.github.wanglikang.ideafunplugin.core.task.AutoGenerateTask;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.popup.*;
import com.intellij.psi.PsiFile;
import com.intellij.ui.components.JBPanel;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.function.Supplier;

public class RightPopUp extends AnAction {

    private ThreadPoolExecutor pool = ThreadUtil.getBackgroundThreadPool();

    @Setter
    private String packageName  ="";

    @Getter
    private JTextField jTextField;

    @Override
    public void actionPerformed(AnActionEvent e) {

        Editor editor = e.getData(PlatformDataKeys.EDITOR);
        @Nullable PsiFile psiFile = e.getData(LangDataKeys.PSI_FILE);
        String text = psiFile.getText();
        JBPopup popup = createDialogPanel(psiFile);
        popup.showInFocusCenter();

    }


    /**
     * 创建在文件编辑器内右键点击之后回跳出来的提示输入框
     * @return
     */
    public JBPopup  createDialogPanel(PsiFile psiFile){
        JBPopupFactory factory = JBPopupFactory.getInstance();
        JBPanel jbPanel = new JBPanel();
        JLabel packageLabel = new JLabel("请输入完整包名");
        JTextField packageNameInput = new JTextField(30);

        JButton okBtn = new JButton("确认");
        JButton cancelBtn = new JButton("取消");
        packageLabel.setLabelFor(packageNameInput);

        jTextField = packageNameInput;
        packageNameInput.setFocusable(true);
        jbPanel.add(packageNameInput);
        jbPanel.add(packageLabel);
        jbPanel.add(okBtn);
        jbPanel.add(cancelBtn);
        @NotNull ComponentPopupBuilder builder = factory.createComponentPopupBuilder(jbPanel, jbPanel);
        @NotNull JBPopup popup = builder.setResizable(true)
                .setModalContext(false)
                .setFocusable(true)
                .setRequestFocus(true)
                .setCancelOnWindowDeactivation(true)
                .setCancelOnOtherWindowOpen(true)
                .setMovable(true)
                .setCancelKeyEnabled(true)
                .setCancelOnClickOutside(true)
                .setTitle("请完善信息")
                .setMinSize(new Dimension(100,50))
                .setCancelButton(new IconButton("取消", Messages.getInformationIcon()))
                .createPopup();
        OnClickListener listener = new OnClickListener(psiFile,this,popup);
        okBtn.addActionListener(listener);
        cancelBtn.addActionListener(v->{ popup.cancel(); });
        return popup;
    }

    class OnClickListener implements ActionListener {

        private PsiFile psiFile;
        private RightPopUp rightPopUp;
        private JBPopup popup;

        public OnClickListener(PsiFile psiFile, RightPopUp rightPopUp, JBPopup popup ){
            this.psiFile = psiFile;
            this.rightPopUp = rightPopUp;
            this.popup   = popup;
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            rightPopUp.setPackageName(rightPopUp.jTextField.getText());
            System.out.println("packageName :"+packageName);


            final CompletableFuture<Class> classCompletableFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return loadClass(psiFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return null;
            }, pool);

            classCompletableFuture.thenAccept((cls)->{
                System.out.println(cls);
                @NotNull String className = psiFile.getVirtualFile().getNameWithoutExtension();
                GenerateContext context = new GenerateContext();
                context.setBeanName(className);
                context.setClassFilePath("");
                context.setCls(cls);
                context.setPackageName(packageName);
                pool.execute(new AutoGenerateTask(context));
            });

            popup.cancel();
        }
    }

    public Class<?> loadClass(PsiFile psiFile) throws IOException {

        byte[] targetBuffer = psiFile.getVirtualFile().contentsToByteArray();
        @NotNull String className = psiFile.getVirtualFile().getNameWithoutExtension();
        @NotNull String name = psiFile.getVirtualFile().getName();
        final String[] split = psiFile.getVirtualFile().getName().split("\\.");
        String extendType = split[split.length - 1];

        MyClassLoader myClassLoader = new MyClassLoader();
        myClassLoader.setPackageName(packageName);
        myClassLoader.setTargetByte(targetBuffer);
        Class<?> cls = myClassLoader.loadTargetClass(className);
        return  cls;
    }

}
