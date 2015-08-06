import Em from 'ember';

export default Em.Controller.extend({
    transaction: Em.computed.alias('model'),

    accounts: function() {
        return this.store.findAll('account');
    }.property(),

    categories: function() {
        return this.store.findAll('category');
    }.property(),

    costCenters: function() {
        return this.store.findAll('costCenter');
    }.property(),

    actions: {
        save: function() {
            var _this = this;
            var model = this.get('transaction');
            model.save().then(function() {
                _this.transitionTo('transactions');
            });
        }
    }
});
