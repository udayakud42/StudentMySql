package com.student.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import com.student.service.StudentService;

import jakarta.servlet.http.HttpServletResponse;

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
	
	@GetMapping("/csv")
	@ResponseStatus(code = HttpStatus.CREATED)
	public ResponseEntity<?> generateCSV() throws Exception {			
		 try {
				String details = serv.exportToCsv();
				HttpHeaders headers = new HttpHeaders();
				headers.add("Content-Disposition", "attachment; filename=Student_Details.csv");
				return new ResponseEntity<>(details, headers, HttpStatus.CREATED);
			} catch (ResourceNotFoundException e) {
				return new ResponseEntity<>("No Calculated Loan Details Found", HttpStatus.BAD_REQUEST);
			}catch (Exception e) {
				return new ResponseEntity<>("Error exporting data to CSV", HttpStatus.BAD_REQUEST);
			}
	}
	 @GetMapping("/export")
	 @ResponseStatus(code = HttpStatus.CREATED)
	 public void exportStudentsToExcel(HttpServletResponse response) throws IOException {
	response.setContentType("application/octet-stream");
    String headerKey = "Content-Disposition";
    String headerValue = "attachment; filename=Student_Information_SimpleMethod.xlsx";
    response.setHeader(headerKey, headerValue);
		 serv.exportStudentsToExcel(response);
	    }
    @GetMapping("/export-to-excel")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Student_Information_withUtils.xlsx";
        response.setHeader(headerKey, headerValue);
        serv.exportCustomerToExcel(response);
    }
	
}
