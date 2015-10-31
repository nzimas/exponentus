import Em from 'ember';
import ModelRoute from '../../mixins/routes/model';

export default Em.Route.extend(ModelRoute, {
    templateName: 'tasks/task',

    model: function(params) {
        return this.store.find('task', params.task_id);
    },

    setupController: function(controller, model) {
        controller.set('task', model);
    }
});