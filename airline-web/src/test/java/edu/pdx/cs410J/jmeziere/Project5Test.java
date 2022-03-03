package edu.pdx.cs410J.jmeziere;

import org.junit.jupiter.api.Test;

import java.io.*;

import static edu.pdx.cs410J.jmeziere.Project5.displayReadme;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class Project5Test {

  @Test
  void readmeCanBeReadAsResource() throws IOException {
    ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outputStreamCaptor));
    displayReadme();

    assertThat(outputStreamCaptor.toString(), containsString("This is a README file!"));
  }
}
