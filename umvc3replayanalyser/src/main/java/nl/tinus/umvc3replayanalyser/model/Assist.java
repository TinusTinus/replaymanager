package nl.tinus.umvc3replayanalyser.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Representation of an assist for a specific character.
 * 
 * @author Martijn van de Rijdt
 */
@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class Assist {
    /** Assist type. */
    @NonNull
    private final AssistType type;
    /** Assist character. */
    @NonNull
    private final Umvc3Character character;
    
    /** @return name of the assist */
    public String getName() {
        return character.getAssistName(type);
    }
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return getName();
    }
}
