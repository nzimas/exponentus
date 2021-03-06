package cashtracker.validation;

import cashtracker.model.Tag;


public class TagValidator {

	public TagValidator() {
	}

	public ValidationError validate(Tag m) {
		ValidationError ve = new ValidationError();

		if (m.getName() == null || m.getName().isEmpty()) {
			ve.addError("name", "required", "required");
		} else if (m.getName().length() > 32) {
			ve.addError("name", "invalid", "too_long");
		}

		return ve;
	}
}
