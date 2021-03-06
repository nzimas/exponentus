package cashtracker.validation;

import cashtracker.model.Account;


public class AccountValidator {

	public AccountValidator() {
	}

	public ValidationError validate(Account m) {
		ValidationError ve = new ValidationError();

		if (m.getName() == null || m.getName().isEmpty()) {
			ve.addError("name", "required", "required");
		} else if (m.getName().length() > 64) {
			ve.addError("name", "invalid", "too_long");
		}

		if (m.getCurrencyCode() == null || m.getCurrencyCode().isEmpty()) {
			ve.addError("currencyCode", "required", "required");
		} else if (m.getCurrencyCode().length() > 3) {
			ve.addError("currencyCode", "invalid", "too_long");
		}

		return ve;
	}
}
