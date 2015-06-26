CT.LoginRoute = Ember.Route.extend({
    actions: {
        login: function() {
            var route = this,
                controller = this.get('controller');

            var authUser = this.store.createRecord('auth_user', {
                login: controller.get('username'),
                pwd: controller.get('password')
            });

            authUser.save().then(function(user) {
                var transition = controller.get('transition');

                route.session.set('auth_user', user);

                if (transition) {
                    transition.retry();
                } else {
                    route.transitionTo('index');
                }
            }, function() {
                console.log('-----------er', this);
            });
        },

        cancel: function() {
            this.transitionTo('index');
        }
    },

    beforeModel: function() {
        this.session.set('auth_user', null);
    },

    resetController: function(controller) {
        controller.setProperties({
            username: null,
            password: null,
            transition: null
        });
    }
});
