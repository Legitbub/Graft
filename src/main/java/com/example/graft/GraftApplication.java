package com.example.graft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.vaadin.flow.theme.aura.Aura;
import com.vaadin.flow.component.dependency.StyleSheet;

@SpringBootApplication
@StyleSheet(Aura.STYLESHEET)
@StyleSheet("styles.css")
public class GraftApplication {

    public static void main(String[] args) {
        SpringApplication.run(GraftApplication.class, args);
    }
}
