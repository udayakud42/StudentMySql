package com.student.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.student.exception.BadRequest;
import com.student.exception.ResourceNotFoundException;
import com.student.model.Student;
import com.student.repo.StudentRepo;

@Service
public class StudentService {
	@Autowired
	private StudentRepo repo;

	public Student getOne(int id) {
		try {
			Student student = repo.findById(id).get();
			return student;
		} catch (Exception e) {
			throw new ResourceNotFoundException("Student not found with requested ID.");
		}
	}

	public List<Student> getAll() {
		List<Student> findAll = repo.findAll();
		if (findAll.isEmpty()) {
			throw new ResourceNotFoundException("No Student details found");
		}
		return findAll;
	}

	public String create(Student student) {
		try {
			Student saved = repo.save(student);
			return "new Student saved to DB with ID : " + saved.getSCode();
		} catch (Exception e) {
			throw new BadRequest("Bad Request");
		}
	}

	public Student update(int id, Student stu) {
		return this.repo.findById(id).map(st -> {
			if (null == stu.getSName()) {
			} else {
				st.setSName(stu.getSName());
			}
			if (null == stu.getSDOB()) {
			} else {
				st.setSDOB(stu.getSDOB());
			}
			return repo.save(st);
		}).orElseThrow(() -> new ResourceNotFoundException("Student not found with id :" + id));
	}
}
