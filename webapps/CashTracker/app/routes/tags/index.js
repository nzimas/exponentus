import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.findAll('tag');
    },

    actions: {
        add() {
            this.transitionTo('tags.new');
        }
    }
});