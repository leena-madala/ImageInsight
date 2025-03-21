package com.leena.imageinsight.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private String label;

    @ElementCollection
    @CollectionTable(name = "image_objects", joinColumns = @JoinColumn(name = "image_id"))
    @Column(name = "object_name")
    private List<String> objectsInImage;

    // Constructor with url, label, and list of ImageObjects
    public Image(String url, String label, List<String> objectsInImage) {
        this.url = url;
        this.label = label;
        this.objectsInImage = objectsInImage;
    }
}