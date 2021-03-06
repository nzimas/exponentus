/* global require, module */
var EmberApp = require('ember-cli/lib/broccoli/ember-app'),
    environment = EmberApp.env(),
    isProduction = environment === 'production',
    disabled = {
        enabled: false
    },
    assetLocation;

assetLocation = function(fileName) {
    return '/assets/' + fileName;
};

module.exports = function(defaults) {
    var app = new EmberApp(defaults, {
        outputPaths: {
            app: {
                js: assetLocation('app.js')
            },
            vendor: {
                js: assetLocation('vendor.js'),
                css: assetLocation('vendor.css')
            }
        },
        fingerprint: disabled
    });

    app.import('bower_components/jq_plugins/jquery.ui.widget.js');
    app.import('bower_components/jq_plugins/jquery.fileupload.js');

    return app.toTree();
};
