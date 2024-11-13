package com.vatodev.mercaditouam.WebApi.Entities;

import com.vatodev.mercaditouam.Core.Entities.User;
import jakarta.persistence.*;

@Entity
@Table(name = "tbl_Profile")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @OneToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @Column(nullable = false, unique = true, length = 14)
    private String cif;

    @Column(nullable = false, length = 12)
    private String phoneNumber;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length = 350)
    private String bio;

    @Lob
    private byte[] profileImage;

    public Long getProfileId() {
        return profileId;
    }

    public void setProfileId(Long profileId) {
        this.profileId = profileId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public byte[] getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(byte[] profileImage) {
        this.profileImage = profileImage;
    }
}
