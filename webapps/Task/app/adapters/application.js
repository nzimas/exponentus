import DS from 'ember-data';

function getHost() {
    return window.location.pathname.replace('index.html', '');
}

export default DS.RESTAdapter.extend({
    namespace: getHost() + 'rest',

    pathForType: function(type) {
        console.log(arguments);
        switch (type) {
            case 'category':
                return 'categories';
            case 'role':
                return 'application/roles'
            default:
                return type + 's';
        }
    },

    shouldReloadAll: () => false
});
