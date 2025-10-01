package org.srd.ediary;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "eDiary API",
				version = "v1",
				description = "Server for personal diaries and moods"
		)
)
@SpringBootApplication
public class EdiaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(EdiaryApplication.class, args);
	}

}
