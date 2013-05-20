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
package nl.tinus.umvc3replayanalyser.config;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for PropertiesConfiguration.
 * 
 * @author Martijn van de Rijdt
 */
public class PropertiesConfigurationTest {
    /** Creates a new PropertiesConfiguration and checks that configuration parameters can be retrieved. */
    @Test
    public void test() {
        PropertiesConfiguration configuration = new PropertiesConfiguration();
        Assert.assertEquals("../umvc3replayanalyser-assembly/win/tesseract/tesseract.exe", configuration.getTesseractExecutablePath());
        Assert.assertNotNull("src/test/resources/data", configuration.getDataDirectoryPath());
        Assert.assertFalse(configuration.isMoveVideoFilesToDataDirectory());
        Assert.assertFalse(configuration.isSavePreviewImageToDataDirectory());
        Assert.assertTrue(configuration.isPrettyPrintReplays());
    }

    /** Creates a new PropertiesConfiguration where a mandatory property has been omitted. */
    @Test(expected = IllegalStateException.class)
    public void testMissingTesseractExecutable() {
        new PropertiesConfiguration("/config-no-tesseract.properties");
    }

    /** Creates a new PropertiesConfiguration where non-mandatory properties have been omitted. */
    @Test
    public void testDefaults() {
        PropertiesConfiguration configuration = new PropertiesConfiguration("/config-defaults.properties");
        Assert.assertNotNull(configuration.getTesseractExecutablePath());
        Assert.assertEquals("../data", configuration.getDataDirectoryPath());
        Assert.assertTrue(configuration.isMoveVideoFilesToDataDirectory());
        Assert.assertTrue(configuration.isSavePreviewImageToDataDirectory());
        Assert.assertFalse(configuration.isPrettyPrintReplays());
    }
}
