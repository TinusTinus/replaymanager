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
}
