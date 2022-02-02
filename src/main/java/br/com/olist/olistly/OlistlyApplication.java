package br.com.olist.olistly;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import br.com.olist.olistly.cryptography.aes.builder.AESCryptographyBuilder;
import br.com.olist.olistly.cryptography.aes.builder.AESCryptographySecretKeyIvBuilder;
import br.com.olist.olistly.cryptography.aes.enumerator.AESPadding;
import br.com.olist.olistly.cryptography.aes.enumerator.AESVariation;
import br.com.olist.olistly.cryptography.model.CryptographySecretKeyIv;
import br.com.olist.olistly.cryptography.model.Cryptor;
import br.com.olist.olistly.email.enumerator.ServiceType;
import br.com.olist.olistly.email.factory.FactoryEmailService;
import br.com.olist.olistly.email.service.IEmailService;
import br.com.olist.olistly.model.JWTInfo;
import br.com.olist.olistly.model.system.Information;

@SpringBootApplication
@EntityScan(basePackages = {"br.com.olist.olistly.model"})
@EnableJpaRepositories(basePackages = {"br.com.olist.olistly.repository"})
public class OlistlyApplication {

	@Value("${jwt.secret}")
	private String jwtSecret;

	@Value("${email.type}")
	private String emailType;

	@Value("${system.name}")
	private String systemName;

	@Value("${system.homepage}")
	private String systemHomepage;

	@Value("${system.frontendurl}")
	private String systemFrontendURL;

	@Value("${system.backendurl}")
	private String systemBackendURL;

	@Value("${system.contact}")
	private String systemContact;

	@Value("${system.noreply}")
	private String systemNoreplay;

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

	@Bean
	public ProjectionFactory projectionFactory() {
		return new SpelAwareProxyProjectionFactory();
	}

	@Bean
	public IEmailService emailService() {
		ServiceType type = ServiceType.getServiceType(emailType);
		return new FactoryEmailService().getEmailService(type);
	}

	@Bean
	public JWTInfo jwtInfo() {
		return new JWTInfo(jwtSecret);
	}

	@Bean
	public Information information() {
		return new Information(systemName, systemHomepage, systemFrontendURL, systemBackendURL, systemContact, systemNoreplay);
	}

	@Bean
	public Cryptor cryptor() {
		try {
			CryptographySecretKeyIv secretKeyIv = AESCryptographySecretKeyIvBuilder.read("cryptography.properties").build();
			return AESCryptographyBuilder
					.usingMode(AESVariation.CBC)
					.withPadding(AESPadding.PKCS5PADDING)
					.usingSecretKey(secretKeyIv.getSecretKey())
					.withIV(secretKeyIv.getIV())
				.build();
		} catch (Exception exception) {
			throw new BeanCreationException("Erro ao criar cryptor bean!", exception);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(OlistlyApplication.class, args);
	}

}
