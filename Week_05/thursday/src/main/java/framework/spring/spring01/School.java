package framework.spring.spring01;

import framework.spring.aop.ISchool;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Data
@Component
public class School implements ISchool {
    
    @Autowired
    private Klass klass;
    
    @Resource(name = "student123")
    Student student123;
    
    @Override
    public void ding(){
        System.out.println("Class1 have " + this.klass.getStudents().size() + " students and one is " + this.student123);
    }
}
