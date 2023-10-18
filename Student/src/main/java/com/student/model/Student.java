package com.student.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Entity
@Table(name = "Student")
public class Student {
	@Id
	@Column (name = "Id")
	private int sCode;
	@Column (name = "Name")
	private String sName;
	@Column (name = "DOB")
	private LocalDate sDOB;

}
