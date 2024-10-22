package com.bolsadeideas.springboot.backend.apirest.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
		info = @Info(
				title = "API CRUDs", 
				description = "Different test CRUDs with their relationships",
				termsOfService = "www.christiangarcia.com/terms",
				contact = @Contact(
						name = "Christian Garcia",
						url = "www.christiangarcia.com/contact",
						email = "christian@gmail.com"
						),
				license = @License(
						name = "Standard Software Use License",
						url = "www.christiangarcia.com/license"
						),
				version = "1.0.0"
				),
		servers = {
				@Server(
						description = "DEV SERVER",
						url = "http://localhost:8080"
						),
				@Server(
						description = "PROD SERVER",
						url = "http://christian:8080"
						)
				}
		)
public class SwaggerConfig {}
