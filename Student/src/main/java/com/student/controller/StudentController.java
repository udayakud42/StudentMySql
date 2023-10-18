package com.student.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.student.exception.ResourceNotFoundException;
import com.student.model.Student;
import com.student.repo.StudentRepo;
import com.student.service.StudentService;

@RestController
@RequestMapping("/api/v1")
public class StudentController {
	@Autowired
	private StudentService serv;
	
	@GetMapping("/find/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<Student> findOne(@PathVariable int id) throws Exception {	
		Student one = serv.getOne(id);
		 return new ResponseEntity<Student>(one,HttpStatus.OK);
	}
	
	@GetMapping("/find")
	@ResponseStatus(code = HttpStatus.OK)
	public ResponseEntity<?> find() throws Exception {			
		 List<Student> all = serv.getAll();
		 return new ResponseEntity<>(all,HttpStatus.OK);
	}
	
	@PostMapping("/create")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<?> create(@RequestBody Student stu) {
		String created = serv.create(stu);
		return new ResponseEntity<>(created, HttpStatus.CREATED);
	}
	
	@PutMapping("/update/{id}")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<?> update (@PathVariable int id, @RequestBody Student stu){
		Student updated = serv.update(id,stu);
		return new ResponseEntity<>(updated,HttpStatus.CREATED);		
	}
	
	
}
