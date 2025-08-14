package com.master.on.time.master.on.time.mapper;

import com.master.on.time.master.on.time.config.MapperConfig;
import com.master.on.time.master.on.time.dto.ReviewModerationDto;
import com.master.on.time.master.on.time.dto.ReviewModerationResponseDto;
import com.master.on.time.master.on.time.dto.ReviewResponseDto;
import com.master.on.time.master.on.time.model.Review;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface ReviewMapper {

    ReviewResponseDto toDto(Review review);

    default ReviewModerationResponseDto toModerationDto(Review review) {
        return new ReviewModerationResponseDto(
                review.getId(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.getClient().getFirstName() + " " + review.getClient().getLastName(),
                review.getStatus()
        );
    }

    ReviewModerationDto toReviewModerationDto(Review review);
}
