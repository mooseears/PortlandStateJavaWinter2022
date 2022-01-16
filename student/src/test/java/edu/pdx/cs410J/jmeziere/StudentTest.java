package edu.pdx.cs410J.jmeziere;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit tests for the Student class.  In addition to the JUnit annotations,
 * they also make use of the <a href="http://hamcrest.org/JavaHamcrest/">hamcrest</a>
 * matchers for more readable assertion statements.
 */
public class StudentTest
{

  @Test
  void studentNamedPatIsNamedPat() {
    String name = "Pat";
    var pat = new Student(name, new ArrayList<>(), 0.0, "Doesn't matter");
    assertThat(pat.getName(), equalTo(name));
  }

  @Test
  void femaleGenderHasShePronouns() {
    Student femaleStudent = createStudentWithGender(Student.FEMALE_GENDER);
    assertThat(femaleStudent.toString(), containsString(Student.FEMALE_PRONOUN));
  }

  private Student createStudentWithGender(String gender) {
    return new Student("Name", new ArrayList(), 0.0, gender);
  }

  @Test
  void maleGenderHasHePronouns() {
    Student maleStudent = createStudentWithGender(Student.MALE_GENDER);
    assertThat(maleStudent.toString(), containsString(Student.MALE_PRONOUN));
  }

  @Test
  void otherGenderHasTheyPronouns() {
    Student otherStudent = createStudentWithGender(Student.OTHER_GENDER);
    assertThat(otherStudent.toString(), containsString(Student.OTHER_PRONOUN));
  }
}