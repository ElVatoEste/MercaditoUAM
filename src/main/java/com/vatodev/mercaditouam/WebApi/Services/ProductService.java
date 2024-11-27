package com.vatodev.mercaditouam.WebApi.Services;

import com.vatodev.mercaditouam.Core.Security.JWT.JwtUtil;
import com.vatodev.mercaditouam.Utils.ImageUtils;
import com.vatodev.mercaditouam.WebApi.Dtos.ProductListDto;
import com.vatodev.mercaditouam.WebApi.Dtos.ProductRequest;
import com.vatodev.mercaditouam.WebApi.Dtos.ProductWithOwnerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JwtUtil jwtUtil;

    public List<ProductListDto> getPaginatedProducts(int page, int pageSize) {
        List<ProductListDto> products = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL obtener_productos_paginados(?, ?)}")) {

            statement.setInt(1, page);
            statement.setInt(2, pageSize);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ProductListDto product = new ProductListDto();
                    product.setProductId(resultSet.getLong("productId"));
                    product.setTitle(resultSet.getString("title"));
                    product.setDescription(resultSet.getString("description"));
                    product.setPrice(resultSet.getDouble("price"));
                    product.setImageData(resultSet.getBytes("imageData")); // Puede ser null si no hay imagen
                    products.add(product);
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al obtener los productos paginados: " + ex.getMessage(), ex);
        }

        return products;
    }

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
            if (userId == null) {
                throw new RuntimeException("ID de usuario no encontrado en el token.");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al extraer el userId del token: " + e.getMessage(), e);
        }

        // Paso 1: Crear el producto llamando al procedimiento almacenado
        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL insertar_producto(?, ?, ?, ?, ?)}")) {

            statement.setLong(1, userId);
            statement.setString(2, productRequest.getTitle());
            statement.setString(3, productRequest.getDescription());
            statement.setBigDecimal(4, productRequest.getPrice());
            statement.setTimestamp(5, new Timestamp(new Date().getTime()));

            // Ejecutar y obtener el productId generado
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    productId = rs.getLong("NewProductId");
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al crear el producto: " + ex.getMessage(), ex);
        }

        // Paso 2: Agregar imágenes al producto si se ha generado un productId
        if (productId != null && productRequest.getImages() != null) {
            for (byte[] imageData : productRequest.getImages()) {
                byte[] compressedImage = ImageUtils.compressImage(imageData);
                addProductImage(productId, compressedImage);
            }
        }
        return productId;
    }

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

    public ProductWithOwnerDto getProductWithOwnerInfo(Long productId) {
        ProductWithOwnerDto productWithOwner = null;

        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall("{CALL Get_productWithOwnerInfo(?)}")) {

            // Establecer el parámetro de entrada
            statement.setLong(1, productId);

            // Ejecutar el procedimiento almacenado y procesar los resultados
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    productWithOwner = new ProductWithOwnerDto();
                    productWithOwner.setProductId(resultSet.getInt("productId"));
                    productWithOwner.setTitle(resultSet.getString("title"));
                    productWithOwner.setDescription(resultSet.getString("description"));
                    productWithOwner.setPrice(resultSet.getDouble("price"));
                    productWithOwner.setActive(resultSet.getBoolean("isActive"));
                    productWithOwner.setFeatured(resultSet.getBoolean("isFeatured"));
                    productWithOwner.setCreatedAt(resultSet.getString("createdAt"));
                    productWithOwner.setOwnerUsername(resultSet.getString("OwnerUsername"));
                    productWithOwner.setOwnerEmail(resultSet.getString("OwnerEmail"));
                    productWithOwner.setOwnerPhoneNumber(resultSet.getInt("OwnerPhoneNumber"));
                }
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error al obtener la información del producto: " + ex.getMessage(), ex);
        }

        return productWithOwner;
    }

}
