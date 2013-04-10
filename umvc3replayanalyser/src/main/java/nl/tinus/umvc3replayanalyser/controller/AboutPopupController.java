package nl.tinus.umvc3replayanalyser.controller;

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
import nl.tinus.umvc3replayanalyser.gui.ErrorMessagePopup;

/**
 * Controller for the about box view.
 * 
 * @author Martijn van de Rijdt
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
class AboutPopupController {
    /** Map from hyperlink texts to corresponding URLs. */
    private static final Map<String, URI> URIS = new HashMap<>();

    // Initialisation of the URLS map.
    {
        try {
            URIS.put("JavaFX", new URI("http://docs.oracle.com/javafx/"));
            URIS.put("Xuggler", new URI("http://www.xuggle.com/xuggler"));
            // TODO add the following as well
            // <Hyperlink layoutX="14.0" layoutY="109.0" text="Slf4j" onAction="#handleHyperlinkAction" />
            // <Hyperlink layoutX="14.0" layoutY="131.0" text="Log4j" onAction="#handleHyperlinkAction" />
            // <Hyperlink layoutX="14.0" layoutY="153.0" text="Commons-CLI" onAction="#handleHyperlinkAction" />
            // <Hyperlink layoutX="14.0" layoutY="175.0" text="Commons-lang" onAction="#handleHyperlinkAction" />
            // <Hyperlink layoutX="14.0" layoutY="197.0" text="Guava" onAction="#handleHyperlinkAction" />
            // <Hyperlink layoutX="15.0" layoutY="219.0" text="Jackson" onAction="#handleHyperlinkAction" />
            // <Hyperlink layoutX="103.0" layoutY="65.0" text="BCL for Java SE" onAction="#handleHyperlinkAction" />
            // <Hyperlink layoutX="104.0" layoutY="87.0" text="GPL v3" onAction="#handleHyperlinkAction" />
            // <Hyperlink layoutX="104.0" layoutY="109.0" text="MIT" onAction="#handleHyperlinkAction" />
            // <Hyperlink layoutX="103.0" layoutY="131.0" text="Apache License v2.0" onAction="#handleHyperlinkAction"
            // />
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
