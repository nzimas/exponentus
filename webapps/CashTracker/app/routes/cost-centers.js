import Ember from 'ember';

export default Ember.Route.extend({
    model: function(params) {
        return this.store.findAll('cost-center');
    },

    actions: {
        selectAll: function() {}
    }
});