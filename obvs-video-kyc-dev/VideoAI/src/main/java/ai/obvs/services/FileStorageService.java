package ai.obvs.services;

import ai.obvs.exceptions.FileStorageException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
@EnableConfigurationProperties
public class FileStorageService {

    @Value("file.dir")
    private String fileStorageDirectory;

    private final Path fileStorageLocation;

    public FileStorageService() {
        fileStorageDirectory = "/home/ubuntu/workspace/backend";
        this.fileStorageLocation = Paths.get(fileStorageDirectory)
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(String dirName, String fileName, byte[] imageBytes) {
        try {
            Path targetLocation = verifyAndCreatePath(dirName, fileName);
            new FileOutputStream(targetLocation.toString()).write(imageBytes);

            return targetLocation.toString();
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    private Path verifyAndCreatePath(String dirName, String fileName) throws IOException {
        if (fileName.contains("..")) {
            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
        }
        Path targetDir = this.fileStorageLocation.resolve(dirName);
        if (!Files.exists(targetDir))
            Files.createDirectories(targetDir);
        Path targetLocation = targetDir.resolve(fileName);
        Files.deleteIfExists(targetLocation);
        return targetLocation;
    }

    public String storeImage(String dirName, String fileName, byte[] imageBytes) {
        try {
            Path targetLocation = verifyAndCreatePath(dirName, fileName);
            if (imageBytes != null) {
                ByteArrayInputStream bis = new ByteArrayInputStream(imageBytes);
                BufferedImage bufferedImage = ImageIO.read(bis);
                ImageIO.write(bufferedImage, "png", targetLocation.toFile());
                return targetLocation.toString();
            }
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!");
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

}
