package com.master.on.time.master.on.time.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request DTO for specialist decision with optional notes")
public record SpecialistDecisionRequestDto(

        @Schema(description = "Reason or comment for the decision",
                example = "Approved based on qualifications")
        String notes

) {
}
