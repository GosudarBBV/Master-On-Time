package com.master.on.time.master.on.time.service;

import com.master.on.time.master.on.time.exception.EntityNotFoundException;
import com.master.on.time.master.on.time.exception.FileStorageException;
import com.master.on.time.master.on.time.exception.InvalidImageFormatException;
import com.master.on.time.master.on.time.model.PortfolioImage;
import com.master.on.time.master.on.time.model.User;
import com.master.on.time.master.on.time.repository.PortfolioImageRepository;
import com.master.on.time.master.on.time.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class PortfolioServiceImpl implements PortfolioService {

    private static final String UPLOAD_DIR = "uploads/portfolio/";

    private final PortfolioImageRepository portfolioImageRepository;
    private final UserRepository userRepository;

    @Override
    public String savePortfolioImage(Long userId, MultipartFile file) {
        User user = findUserById(userId);
        validateImageFile(file);

        String fileName = generateFileName(file);
        Path filePath = storeFile(file, fileName);

        PortfolioImage portfolioImage = new PortfolioImage();
        portfolioImage.setUser(user);
        portfolioImage.setImageUrl("/" + UPLOAD_DIR + fileName);

        portfolioImageRepository.save(portfolioImage);
        return portfolioImage.getImageUrl();
    }

    @Override
    public List<String> getPortfolioImagesUrls(Long userId) {
        return portfolioImageRepository.findByUserId(userId).stream()
                .map(PortfolioImage::getImageUrl)
                .toList();
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(()
                        -> new EntityNotFoundException("User not found with id: "
                        + userId));
    }

    private void validateImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.equalsIgnoreCase("image/jpeg")
                || contentType.equalsIgnoreCase("image/png"))) {
            throw new InvalidImageFormatException("Only JPG and PNG images are allowed");
        }
    }

    private String generateFileName(MultipartFile file) {
        String extension = file.getContentType().equalsIgnoreCase("image/png")
                ? ".png" : ".jpg";
        return UUID.randomUUID().toString() + extension;
    }

    private Path storeFile(MultipartFile file, String fileName) {
        try {
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            return filePath;
        } catch (IOException e) {
            throw new FileStorageException("Failed to store file: " + fileName, e);
        }
    }
}
