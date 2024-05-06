package br.com.fiap.financaspro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@Controller
@EnableCaching
@OpenAPIDefinition(
	info = @Info(
		title = "API do Finanças Pro",
		description = "App de controle de gastos pessoais",
		contact = @Contact(name = "João Carlos", email = "joao@fiap.com.br"),
		version = "1.0.0"
	)
)
public class FinancasproApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancasproApplication.class, args);
	}

	@RequestMapping
	@ResponseBody
	public String home(){
		return "Finanças Pro";
	}

}
