package org.palladiosimulator.experimentautomation.kubernetesclient.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;

/**
 * Utility class for standard files
 * 
 * @author Niko Benkler
 *
 */
public class FileUtil {


  /**
   * Create directory recursively. That is: Create directories and sub-directories if specified in
   * path.
   * 
   * @param pathToDirectory
   * @return
   */
  public static boolean createDirectoryRecursively(String pathToDirectory) {
    try {
      Files.createDirectories(Paths.get(pathToDirectory));
    } catch (IOException e) {
      return false;
    }

    return true;
  }


  /**
   * Delete Directory with entire content
   * 
   * @param path
   */
  public static void deleteDirectory(String pathToSimulation) {
    try {
      FileUtils.deleteDirectory(new File(pathToSimulation));
    } catch (IOException e) {
    }
  }

  /**
   * Load file into byte Array
   * 
   * @param path of the file
   * @return
   */
  public static byte[] loadFileAsByteStream(String path) {

    ByteArrayOutputStream stream = loadFile(path);

    if (stream == null) {
      return null;
    }

    return stream.toByteArray();

  }

  /**
   * Load file into String
   * 
   * @param path of the file
   * @return null in case of error
   */
  public static String loadFileAsString(String path) {

    ByteArrayOutputStream stream = loadFile(path);

    if (stream == null) {
      return null;
    }

    return stream.toString();
  }

  /*
   * Actual load file method.
   */
  private static ByteArrayOutputStream loadFile(String path) {

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    File file = new File(path);

    try (FileInputStream fis = new FileInputStream(file);) {

      org.apache.commons.io.IOUtils.copy(fis, byteArrayOutputStream);
    } catch (IOException e) {

      return null;
    }

    return byteArrayOutputStream;

  }

  /**
   * Creates (or replace) file at specified path with given content.
   * 
   * @param path
   * @param content
   */
  public static boolean createFileFromString(String path, String content) {

    File file = new File(path);
    try (PrintWriter out = new PrintWriter(file);) {

      file.createNewFile();
      out.println(content);
      return true;
    } catch (IOException e) {

      return false;

    }

  }

}
