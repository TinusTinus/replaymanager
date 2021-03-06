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
package nl.mvdr.umvc3replayanalyser.controller;

import java.io.File;
import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import org.apache.commons.io.FilenameUtils;

/**
 * Utility class for file manipulation. Based on Milan Aleksic's implementation as available on Stack Overflow:
 * http://stackoverflow.com/a/3054692.
 * 
 * @author Martijn van de Rijdt
 */
// Private constructor to prevent utility class instantiation.
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtils {
    /** Path separator. */
    static final String SEPARATOR = System.getProperty("file.separator");

    /**
     * Get the relative path from one file to another, specifying the directory separator. If one of the provided
     * resources does not exist, it is assumed to be a file unless it ends with '/' or '\'.
     * 
     * @param targetPath
     *            targetPath is calculated to this file
     * @param basePath
     *            basePath is calculated from this file
     * @return relative path
     * @throws PathResolutionException
     *             in case the paths are not related at all
     */
    static String getRelativePath(String targetPath, String basePath) throws PathResolutionException {
        return getRelativePath(targetPath, basePath, SEPARATOR);
    }

    /**
     * Get the relative path from one file to another, specifying the directory separator. If one of the provided
     * resources does not exist, it is assumed to be a file unless it ends with '/' or '\'.
     * 
     * @param targetPath
     *            targetPath is calculated to this file
     * @param basePath
     *            basePath is calculated from this file
     * @param pathSeparator
     *            directory separator; the platform default is not assumed so that we can test Unix behaviour when
     *            running on Windows (for example)
     * @return relative path
     * @throws PathResolutionException
     *             in case the paths are not related at all
     */
    static String getRelativePath(String targetPath, String basePath, String pathSeparator)
            throws PathResolutionException {

        // Normalize the paths
        String normalizedTargetPath = FilenameUtils.normalizeNoEndSeparator(targetPath);
        String normalizedBasePath = FilenameUtils.normalizeNoEndSeparator(basePath);

        // Undo the changes to the separators made by normalization
        if (pathSeparator.equals("/")) {
            normalizedTargetPath = FilenameUtils.separatorsToUnix(normalizedTargetPath);
            normalizedBasePath = FilenameUtils.separatorsToUnix(normalizedBasePath);
        } else if (pathSeparator.equals("\\")) {
            normalizedTargetPath = FilenameUtils.separatorsToWindows(normalizedTargetPath);
            normalizedBasePath = FilenameUtils.separatorsToWindows(normalizedBasePath);
        } else {
            throw new IllegalArgumentException("Unrecognised dir separator '" + pathSeparator + "'");
        }

        String[] base = normalizedBasePath.split(Pattern.quote(pathSeparator));
        String[] target = normalizedTargetPath.split(Pattern.quote(pathSeparator));

        // First get all the common elements. Store them as a string,
        // and also count how many of them there are.
        StringBuffer common = new StringBuffer();

        int commonIndex = 0;
        while (commonIndex < target.length && commonIndex < base.length
                && target[commonIndex].equals(base[commonIndex])) {
            common.append(target[commonIndex] + pathSeparator);
            commonIndex++;
        }

        if (commonIndex == 0) {
            // No single common path element. This most
            // likely indicates differing drive letters, like C: and D:.
            // These paths cannot be relativized.
            throw new PathResolutionException("No common path element found for '" + normalizedTargetPath + "' and '"
                    + normalizedBasePath + "'");
        }

        // The number of directories we have to backtrack depends on whether the base is a file or a dir
        // For example, the relative path from
        //
        // /foo/bar/baz/gg/ff to /foo/bar/baz
        //
        // ".." if ff is a file
        // "../.." if ff is a directory
        //
        // The following is a heuristic to figure out if the base refers to a file or dir. It's not perfect, because
        // the resource referred to by this path may not actually exist, but it's the best I can do
        boolean baseIsFile = true;

        File baseResource = new File(normalizedBasePath);

        if (baseResource.exists()) {
            baseIsFile = baseResource.isFile();
        } else if (basePath.endsWith(pathSeparator)) {
            baseIsFile = false;
        }

        StringBuffer relative = new StringBuffer();

        if (base.length != commonIndex) {
            int numDirsUp = baseIsFile ? base.length - commonIndex - 1 : base.length - commonIndex;

            for (int i = 0; i < numDirsUp; i++) {
                relative.append(".." + pathSeparator);
            }
        }
        relative.append(normalizedTargetPath.substring(common.length()));
        return relative.toString();
    }
}