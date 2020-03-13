package net.w2p.appfx.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import io.datafx.controller.ViewController;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import net.w2p.Shared.util.ValidateUtils;
import net.w2p.appfx.components.DialogBuilder;

import javax.annotation.PostConstruct;

@ViewController(value = "/fxml/views/Login.fxml", title = "系统登录")
public class LoginController {
    @FXML
    private JFXTextField txtAccount;
    @FXML
    private JFXPasswordField txtPwd;
    @FXML
    private JFXButton btnLogin;

    @PostConstruct
    public void init() {

        txtAccount.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                txtAccount.validate();
            }
        });
        txtPwd.focusedProperty().addListener((o, oldVal, newVal) -> {
            if (!newVal) {
                txtPwd.validate();
            }
        });
        btnLogin.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(ValidateUtils.isEmpty(txtAccount.getText())){
                    txtAccount.validate();
                }
                if(ValidateUtils.isEmpty(txtPwd.getText())){
                    txtPwd.validate();
                }
                if(ValidateUtils.isEmpty(txtAccount.getText())
                || ValidateUtils.isEmpty(txtPwd.getText())
                ){
                    new DialogBuilder(btnLogin)
                            .setTitle("错误")
                            .setMessage("请检查您的填写")
                            .setPositiveBtn("确认","blue")
                            .create();
                    return;
                }

            }
        });
    }
}
