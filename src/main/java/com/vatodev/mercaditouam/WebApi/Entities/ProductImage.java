package com.vatodev.mercaditouam.WebApi.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_ProductImage")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @Lob
    @Column(nullable = false)
    private byte[] imageData;

    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private Product product;

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
