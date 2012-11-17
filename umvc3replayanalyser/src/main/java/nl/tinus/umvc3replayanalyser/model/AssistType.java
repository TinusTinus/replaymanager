package nl.tinus.umvc3replayanalyser.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Possible values for a character's assist.
 * 
 * @author Martijn van de Rijdt
 */
@RequiredArgsConstructor
@Getter
public enum AssistType {
    ALPHA("alpha"), BETA("beta"), GAMMA("gamma");

    /** Name. */
    private final String name;

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return name;
    }
}
