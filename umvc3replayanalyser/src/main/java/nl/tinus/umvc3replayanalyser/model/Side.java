package nl.tinus.umvc3replayanalyser.model;

import lombok.AllArgsConstructor;

/**
 * Representation of the two sides in a game.
 * 
 * @author Martijn van de Rijdt
 */
@AllArgsConstructor
public enum Side {
    PLAYER_ONE("Player One"), PLAYER_TWO("Player Two");
    
    /** Name. */
    private final String name;
    
    /** {@inheritDoc} */
    @Override
    public String toString() {
        return this.name;
    }
    
    /** @return the opposite side */
    public Side getOpposite() {
        Side result;
        if (this == PLAYER_ONE) {
            result = PLAYER_TWO;
        } else if (this == PLAYER_TWO) {
            result = PLAYER_ONE;
        } else {
            throw new IllegalStateException("Unexpected side: " + this);
        }
        return result;
    }
}
