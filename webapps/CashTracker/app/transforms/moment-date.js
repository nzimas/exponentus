import DS from 'ember-data';
import moment from 'moment';

var MomentDate = DS.Transform.extend({
    deserialize: function(serialized) {
        if (serialized) {
            return moment(serialized);
        }
        return serialized;
    },
    serialize: function(deserialized) {
        if (deserialized) {
            return moment(deserialized).toDate();
        }
        return deserialized;
    }
});

export default MomentDate;
