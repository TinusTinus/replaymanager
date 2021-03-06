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
package nl.mvdr.umvc3replayanalyser.ocr;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import lombok.extern.slf4j.Slf4j;
import nl.mvdr.umvc3replayanalyser.config.Configuration;
import nl.mvdr.umvc3replayanalyser.model.Umvc3Character;

import org.apache.commons.lang3.StringUtils;

/**
 * Uses a native Tesseract installation to perform optical character recognition.
 * 
 * @author Martijn van de Rijdt
 */
@Slf4j
public class TesseractOCREngine implements OCREngine {
    /** Image format used to temporarily store images. */
    private static final String IMAGE_FORMAT = "png";
    /** Suffix for temporary image files. */
    private static final String IMAGE_SUFFIX = "." + IMAGE_FORMAT;
    /** Suffix for temporary text files. */
    private static final String TEXT_SUFFIX = ".txt";
    /** Location of the Tesseract executable. */

    /** Configuration. */
    private final Configuration configuration;

    /**
     * Constructor.
     * 
     * @param configuration
     *            configuration
     */
    public TesseractOCREngine(Configuration configuration) {
        super();
        this.configuration = configuration;

        // Check that the given configuration contains a working tesseract executable by attempting to retrieve Tesseract's version number.
        try {
            log.info("Tesseract version: " + getTesseractVersion());
        } catch (OCRException e) {
            throw new IllegalArgumentException(
                    "Unable to invoke Tesseract. Please check if the configuration in /etc/configuration.properties is correct and/or that the Tesseract executable is available at "
                            + configuration.getTesseractExecutablePath(), e);
        }
    }

