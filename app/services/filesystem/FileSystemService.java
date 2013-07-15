package services.filesystem;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileSystemService {
    
    /**
     * 
     * @param imageBytes bytes of the image
     * @return the path to the image
     */
    String saveImage(byte[] imageBytes, String fileExtensionWithoutDot) throws IOException;
    
    /**
     * 
     * @param path the path to the given image
     * @return Do not forget to close the {@link InputStream}
     */
    InputStream getImage(String path) throws FileNotFoundException;
}