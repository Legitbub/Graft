// Serves as gateway for application to build frontend

package com.example.graft;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Meta;
import com.vaadin.flow.server.PWA;

@Meta(name = "viewport", content = "width=device-width, initial-scale=1")
@Meta(name = "description", content = "Graft - Social Networking App")
@PWA(name = "Graft", shortName = "Graft")
public class AppShell implements AppShellConfigurator {
}