import DS from 'ember-data';
import EmberValidations from 'ember-validations';

export default DS.Model.extend(EmberValidations.Mixin, {
    login: DS.attr('string'),
    email: DS.attr('string'),
    roles: DS.attr('string'),

    validations: {
        login: {
            presence: true
        },
        email: {
            presence: true
        }
    }
});
