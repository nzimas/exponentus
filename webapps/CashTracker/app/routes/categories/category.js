import Em from 'ember';
import ModelRoute from '../../mixins/model-route';

export default Em.Route.extend(ModelRoute, {
    templateName: 'categories/category',

    model: function(params) {
        return this.store.find('category', params.category_id);
    },

    categories: function() {
        return this.store.findAll('category');
    }.property(),

    setupController: function(controller, model) {
        controller.set('category', model);
        controller.set('categories', this.get('categories'));
    }
});
