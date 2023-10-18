package com.student;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.student.controller.StudentController;
import com.student.model.Student;
import com.student.repo.StudentRepo;

@WebMvcTest(StudentController.class)
public class StudentTest {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;
	@MockBean
	StudentRepo repo;
	
	@Test
	public void Test1() throws Exception {
		int studentId =1;		
		Student student = new Student(studentId, "Udaya Kumar", LocalDate.of(1990, 02, 04));
		when(repo.findById(studentId)).thenReturn(Optional.of(student));
		MvcResult result = mockMvc.perform(get("/student/findone/{id}",studentId)
				.content(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();		
		int status2 = result.getResponse().getStatus();
		assertEquals(200, status2);	
	}
	@Test
	public void Test2() throws Exception {
		int studentId =1;		
		
		when(repo.findById(studentId)).thenReturn(Optional.empty());
		MvcResult result = mockMvc.perform(get("/student/findone/{id}",studentId))
				.andReturn();
		System.out.println(result.getResponse().getStatus());
	}
	@Test	
    public void test3() throws Exception {
        // Arrange
        Student student = new Student(1, "Alice", LocalDate.of(1999, 5, 15));

        when(repo.save(student)).thenReturn(student);

        // Create an ObjectMapper with JavaTimeModule
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // Act & Assert
        mockMvc.perform(post("/student/create")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(student))) // Serialize the object
            .andExpect(status().isCreated());
    }

	}



