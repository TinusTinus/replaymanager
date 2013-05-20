package nl.tinus.umvc3replayanalyser.controller;


import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for FileUtils.
 * 
 * Based on Milan Aleksic's implementation as available on Stack Overflow:
 * http://stackoverflow.com/a/3054692.
 * 
 * @author Martijn van de Rijdt
 */
public class FileUtilsTest {
    @Test
    public void testGetRelativePathsUnix() throws PathResolutionException {
        Assert.assertEquals("stuff/xyz.dat", FileUtils.getRelativePath("/var/data/stuff/xyz.dat", "/var/data/", "/"));
        Assert.assertEquals("../../b/c", FileUtils.getRelativePath("/a/b/c", "/a/x/y/", "/"));
        Assert.assertEquals("../../b/c", FileUtils.getRelativePath("/m/n/o/a/b/c", "/m/n/o/a/x/y/", "/"));
    }

    @Test
    public void testGetRelativePathFileToFile() throws PathResolutionException {
        String target = "C:\\Windows\\Boot\\Fonts\\chs_boot.ttf";
        String base = "C:\\Windows\\Speech\\Common\\sapisvr.exe";

        String relPath = FileUtils.getRelativePath(target, base, "\\");
        Assert.assertEquals("..\\..\\Boot\\Fonts\\chs_boot.ttf", relPath);
    }

    @Test
    public void testGetRelativePathDirectoryToFile() throws PathResolutionException {
        String target = "C:\\Windows\\Boot\\Fonts\\chs_boot.ttf";
        String base = "C:\\Windows\\Speech\\Common\\";

        String relPath = FileUtils.getRelativePath(target, base, "\\");
        Assert.assertEquals("..\\..\\Boot\\Fonts\\chs_boot.ttf", relPath);
    }

    @Test
    public void testGetRelativePathFileToDirectory() throws PathResolutionException {
        String target = "C:\\Windows\\Boot\\Fonts";
        String base = "C:\\Windows\\Speech\\Common\\foo.txt";

        String relPath = FileUtils.getRelativePath(target, base, "\\");
        Assert.assertEquals("..\\..\\Boot\\Fonts", relPath);
    }

    @Test
    public void testGetRelativePathDirectoryToDirectory() throws PathResolutionException {
        String target = "C:\\Windows\\Boot\\";
        String base = "C:\\Windows\\Speech\\Common\\";
        String expected = "..\\..\\Boot";

        String relPath = FileUtils.getRelativePath(target, base, "\\");
        Assert.assertEquals(expected, relPath);
    }

    @Test(expected = PathResolutionException.class)
    public void testGetRelativePathDifferentDriveLetters() throws PathResolutionException {
        String target = "D:\\sources\\recovery\\RecEnv.exe";
        String base = "C:\\Java\\workspace\\AcceptanceTests\\Standard test data\\geo\\";

        FileUtils.getRelativePath(target, base, "\\");
    }
}
