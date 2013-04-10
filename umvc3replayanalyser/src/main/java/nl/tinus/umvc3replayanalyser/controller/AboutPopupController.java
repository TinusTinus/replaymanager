package nl.tinus.umvc3replayanalyser.controller;

import java.util.HashMap;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for the about box view.
 * 
 * @author Martijn van de Rijdt
 */
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
class AboutPopupController {
    /** Map from hyperlink texts to corresponding URLs. */
    private static final Map<String, String> URLS = new HashMap<>();

    // Initialisation of the URLS map.
    {
        URLS.put("JavaFX", "http://docs.oracle.com/javafx/");
        URLS.put("Xuggler", "http://www.xuggle.com/xuggler");
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
        // <Hyperlink layoutX="103.0" layoutY="131.0" text="Apache License v2.0" onAction="#handleHyperlinkAction" />

    }

    /**
     * Action handler for when one of the hyperlinks has been clicked.
     * 
     * @param event
     *            event which led to this method being called; source must be a hyperlink whose text occurs in URLS
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

        String url = URLS.get(linkText);
        if (url == null) {
            throw new IllegalArgumentException("Unknow link text: " + linkText + ", must be one of the following: "
                    + URLS.keySet());
        }
        
        // TODO open url in browser
    }
}
