import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.findAll('cost-center');
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('cost_centers.new');
        },

        saveCostCenter: function(costCenter) {
            costCenter.save().then(() => {
                this.transitionTo('cost_centers');
            });
        },

        deleteRecord: function(costCenter) {
            costCenter.destroyRecord().then(() => {
                this.transitionTo('cost_centers');
            }, function(resp) {
                costCenter.rollbackAttributes();
                alert(resp.errors.message);
            });
        }
    }
});
