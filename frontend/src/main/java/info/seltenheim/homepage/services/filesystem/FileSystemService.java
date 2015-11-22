package info.seltenheim.homepage.services.filesystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.inject.ImplementedBy;

@ImplementedBy(LocalFileSystemService.class)
public interface FileSystemService {
    
    /**
     * 
     * @param imageBytes bytes of the image
     * @return the path to the image
     */
    String saveImage(byte[] imageBytes, String fileExtensionWithoutDot) throws IOException;

    File getImageAsFile(String path) throws FileNotFoundException;
}
