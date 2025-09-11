package org.srd.fakeDataGenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.srd.fakeDataGenerator.service.Generator;

@SpringBootApplication
public class FakeDataGeneratorApplication {

	public static void main(String[] args) {
        ConfigurableApplicationContext context =
                SpringApplication.run(FakeDataGeneratorApplication.class, args);

        Generator generator = context.getBean("generatorBaseImpl", Generator.class);

        generator.run();
	}

}
