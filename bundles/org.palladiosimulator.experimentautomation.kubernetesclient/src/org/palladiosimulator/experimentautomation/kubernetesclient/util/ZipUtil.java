package org.palladiosimulator.experimentautomation.kubernetesclient.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class ZipUtil {


  /**
   * Zip recursively all directories and files starting from the given directory</br>
   * Destination will be the same path
   * 
   * @param directoryPath which has to be zipped
   * @return path to zip file
   */
  public String createZipFileRecursively(String directoryPath, String pathToZipFile) {
    try (FileOutputStream fos = new FileOutputStream(pathToZipFile);
        ZipOutputStream zos = new ZipOutputStream(fos)) {

      Path sourcePath = Paths.get(directoryPath);
      // using WalkFileTree to traverse directory
      Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
        @Override
        public FileVisitResult preVisitDirectory(final Path dir, final BasicFileAttributes attrs)
            throws IOException {
          // it starts with the source folder so skipping that
          if (!sourcePath.equals(dir)) {
            zos.putNextEntry(new ZipEntry(sourcePath.relativize(dir).toString() + "/"));

            zos.closeEntry();
          }
          return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs)
            throws IOException {
          zos.putNextEntry(new ZipEntry(sourcePath.relativize(file).toString()));
          Files.copy(file, zos);
          zos.closeEntry();
          return FileVisitResult.CONTINUE;
        }

      });


      return pathToZipFile;
    } catch (IOException e) {
      return null;
    }
  }
}
