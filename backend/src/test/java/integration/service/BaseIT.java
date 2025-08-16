package integration.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.srd.ediary.EdiaryApplication;

@SpringBootTest(classes = EdiaryApplication.class)
@ExtendWith(SpringExtension.class)
@Transactional
@Rollback
@Sql("/db/initIT.sql")
@ActiveProfiles("integration_test")
public abstract class BaseIT {
}
