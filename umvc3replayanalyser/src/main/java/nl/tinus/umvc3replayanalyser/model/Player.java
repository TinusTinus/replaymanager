package nl.tinus.umvc3replayanalyser.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Player in an Ultimate Marvel vs Capcom 3 online match on XBox Live.
 * 
 * @author Martijn van de Rijdt
 */
@Getter
@EqualsAndHashCode
public class Player {
    // For now, a player will only contain a gamertag.
    // Later on this may be enriched with other information such as rank.
    /** Gamertag. */
    @NonNull
    private final String gamertag;

    /**
     * Constructor.
     * 
     * @param gamertag
     *            gamertag
     */
    @JsonCreator
    public Player(@JsonProperty("gamertag") String gamertag) {
        super();
        this.gamertag = gamertag;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return this.gamertag;
    }
}
