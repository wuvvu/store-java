package com.example.store.mapper;

import com.example.store.model.Carousel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceMapper {

    List<Carousel> listCarousels();


}
