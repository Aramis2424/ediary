package integration;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.srd.ediary.EdiaryApplication;

@Epic("Integration Tests")
@Feature("Common")
@SpringBootTest(classes = EdiaryApplication.class)
@ActiveProfiles("integration_test")
class EdiaryApplicationTests {

	@Test
	void contextLoads() {
	}

}
