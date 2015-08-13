import Em from 'ember';

const {
    Route, inject, $
} = Em;

export default Route.extend({
    tagName: '',
    navProfileIsExpanded: false,

    session: inject.service(),

    translationsFetcher: inject.service(),

    activate: function() {
        this.windowOnResize();
        $(window).resize(this.windowOnResize);
    },

    windowOnResize: function() {
        if (window.innerWidth <= 800) {
            $('body').addClass('phone');
        } else {
            $('body').removeClass('phone');
        }
    },

    beforeModel: function() {
        return this.get('translationsFetcher').fetch();
    },

    afterModel: function(user) {
        // this.set('i18n.locale', user.get('locale'));
        if (!this.get('session').isAuthenticated()) {
            // window.location.href = 'Provider?id=login';
        } else {
            if (this.get('session').get('user.redirect') === 'setup') {
                this.controllerFor('budget').set('isEditMode', true);
                this.transitionTo('budget');
            } else {
                this.controllerFor('budget').send('check');
            }

            $('.page-loading').hide();
        }
    },

    model: function() {
        var sessionService = this.get('session');
        return sessionService.getSession().then(function() {
            return sessionService.get('user');
        });
    },

    actions: {
        logout: function() {
            // var route = this;
            var authMode = this.get('session.user.authMode');
            this.get('session').logout().then(function() {
                // route.transitionTo('index');
                if (authMode === 'DIRECT_LOGIN') {
                    window.location.href = '/CashTracker/Provider?id=welcome';
                } else {
                    window.location.href = '/Nubis/Provider?id=login';
                }
            });
        },

        goBack: function() {
            history.back(-1);
        },

        navAppMenuToggle: function() {
            $('body').toggleClass('nav-app-open');
        },

        navUserMenuToggle: function() {
            $('body').toggleClass('nav-ws-open');
        },

        hideOpenedNav: function() {
            $('body').removeClass('nav-app-open nav-ws-open');
        },

        toggleSearchForm: function() {
            $('body').toggleClass('search-open');
        },

        toggleNavProfile: function() {
            $('.nav-profile').toggleClass('expanded');
        },

        willTransition: function() {
            this.send('hideOpenedNav');
        },

        error: function(_error, transition) {
            console.log(_error);

            if (_error.errors && _error.errors.length && _error.errors[0].status === '401') {
                // window.location.href = 'Provider?id=login';
            }

            if (_error.status === 401 || (!this.get('session').isAuthenticated() && this.routeName !== 'login')) {
                window.location.href = '/CashTracker/Provider?id=login';

                /*this.controllerFor('login').setProperties({
                    transition: transition
                });*/

                // this.transitionTo('login');
            } else if (_error.status === '400') {
                return true;
            } else {
                return true;
            }
        }
    }
});
