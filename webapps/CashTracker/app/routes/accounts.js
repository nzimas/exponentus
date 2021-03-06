import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.findAll('account');
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('accounts.new');
        },

        saveRecord: function(account) {
            account.save().then(() => {
                this.transitionTo('accounts');
            }, function() {
                console.log(arguments);
            });
        },

        deleteRecord: function(account) {
            account.destroyRecord().then(() => {
                this.transitionTo('accounts');
            }, function(resp) {
                account.rollbackAttributes();
                alert(resp.errors.message);
            });
        }
    }
});
