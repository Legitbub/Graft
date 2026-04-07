package com.example.graft.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("")
@AnonymousAllowed // public entry page
public class MainView extends VerticalLayout {
    public MainView() {
        add(new H1("Simulation Active"));
        add(new Button("Check Neo4j Connection"));
    }
}