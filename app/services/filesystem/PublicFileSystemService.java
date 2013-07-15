package services.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import org.apache.commons.io.FileUtils;

import play.Play;

/**
 * 
 * @author Julius
 * This implementation saves all files into the public-folder <br>
 * which makes them uncontrollably downloadable
 */
public class PublicFileSystemService implements FileSystemService {
    
    private final File basePath;
    private static final Random random = new Random();

    public PublicFileSystemService() {
        final String basePathFromConf = Play.application().configuration().getString("filesystem.basepath");
        final File playBase = Play.application().path();
        basePath = new File(playBase,  basePathFromConf != null ? basePathFromConf : "uploaded");
        basePath.mkdirs();
    }
    
    @Override
    public String saveImage(byte[] imageBytes, String fileExtensionWithoutDot) throws IOException {
        final String firstPathPart = (random.nextInt(899999999) + 1000000)+"";
        final String secondPathPart = (random.nextInt(899999999) + 1000000)+"";
        final String fileName = random.nextInt(1000)+"."+fileExtensionWithoutDot;
        final String resource = firstPathPart + File.separator + secondPathPart + File.separator + fileName;
        final File file = new File(basePath, resource);
        FileUtils.writeByteArrayToFile(file, imageBytes);
        return resource;
    }

    @Override
    public InputStream getImage(String path) throws FileNotFoundException {
        final File file = new File(basePath,path);
        if(!file.exists()) {
            throw new FileNotFoundException();
        }
        
        return new FileInputStream(file);
    }



}
