package com.vatodev.mercaditouam.Utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

    /**
     * Guarda un archivo en el directorio especificado.
     * @param uploadDir Directorio de subida.
     * @param fileName Nombre del archivo.
     * @param multipartFile Archivo recibido desde el cliente.
     * @throws IOException Si ocurre un error al guardar el archivo.
     */
    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("No se pudo guardar el archivo de imagen: " + fileName, ioe);
        }
    }
}
