package nl.tinus.umvc3replayanalyser.model;

import lombok.extern.slf4j.Slf4j;

import nl.tinus.umvc3replayanalyser.model.AssistType;
import nl.tinus.umvc3replayanalyser.model.Umvc3Character;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for Umvc3Character.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class Umvc3CharacterTest {
    /** Checks the number of characters. */
    @Test
    public void testNumber() {
        Assert.assertEquals(50, Umvc3Character.values().length);
    }
    
    /** Tests the toString method and the assist names. All values are also logged. */
    @Test
    public void testToStringAndAssists() {
        for (Umvc3Character character: Umvc3Character.values()) {
            String string = character.toString();
            Assert.assertNotNull(string);
            log.info(string);
            
            String alpha = character.getAlphaAssistName();
            Assert.assertNotNull(alpha);
            Assert.assertEquals(alpha, character.getAssistName(AssistType.ALPHA));
            
            String beta = character.getBetaAssistName();
            Assert.assertNotNull(beta);
            Assert.assertEquals(beta, character.getAssistName(AssistType.BETA));
            
            String gamma = character.getGammaAssistName();
            Assert.assertNotNull(gamma);
            Assert.assertEquals(gamma, character.getAssistName(AssistType.GAMMA));
            
            for (AssistType type: AssistType.values()) {
                log.info(String.format("  %5s: %s", type, character.getAssistName(type)));
            }
        }
    }
}
