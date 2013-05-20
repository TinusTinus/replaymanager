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
package nl.tinus.umvc3replayanalyser.model;

import java.io.IOException;

import lombok.extern.slf4j.Slf4j;

import org.codehaus.jackson.map.ObjectMapper;
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
    
    /** 
     * Tests converting a value to a JSON string and back. 
     * 
     * @throws IOException unexpected
     */
    @Test
    public void testJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Umvc3Character character = Umvc3Character.CHUN_LI;
        
        // marshal
        String string = mapper.writeValueAsString(character);

        Assert.assertNotNull(string);
        log.info("JSON: " + string);
        
        // unmarshal
        Umvc3Character unmarshalled = mapper.readValue(string, Umvc3Character.class);
        
        Assert.assertEquals(character, unmarshalled);
    }
}
