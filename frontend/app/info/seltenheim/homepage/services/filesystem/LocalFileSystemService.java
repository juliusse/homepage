package info.seltenheim.homepage.services.filesystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import play.Play;

/**
 * 
 * @author Julius This implementation saves all files into the folder that is
 *         specified in services.filesystem.location
 */
@Profile("localFS")
@Component
public class LocalFileSystemService implements FileSystemService {

    private final File basePath;
    private static final Random random = new Random();

    public LocalFileSystemService() throws IOException {
        final String basePathFromConf = Play.application().configuration().getString("filesystem.basepath");
        basePath = new File(basePathFromConf != null ? basePathFromConf : "uploaded");
        if (!basePath.exists()) {
            FileUtils.forceMkdir(basePath);
        }

    }

    @Override
    public String saveImage(byte[] imageBytes, String fileExtensionWithoutDot) throws IOException {
        final String firstPathPart = (random.nextInt(899999999) + 1000000) + "";
        final String secondPathPart = (random.nextInt(899999999) + 1000000) + "";
        final String fileName = random.nextInt(1000) + "." + fileExtensionWithoutDot;
        final String resource = firstPathPart + File.separator + secondPathPart + File.separator + fileName;
        final File file = new File(basePath, resource);
        FileUtils.writeByteArrayToFile(file, imageBytes);
        return resource;
    }
    
    @Override
    public File getImageAsFile(String path) throws FileNotFoundException {
        final File file = new File(basePath, path);
        if (!file.exists()) {
            throw new FileNotFoundException("File '"+file.getAbsolutePath()+"' not found!");
        }

        return file;
    }

}
