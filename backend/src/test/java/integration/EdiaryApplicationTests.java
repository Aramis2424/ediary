package integration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.srd.ediary.EdiaryApplication;

@SpringBootTest(classes = EdiaryApplication.class)
@ActiveProfiles("integration_test")
class EdiaryApplicationTests {

	@Test
	void contextLoads() {
	}

}
