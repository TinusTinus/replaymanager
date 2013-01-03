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
        Assert.assertNotNull(configuration.getTesseractExecutablePath());
        Assert.assertNotNull(configuration.getDataDirectoryPath());
        configuration.isMoveVideoFilesToDataDirectory();
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
    }
}
