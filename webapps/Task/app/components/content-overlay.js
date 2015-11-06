import Em from 'ember';

export default Em.Component.extend({
    tagName: 'div',

    classNames: ['content-overlay'],

    action: 'hideOpenedNav',

    mouseDown: function(e) {
        e.preventDefault();
        this.sendAction();
    },

    touchStart: function(e) {
        e.preventDefault();
        this.sendAction();
    }
});
