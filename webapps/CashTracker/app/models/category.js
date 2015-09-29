import DS from 'ember-data';

export default DS.Model.extend({
    transactionTypes: DS.attr(),
    parent: DS.belongsTo('category'),
    children: DS.hasMany('category', {
        inverse: 'parent',
        embedded: 'always'
    }),
    name: DS.attr('string'),
    enabled: DS.attr('boolean', {
        defaultValue: true
    }),
    note: DS.attr('string'),
    color: DS.attr('string', {
        defaultValue: '#FFFFFF'
    })
});
