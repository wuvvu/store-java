package com.example.store.service;

import com.example.store.mapper.ResourceMapper;
import com.example.store.model.Carousel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    private final ResourceMapper resourceMapper;

    public ResourceService(ResourceMapper resourceMapper) {
        this.resourceMapper = resourceMapper;
    }

    /**
     * 列出轮播图相关信息
     * @return 轮播图信息json
     */
    public String listCarousel() {
        List<Carousel> carouselList = resourceMapper.listCarousels();
        JsonObject responseJson = new JsonObject();
        responseJson.add("code", new JsonPrimitive("001"));
        responseJson.add("carousel", new Gson().toJsonTree(carouselList).getAsJsonArray());
        return responseJson.toString();
    }


}
