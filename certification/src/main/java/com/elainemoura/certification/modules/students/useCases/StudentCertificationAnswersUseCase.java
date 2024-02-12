package com.elainemoura.certification.modules.students.useCases;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.elainemoura.certification.modules.questions.entities.QuestionEntity;
import com.elainemoura.certification.modules.questions.entities.repository.QuestionRepository;
import com.elainemoura.certification.modules.students.dto.StudentCertificationAnswerDTO;
import com.elainemoura.certification.modules.students.dto.VerifyHasCertificationDTO;
import com.elainemoura.certification.modules.students.entities.AnswersCertificationsEntity;
import com.elainemoura.certification.modules.students.entities.CertificationStudentEntity;
import com.elainemoura.certification.modules.students.entities.StudentEntity;
import com.elainemoura.certification.modules.students.repositories.CertificationStudentRepository;
import com.elainemoura.certification.modules.students.repositories.StudentRepository;

@Service
public class StudentCertificationAnswersUseCase {
    
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private CertificationStudentRepository certificationStudentRepository;
    @Autowired
    private VerifyIfHasCertificationUseCase verifyIfHasCertificationUseCase;

    public CertificationStudentEntity execute(StudentCertificationAnswerDTO dto) throws Exception {

    var hasCertification = this.verifyIfHasCertificationUseCase.execute(new VerifyHasCertificationDTO(dto.getEmail(),dto.getTechnology()));
        if (hasCertification) {
            throw new Exception("Voce já tirou sua certificação");
        }
//buscar as respostas 

//corretas e incorretas
List <QuestionEntity> questionEntity = questionRepository.findByTechnology(dto.getTechnology());
List<AnswersCertificationsEntity> answersCertifications = new ArrayList<>();

AtomicInteger correctAnswers = new AtomicInteger(0);
dto.getQuestionAnswers()
.stream().forEach(questionAnswer -> {
   var question = questionEntity.stream()
   .filter(q -> q.getId().equals(questionAnswer.getQuestionID()));
  

   var findCorrectAlternative =  question.findFirst().get().getAlternatives().stream()
   .filter(alternative -> alternative.isCorrect()).findFirst().get();

    if(findCorrectAlternative.getId().equals(questionAnswer.getAlternativeID())){
        questionAnswer.setCorrect(true);
        correctAnswers.incrementAndGet();
    }else {
        questionAnswer.setCorrect(false);
    }
    var answersCertificationsEntity= AnswersCertificationsEntity.builder()
    .aswerID(questionAnswer.getAlternativeID())
    .questionID(questionAnswer.getAlternativeID())
    .isCorrect(questionAnswer.isCorrect()).build();

    answersCertifications.add(answersCertificationsEntity);
});

//verificar se existe estudante 
var student = studentRepository.findByEmail(dto.getEmail());
UUID studentID;
if (student.isEmpty()){
   var studentCreated = StudentEntity.builder().email(dto.getEmail()).build();

   studentCreated = studentRepository.save(studentCreated);
   studentID = studentCreated.getId();
}else {
    studentID = student.get().getId();
    
}


CertificationStudentEntity certificationStudentEntity = CertificationStudentEntity.builder()
.technology(dto.getTechnology())
.studentID(studentID)
.grate(correctAnswers.get())
//.answersCertificationsEntity(answersCertifications)
.build();
var certificationStudentCreated = certificationStudentRepository.save(certificationStudentEntity);

answersCertifications.stream().forEach(answersCertification -> {
    answersCertification.setCertificationID(certificationStudentEntity.getId());
    answersCertification.setCertificationStudentEntity(certificationStudentEntity);
});

certificationStudentEntity.setAnswersCertificationsEntity(answersCertifications);
certificationStudentRepository.save(certificationStudentEntity);
return certificationStudentCreated;
//salvar




    }
}
