import Em from 'ember';

const PATH = 'rest/session';

const {
    Service, $
} = Em;

export default Service.extend({

    _isAuthenticated: false,

    isAuthenticated: function() {
        return this._isAuthenticated;
    },

    getSession: function() {
        return $.get(PATH).then(this._setResult.bind(this));
    },

    _setResult: function(result) {
        if (result.authUser.login) {
            this.set('user', result.authUser);
            this.set('_isAuthenticated', true);
        } else {
            this.set('_isAuthenticated', false);
        }
    },

    login: function(userName, password) {
        const _this = this;

        return $.ajax({
            method: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            url: PATH,
            data: JSON.stringify({
                authUser: {
                    login: userName,
                    pwd: password
                }
            }),
            success: function(result) {
                _this.set('_isAuthenticated', true);
                return result;
            }
        });
    },

    logout: function() {
        this.set('_isAuthenticated', false);
        this.set('user', null);
        return $.ajax({
            method: 'DELETE',
            url: PATH
        });
    }
});
