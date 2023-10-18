package com.student.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.CSVWriter;
import com.student.exception.BadRequest;
import com.student.exception.ResourceNotFoundException;
import com.student.exporttoexcelutils.ExcelExportUtils;
import com.student.model.Student;
import com.student.repo.StudentRepo;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

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
	
	public String exportToCsv() throws Exception {
//		log.info("exportToCsv() in service called!!!");
		List<Student> all = getAll();
		try {
			StringWriter stringWriter = new StringWriter();
			CSVWriter csvWriter = new CSVWriter(stringWriter);

			// Define CSV headers
			String[] headers = { "Student ID", "Student Name", "Student DOB"};
			csvWriter.writeNext(headers);
//			log.info("Headers Populated for CSV !!!");

			// Write data to CSV
			for (Student stu : all) {
				{String[] dataEntry = {String.valueOf(stu.getSCode()),
						stu.getSName(),String.valueOf(stu.getSDOB())};							
					csvWriter.writeNext(dataEntry);
				}
			}
			csvWriter.close();
//			log.info("Calculated Loan Details Populated in CSV !!!");
			return stringWriter.toString();
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
    public void exportStudentsToExcel(HttpServletResponse response) throws IOException {
    	List<Student> all = getAll();

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("all");

        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Id");
        headerRow.createCell(1).setCellValue("Name");
        headerRow.createCell(2).setCellValue("DOB");

        int rowNum = 1;
        for (Student student : all) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(student.getSCode());
            row.createCell(1).setCellValue(student.getSName());
            row.createCell(2).setCellValue(student.getSDOB().toString());
        }

       ServletOutputStream outputStream = response.getOutputStream();
        	workbook.write(outputStream);
            workbook.close();
            outputStream.close();        
    }
        
    public List<Student> exportCustomerToExcel(HttpServletResponse response) throws IOException {
    	List<Student> findAll = repo.findAll();
        ExcelExportUtils exportUtils = new ExcelExportUtils(findAll);
        exportUtils.exportDataToExcel(response);
        return findAll;
    }
}
