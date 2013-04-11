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
    
    /**
     * Gets the assist type, while handling null values.
     * 
     * @param assist
     *            assist, may be null
     * @return assist type, or null if the given assist is null
     */
    public static AssistType getType(Assist assist) {
        AssistType result;
        if (assist == null) {
            result = null;
        } else {
            result = assist.getType();
        }
        return result;
    }
    
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
