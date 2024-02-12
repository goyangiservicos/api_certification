package com.elainemoura.certification.modules.students.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elainemoura.certification.modules.students.dto.StudentCertificationAnswerDTO;
import com.elainemoura.certification.modules.students.dto.VerifyHasCertificationDTO;
import com.elainemoura.certification.modules.students.entities.CertificationStudentEntity;
import com.elainemoura.certification.modules.students.useCases.StudentCertificationAnswersUseCase;
import com.elainemoura.certification.modules.students.useCases.VerifyIfHasCertificationUseCase;


@RestController
@RequestMapping("/students")
public class StudentController {

@Autowired
private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

@Autowired  
private StudentCertificationAnswersUseCase studentCertificationAnswersUseCase;

  @PostMapping("/verifyIsHasCertification")
  public String verifyIsHasCertification(@RequestBody VerifyHasCertificationDTO verifyHasCertificationDTO){

    var result = this.verifyIfHasCertificationUseCase.execute(verifyHasCertificationDTO);
    if(result){
        return "Usuario já fez a prova";
    }
       return "Usuario pode fazer prova";
  }  
  
  @PostMapping("/certification/answer")
  public ResponseEntity<Object> certificationAnswer (
    @RequestBody StudentCertificationAnswerDTO studentCertificationAnswerDTO)  { 
 // return studentCertificationAnswersUseCase.execute(studentCertificationAnswerDTO);
    try{
      var result = studentCertificationAnswersUseCase.execute(studentCertificationAnswerDTO);
      return ResponseEntity.ok().body(result);
    }catch(Exception e){
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }
}
