CT.User = DS.Model.extend({
    name: DS.attr('string'),
    password: DS.attr('string'),
    email: DS.attr('string'),
    role: DS.attr('string')
});
