package com.kyozhou.jbooter;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class MsReportApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Test
	public void aksk() {
		javax.crypto.KeyGenerator generator = null;
		try {
			generator = javax.crypto.KeyGenerator.getInstance("HMACSHA256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		generator.init(120);
		byte[] awsAccessKeyId = generator.generateKey().getEncoded();
		generator.init(240);
		byte[] awsSecretAccessKey = generator.generateKey().getEncoded();

		System.out.println(UUID.randomUUID().toString());

		String accessKey = Base64.getEncoder().encodeToString(awsAccessKeyId);
		System.out.println(accessKey);

		String secretKey = Base64.getEncoder().encodeToString(awsSecretAccessKey);
		System.out.println(secretKey);

	}

	@Test
	public void byteTest() {

		byte[] bytes= new byte[1];
		bytes[0] = 7;
		byte result = (byte) (bytes[0] >> 1);
	}

	@Test
	public void divideTest() {

		System.out.println(OffsetDateTime.now(ZoneOffset.ofHours(8)).toEpochSecond() / 3600 * 3600);
	}

	@Test
	public void timeTest() {
		long firstTimeStamp = (long)1568221626 * 1000;
		Timestamp timestamp = new Timestamp(firstTimeStamp);
		OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(timestamp.toInstant(), ZoneOffset.ofHours(8));
		if(offsetDateTime.getHour() >= 0 && offsetDateTime.getHour() <=17) {
			offsetDateTime = offsetDateTime.minusDays(1);
		}
		String reportDate = offsetDateTime.format(DateTimeFormatter.ofPattern("s"));
		System.out.println(reportDate);
	}

}
