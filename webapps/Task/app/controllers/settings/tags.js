import Em from 'ember';

export default Em.Controller.extend({
    tagsSorting: ['isNew:desc', 'name'],

    tags: Em.computed.sort('model', 'tagsSorting'),

    newTagName: '',
    isSaveProcess: false,

    actions: {
        keyDownOnNew: function(value, event) {
            if (event.keyCode === 13) {
                event.stopPropagation();
                event.preventDefault();

                this.send('saveNewTag');
                return false;
            }
        },

        saveNewTag: function() {
            if (this.get('newTagName.length')) {
                let tag = this.store.createRecord('tag', {
                    name: this.get('newTagName')
                });
                this.set('isSaveProcess', true);
                this.send('saveTag', tag, () => {
                    this.set('newTagName', '');
                    this.set('isSaveProcess', false);
                }, () => {
                    this.set('isSaveProcess', false);
                    tag.deleteRecord();
                });
            }
        },

        addChildTag: function(tag) {
            this.store.createRecord('tag', {
                parent: tag
            });
        },

        saveTag: function(tag, callSuccess, callError) {
            tag.save().then(() => {
                this.transitionToRoute('settings.tags.tag', tag);
                callSuccess && callSuccess();
            }, function() {
                callError && callError();
                console.log(arguments);
            });
        },

        deleteTag: function(tag) {
            tag.destroyRecord().then(() => {
                this.transitionToRoute('settings.tags');
            }, function(resp) {
                tag.rollbackAttributes();
                alert(resp.errors.message);
            });
        },

        setTagActiveRoute: function(tag) {
            this.transitionToRoute('settings.tags.tag', tag);
        }
    }
});