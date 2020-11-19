package framework.spring.spring01;

import lombok.Getter;

import java.util.List;


/**
 * @org.apache.xbean.XBean element="core" rootElement="true"
 * @org.apache.xbean.Defaults {code:xml}
 */
public class Klass {

    @Getter
    private List<Student> students;

    /**
     * @org.apache.xbean.Property alias="students" nestedType="framework.spring.spring01.Student"
     */
    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public void dong(){
        System.out.println(this.getStudents());
    }
}
