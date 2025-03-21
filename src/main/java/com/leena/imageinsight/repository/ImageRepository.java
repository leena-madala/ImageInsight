package com.leena.imageinsight.repository;

import com.leena.imageinsight.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("SELECT i FROM Image i JOIN i.objectsInImage obj WHERE obj IN :objects")
    List<Image> findImagesContainingObjects(List<String> objects);
}
