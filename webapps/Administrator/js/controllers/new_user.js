AdminApp.NewUserController = Ember.ObjectController.extend({
    actions: {
        save: function(user) {
            user.save();
            this.transitionTo('users');
        }
    }
});