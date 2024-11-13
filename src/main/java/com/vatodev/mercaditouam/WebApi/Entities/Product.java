package com.vatodev.mercaditouam.WebApi.Entities;

import com.vatodev.mercaditouam.Core.Entities.User;
import jakarta.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "tbl_Product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 350)
    private String description;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false)
    private Date createdAt = new Date();

    @Column(nullable = false)
    private Boolean isFeatured = false;  // Nuevo campo para indicar si el producto es destacado

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProductImage> images;

    // Getters y Setters

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<ProductImage> getImages() {
        return images;
    }

    public void setImages(Set<ProductImage> images) {
        this.images = images;
    }
}
