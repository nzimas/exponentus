package cashtracker.validation

import cashtracker.model.Account


public class AccountValidator {

	private boolean isValid

	public AccountValidator() {
	}

	public boolean validate(Account m) {
		isValid = !m.getName().isEmpty()
		isValid
	}

	@Override
	public String toString() {
		return "$isValid";
	}
}
