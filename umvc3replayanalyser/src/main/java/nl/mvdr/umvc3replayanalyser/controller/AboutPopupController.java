/*
 * Copyright 2012, 2013 Martijn van de Rijdt 
 * 
 * This file is part of the Ultimate Marvel vs Capcom 3 Replay Manager.
 * 
 * The Ultimate Marvel vs Capcom 3 Replay Manager is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * The Ultimate Marvel vs Capcom 3 Replay Manager is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with the Ultimate Marvel vs Capcom 3
 * Replay Manager. If not, see <http://www.gnu.org/licenses/>.
 */
package nl.mvdr.umvc3replayanalyser.controller;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.mvdr.umvc3replayanalyser.gui.ErrorMessagePopup;

/**
 * Controller for the about box view.
 * 
 * @author Martijn van de Rijdt
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
class AboutPopupController {
    /** Map from hyperlink texts to corresponding URIs. */
    private static final Map<String, URI> URIS = new HashMap<>();

    // Initialisation of the URIS map.
    {
        try {
            URIS.put("JavaFX", new URI("http://docs.oracle.com/javafx/"));
            URIS.put("Xuggler", new URI("http://www.xuggle.com/xuggler"));
            URIS.put("Slf4j", new URI("http://www.slf4j.org/"));
            URIS.put("Log4j", new URI("http://logging.apache.org/log4j/1.2/"));
            URIS.put("Commons-CLI", new URI("http://commons.apache.org/proper/commons-cli/"));
            URIS.put("Commons-IO", new URI("http://commons.apache.org/proper/commons-io/"));
            URIS.put("Commons-lang", new URI("http://commons.apache.org/proper/commons-lang/"));
            URIS.put("Guava", new URI("http://code.google.com/p/guava-libraries/"));
            URIS.put("Jackson", new URI("http://jackson.codehaus.org/"));
            URIS.put("BCL for Java SE", new URI("http://www.oracle.com/technetwork/java/javase/terms/license/index.html"));
            URIS.put("GPL v3", new URI("http://www.gnu.org/licenses/gpl.html"));
            URIS.put("MIT", new URI("http://opensource.org/licenses/MIT"));
            URIS.put("Apache License v2.0", new URI("http://www.apache.org/licenses/LICENSE-2.0.html"));
        } catch (URISyntaxException e) {
            throw new RuntimeException("Unable to complete class initialisation.", e);
        }
    }
    
    /**
     * Action handler for when one of the hyperlinks has been clicked.
     * 
     * @param event
     *            event which led to this method being called; source must be a hyperlink whose text occurs in URIS
     */
    @FXML
    private void handleHyperlinkAction(final ActionEvent event) {
        Object source = event.getSource();
        if (!(source instanceof Hyperlink)) {
            throw new IllegalArgumentException("handleHyperlinkAction called with a source that is not a hyperlink: "
                    + source);
        }
        String linkText = ((Hyperlink) source).getText();

        log.info("Hyperlink clicked: " + linkText);

        URI uri = URIS.get(linkText);
        if (uri == null) {
            throw new IllegalArgumentException("Unknow link text: " + linkText + ", must be one of the following: "
                    + URIS.keySet());
        }
        
        if (Desktop.isDesktopSupported()) {
            try {
                log.info("Browsing to uri: " + uri);
                Desktop.getDesktop().browse(uri);
            } catch (IOException e) {
                log.error("Unable to browse to URI: " + uri, e);
                // Show an error message to the user.
                ErrorMessagePopup.show("Unable to open link", "Unable to open link: " + uri, e);
            }
        } else {
            log.error("Unable to browse to URI: " + uri + " , desktop not supported!");
            // Show an error message to the user.
            ErrorMessagePopup.show("Unable to open link", "Unable to open link: " + uri, null);
        }
    }
}
