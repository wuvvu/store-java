package com.example.store.controller;

import com.example.store.service.ResourceService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

    private final ResourceService resourceService;

    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @RequestMapping("/resources/carousel")
    public String listCarousel() {
        return resourceService.listCarousel();
    }
}
