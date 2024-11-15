package com.vatodev.mercaditouam.WebApi.Services;

import com.vatodev.mercaditouam.Core.Security.JWT.JwtUtil;
import com.vatodev.mercaditouam.Utils.ImageUtils;
import com.vatodev.mercaditouam.WebApi.Dtos.ProductRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

@Service
public class ProductService {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Crea un nuevo producto y le asigna imágenes.
     *
     * @param productRequest Detalles del producto y las imágenes asociadas.
     * @return El ID del nuevo producto creado.
     */
    public Long createProduct(ProductRequest productRequest) {
        Long productId = null;

        // Extraer el token JWT y verificar que no sea null o vacío
        String token = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token no proporcionado en el contexto de seguridad.");
        }

        // Extraer el userId del token
        Long userId;
        try {
            userId = jwtUtil.extractUserId(token);
        } catch (Exception e) {
            throw new RuntimeException("Error al extraer el userId del token: " + e.getMessage(), e);
        }

        // Paso 1: Crear el producto llamando al procedimiento almacenado
        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL insertar_producto(?, ?, ?, ?, ?)}")) {

            statement.setLong(1, userId); // Pasamos el userId extraído del token
            statement.setString(2, productRequest.getTitle());
            statement.setString(3, productRequest.getDescription());
            statement.setBigDecimal(4, productRequest.getPrice());
            statement.setTimestamp(5, new Timestamp(new Date().getTime()));

            // Ejecutar y obtener el productId generado
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                productId = rs.getLong("NewProductId");
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al crear el producto: " + ex.getMessage(), ex);
        }

        // Paso 2: Agregar imágenes al producto si se ha generado un productId
        if (productId != null && productRequest.getImages() != null) {
            for (MultipartFile imageFile : productRequest.getImages()) {
                try {
                    byte[] compressedImage = ImageUtils.compressImage(imageFile.getBytes());
                    addProductImage(productId, compressedImage);
                } catch (IOException e) {
                    throw new RuntimeException("Error al procesar la imagen: " + e.getMessage(), e);
                }
            }
        }

        return productId;
    }

    /**
     * Agrega una imagen a un producto específico.
     *
     * @param productId El ID del producto.
     * @param imageData Datos de la imagen en formato binario.
     */
    private void addProductImage(Long productId, byte[] imageData) {
        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL insertar_imagen_producto(?, ?)}")) {

            statement.setLong(1, productId);
            statement.setBytes(2, imageData);
            statement.execute();
        } catch (SQLException ex) {
            throw new RuntimeException("Error al agregar imagen al producto: " + ex.getMessage(), ex);
        }
    }
}