    /**
     * Retrieves Tesseract's version number.
     * 
     * @throws OCRException
     *             in case the version number cannot be retrieved
     * @return Tesseract's version number
     */
    public String getTesseractVersion() throws OCRException {
        // TODO check that the use of quotes works on Linux / Mac.
        String command = String.format("\"%s\" -v", configuration.getTesseractExecutablePath());
        String firstLine = execute(command);
        if (firstLine == null) {
            throw new OCRException("Unable to determine Tesseract version number; Tesseract did not write anything to its output.");
        }
        String result;
        if (firstLine.startsWith("tesseract ")) {
            result = firstLine.substring(10);
        } else {
            result = firstLine;
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public String ocrLine(BufferedImage image) throws OCRException {
        // This method creates a few temporary files. There are marked for deletion when the JVM terminates in case
        // anything goes wrong, bu we will try to delete them at the end of this method.
        List<File> tempFilesToBeDeleted = new ArrayList<>(2);

        String result;
        try {
            // Store the image as a temporary file.
            File imageFile = createTempFile("ocrimage", IMAGE_SUFFIX);
            tempFilesToBeDeleted.add(imageFile);
            ImageIO.write(image, IMAGE_FORMAT, imageFile);

            // Tesseract needs a text file to write its output. Create another temporary file for this.
            File textFile = createTempFile("ocrtext", TEXT_SUFFIX);
            tempFilesToBeDeleted.add(textFile);

            // Invoke tesseract.
            runTesseract(imageFile, textFile);

            // Read the contents of the text file.
            result = readLineFromFile(textFile);
        } catch (IOException e) {
            throw new OCRException(e);
        } finally {
            // Clean up the temporary files.
            for (File file : tempFilesToBeDeleted) {
                boolean deleted = file.delete();
                if (deleted) {
                    log.debug("Deleted temporary file: " + file);
                } else {
                    log.warn("Could not delete temporary file: " + file
                            + ". This file should be deleted when the JVM is stopped.");
                }
            }
        }

        log.debug("Read text: " + result);
        return result;
    }

    /**
     * Creates a temporary file using File.createTempFile, logs this and makes sure that, if not deleted before then,
     * the file will be deleted once the JVM terminates.
     * 
     * @param prefix
     *            prefix
     * @param suffix
     *            suffix
     * @return file
     * @throws IOException
     *             in case the file cannot be created
     */
    private File createTempFile(String prefix, String suffix) throws IOException {
        File result = File.createTempFile(prefix, suffix);
        log.debug("Created temporary file: " + result);
        result.deleteOnExit();
        return result;
    }

    /**
     * Runs Tesseract.
     * 
     * @param imageFile
     *            image file which Tesseract should read
     * @param textFile
     *            text file where Tesseract should write its output
     * @throws IOException
     *             in case an I/O error occurs when trying to start Tesseract
     * @throws InterruptedException
     *             in case the wait for Tesseract completion is interrupted
     * @throws OCRException
     *             in case Tesseract execution fails
     */
    private void runTesseract(File imageFile, File textFile) throws OCRException {
        String outbase = textFile.getAbsolutePath().substring(0,
                textFile.getAbsolutePath().length() - TEXT_SUFFIX.length());
        // TODO Test if this works on Linux / Mac.
        // This version uses quotes to deal with spaces in directory names on Windows,
        // but I'm not sure if this works on Linux.
        String command = String.format("\"%s\" \"%s\" \"%s\"", configuration.getTesseractExecutablePath(),
                imageFile.getAbsolutePath(), outbase);

        execute(command);
    }

    /**
     * Executes the given command.
     * 
     * @param command
     *            command to be executed
     * @throws OCRException
     *             if the command returns a nonzero exit code, the command is interrupted or there is an unexpected I/O
     *             exception
     * @return the first line of the command's output
     */
    private String execute(String command) throws OCRException {
        if (log.isDebugEnabled()) {
            log.debug("Executing command: " + command);
        }
        String firstLine;
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(command);

            // Log the process's output and capture the first line.
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line = reader.readLine();
                firstLine = line;
                if (log.isDebugEnabled()) {
                    while (line != null) {
                        log.debug("tesseract: " + line);
                        line = reader.readLine();
                    }
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new OCRException("Command failed, exit code: " + exitCode + ", command: " + command);
            }
        } catch (IOException | InterruptedException e) {
            throw new OCRException(e);
        }
        return firstLine;
    }

    /**
     * Reads the nonempty line from the given text file.
     * 
     * @param textFile
     *            text file to be read
     * @return contents of the nonempty line in the file
     * @throws IOException
     *             if reading the file fails due to an I/O error
     * @throws OCRException
     *             if the file is empty or contains multiple nonempty lines
     */
    private String readLineFromFile(File textFile) throws IOException, OCRException {
        if (log.isDebugEnabled()) {
            log.debug("Reading from file: " + textFile);
        }
        String result;
        List<String> lines = Files.readAllLines(textFile.toPath(), Charset.defaultCharset());
        result = "";
        for (String line : lines) {
            if ("".equals(result)) {
                result = line;
            } else if (!"".equals(line)) {
                // We've already read a nonempty line, now we've found another one.
                throw new OCRException("Image contains more than one line of text: " + lines);
            } // else: empty line, skip it
        }
        if ("".equals(result)) {
            throw new OCRException("No text found.");
        }
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public Umvc3Character ocrCharacter(BufferedImage image) throws OCRException {
        String text = ocrLine(image);
        Umvc3Character result = matchToCharacterName(text);
        log.debug("Read character: " + result);
        return result;
    }

    /**
     * Matches the given string to a character's name.
     * 
     * @param text
     *            text to be matched, should be a Marvel character name, non-null
     * @return the character to whose name the given text is closest
     * @throws OCRException
     *             in case the matching character cannot be uniquely determined
     */
    private Umvc3Character matchToCharacterName(String text) throws OCRException {
        return matchToCharacterName(text, EnumSet.allOf(Umvc3Character.class));
    }

    /**
     * Matches the given string to a character's name.
     * 
     * @param text
     *            text to be matched, should be a Marvel character name
     * @param possibleCharacters
     *            the characters that text may match, may not be empty
     * @return the character to whose name the given text is closest
     * @throws OCRException
     *             in case the matching character cannot be uniquely determined
     */
    private Umvc3Character matchToCharacterName(String text, Set<Umvc3Character> possibleCharacters)
            throws OCRException {
        if (log.isDebugEnabled()) {
            if (possibleCharacters.size() == Umvc3Character.values().length) {
                log.debug(String.format("Attempting to match %s to the UMvC3 characters", text));
            } else {
                log.debug(String.format("Attempting to match %s to the following characters: %s", text,
                        possibleCharacters));
            }
        }

        // Compute the minimal Levenshtein distance between the given text and the uppercase character names.
        int minimalDistance = Integer.MAX_VALUE;
        Set<Umvc3Character> matchingCharacters = EnumSet.noneOf(Umvc3Character.class);

        for (Umvc3Character character : possibleCharacters) {
            int distance = StringUtils.getLevenshteinDistance(character.getName().toUpperCase(), text);
            if (distance < minimalDistance) {
                minimalDistance = distance;
                matchingCharacters.clear();
                matchingCharacters.add(character);
            } else if (distance == minimalDistance) {
                matchingCharacters.add(character);
            }
        }

        // matchingCharacters is not empty, since there must be at least one character with a distance less than
        // Integer.MAX_INT.
        Umvc3Character result;
        if (1 < matchingCharacters.size()) {
            // More than one match found.
            result = handleMultipleMatches(text, minimalDistance, matchingCharacters);
        } else {
            // Exactly one match, return it.
            result = matchingCharacters.iterator().next();
        }

        if (log.isDebugEnabled()) {
            log.debug(String.format("Match found: %s. levenshtein(%s, %s) = %s", result,
                    result.getName().toUpperCase(), text, "" + minimalDistance));
        }
        return result;
    }

    /**
     * Handles the situation where more than one match is found with the minimal Levenshtein distance.
     * 
     * @param text
     *            text to be matched
     * @param minimalDistance
     *            minimal distance found
     * @param matchingCharacters
     *            potentially matching characters, with minimal distance to text
     * @return best match out of matchingCharacters
     * @throws OCRException
     *             in case no best match can be determined
     */
    private Umvc3Character handleMultipleMatches(String text, int minimalDistance,
            Set<Umvc3Character> matchingCharacters) throws OCRException {
        Umvc3Character result;
        if (log.isDebugEnabled()) {
            log.debug("Potential matches: " + matchingCharacters);
        }
        List<OCRException> suppressedExceptions = new ArrayList<>();
        result = matchByReplacingLetters(text, matchingCharacters, suppressedExceptions);
        if (result == null) {
            // Nothing else we can do, throw an exception.
            OCRException e = new OCRException(
                    String.format(
                            "Unable to uniquely match character. Text to be matched: %s, characters with minimal Levenshtein distance %s: %s.",
                            text, "" + minimalDistance, matchingCharacters));
            for (OCRException suppressed : suppressedExceptions) {
                e.addSuppressed(suppressed);
            }
            throw e;
        } else if (log.isDebugEnabled()) {
            for (OCRException e : suppressedExceptions) {
                log.debug("Suppressed exception.", e);
            }
        }
        return result;
    }

    /**
     * Tesseract seems to have trouble reading a few letter combinations. For example, A is often read as II. This
     * method replaces one of these occurences of known difficult combinations and tries to match the resulting text to
     * a character name.
     * 
     * @param text
     *            original text, which did not uniquely match any character's name
     * @param possibleCharacters
     *            the characters that text may match
     * @param suppressedExceptions
     *            list of suppressed exceptions; if any OCRExceptions occur while executing this method, they are added
     *            to this list
     * @return matching character, or null if no match could be found
     */
    private Umvc3Character matchByReplacingLetters(String text, Set<Umvc3Character> possibleCharacters,
            List<OCRException> suppressedExceptions) {

        Set<Umvc3Character> matches = EnumSet.noneOf(Umvc3Character.class);

        matchByReplacingLetters(text, "II", "A", possibleCharacters, matches, suppressedExceptions);
        matchByReplacingLetters(text, "II", "N", possibleCharacters, matches, suppressedExceptions);
        matchByReplacingLetters(text, "II", "U", possibleCharacters, matches, suppressedExceptions);
        matchByReplacingLetters(text, "IM", "MA", possibleCharacters, matches, suppressedExceptions);
        matchByReplacingLetters(text, "W", "VI", possibleCharacters, matches, suppressedExceptions);
        matchByReplacingLetters(text, "o", "O", possibleCharacters, matches, suppressedExceptions);
        matchByReplacingLetters(text, "c", "C", possibleCharacters, matches, suppressedExceptions);
        matchByReplacingLetters(text, "s", "S", possibleCharacters, matches, suppressedExceptions);
        matchByReplacingLetters(text, "w", "W", possibleCharacters, matches, suppressedExceptions);
        matchByReplacingLetters(text, "v", "V", possibleCharacters, matches, suppressedExceptions);

        Umvc3Character result;
        if (matches.size() == 1) {
            result = matches.iterator().next();
        } else {
            result = null;
        }

        return result;
    }

    /**
     * This method replaces te first occurence of regexp with replacement and tries to match the resulting text to a
     * character name.
     * 
     * @param text
     *            original text, which did not uniquely match any character's name
     * @param regexp
     *            expression to be replaced
     * @param replacement
     *            replacement for regexp
     * @param possibleCharacters
     *            the characters that text may match
     * @param matches
     *            any resulting matches are added to this list
     * @param suppressedExceptions
     *            list of suppressed exceptions; if any OCRExceptions occur while executing this method, they are added
     *            to this list
     */
    private void matchByReplacingLetters(String text, String regexp, String replacement,
            Set<Umvc3Character> possibleCharacters, Set<Umvc3Character> matches, List<OCRException> suppressedExceptions) {
        String alternateText = text.replaceFirst(regexp, replacement);
        if (!text.equals(alternateText)) {
            try {
                matches.add(matchToCharacterName(alternateText, possibleCharacters));
            } catch (OCRException e) {
                suppressedExceptions.add(e);
            }
        }
    }
}
