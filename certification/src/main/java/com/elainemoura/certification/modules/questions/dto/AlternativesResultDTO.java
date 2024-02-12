package com.elainemoura.certification.modules.questions.dto;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlternativesResultDTO {

    private UUID id;
    private String description;
       

}
