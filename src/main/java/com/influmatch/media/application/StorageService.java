package com.influmatch.media.application;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Base64;

@Service
public class StorageService {

    /**
     * Procesa el archivo y retorna su nombre
     */
    public String store(MultipartFile file) throws IOException {
        // Solo validamos y retornamos el nombre, el contenido se guarda en la entidad
        return file.getOriginalFilename();
    }

    /**
     * Obtiene la URL del contenido como Data URL (base64)
     */
    public String getUrl(String filename, byte[] content, String contentType) {
        String base64 = Base64.getEncoder().encodeToString(content);
        return "data:" + contentType + ";base64," + base64;
    }

    /**
     * No necesitamos eliminar nada ya que el contenido está en la BD
     */
    public void delete(String filename) {
        // No hace nada, la eliminación se maneja en la BD
    }

    /**
     * Extrae metadatos de un archivo multimedia
     */
    public MediaMetadata extractMetadata(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        
        if (contentType != null && contentType.startsWith("image/")) {
            BufferedImage img = ImageIO.read(file.getInputStream());
            return new MediaMetadata(img.getWidth(), img.getHeight(), null);
        }
        
        // Para videos necesitaríamos una librería como JavaCV o similar
        // Por ahora retornamos valores por defecto
        if (contentType != null && contentType.startsWith("video/")) {
            return new MediaMetadata(1920, 1080, 0);
        }
        
        return new MediaMetadata(null, null, null);
    }

    /**
     * Record para metadatos multimedia
     */
    public record MediaMetadata(
        Integer width,
        Integer height,
        Integer duration  // en segundos, solo para videos
    ) {}
} 