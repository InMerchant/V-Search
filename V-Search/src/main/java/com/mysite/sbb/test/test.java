package com.mysite.sbb.test;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Lob;// 파링 형식 blob 대용량 처리
import jakarta.persistence.Id;
@Entity
@Table(name="test",schema = "bae")
@Data
public class test {
	@Id
	private int TEST_NUM;
	
	private String TEST_CHAR;
	@Lob
	private byte[] TEST_LOB;
	
}
