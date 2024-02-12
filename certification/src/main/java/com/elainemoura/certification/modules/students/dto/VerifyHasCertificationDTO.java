package com.elainemoura.certification.modules.students.dto;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data 
@AllArgsConstructor
@NoArgsConstructor 
public class VerifyHasCertificationDTO {
    private String email;
    private String Technology;

}
