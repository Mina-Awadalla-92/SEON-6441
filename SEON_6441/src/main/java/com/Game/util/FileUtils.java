package com.Game.util;

import java.io.File;
import java.io.IOException;

/**
 * Utility methods for file operations.
 */
public class FileUtils {
    /**
     * Checks if a file exists and is readable.
     * 
     * @param p_filePath The path to the file.
     * @return True if the file exists and is readable, false otherwise.
     */
    public static boolean isFileReadable(String p_filePath) {
        File l_file = new File(p_filePath);
        return l_file.exists() && l_file.canRead();
    }
    
}