CT.UserController = Ember.ObjectController.extend({
    actions: {
        save: function(user) {
            user.save();
        }
    }
});