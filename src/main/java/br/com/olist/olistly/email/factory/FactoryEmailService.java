package br.com.olist.olistly.email.factory;

import br.com.olist.olistly.email.enumerator.ServiceType;
import br.com.olist.olistly.email.exception.InvalidEmailServiceException;
import br.com.olist.olistly.email.service.IEmailService;
import br.com.olist.olistly.email.service.LocalEmailService;
import br.com.olist.olistly.email.service.PropertiesEmailService;

public class FactoryEmailService {

	public IEmailService getEmailService(ServiceType type) throws InvalidEmailServiceException {
		if (ServiceType.LOCAL.equals(type)) {
			return new LocalEmailService(new PropertiesEmailService());
		} else if (ServiceType.SERVICE.equals(type))
			throw new InvalidEmailServiceException("Email service not implemented!");
		else
			throw new InvalidEmailServiceException("Email service invalid!");
	}
}
