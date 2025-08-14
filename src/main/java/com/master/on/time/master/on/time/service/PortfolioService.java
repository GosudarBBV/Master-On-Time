package com.master.on.time.master.on.time.service;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface PortfolioService {
    String savePortfolioImage(Long userId, MultipartFile file);

    List<String> getPortfolioImagesUrls(Long userId);
}
