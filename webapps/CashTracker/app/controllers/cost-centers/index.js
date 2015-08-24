import Em from 'ember';

export default Em.Controller.extend({
    costCenters: Em.computed.alias('model'),

    hasAddAction: true,

    actions: {
        composeRecord() {
            this.transitionToRoute('cost_centers.new');
        }
    }
});