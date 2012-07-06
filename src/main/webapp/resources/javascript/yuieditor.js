YUI.add('metadata-editor', function(Y) {

    Y.HomeView = Y.Base.create('HomeView', Y.View, [], {
        render: function () {
            this.get('container').set('text', 'Hello');
            return this;
        }
    });
    
    
    Y.MetadataEditor = Y.Base.create('MetadataEditor', Y.App, [], {
        
        initializer : function (cfg){
            
            if( cfg !== undefined ){
                if( 'baseurl' in cfg ){
                    this.set('root', cfg['baseurl']);
                }
                
            }
            
        },
        
        // Default registered views inherited by all CustomApp instances.
        views : {
            home : {
                preserve : true,
                type : 'HomeView'
            },
            editor : {
                preserve : true
            }
        },

        // Default route handlers inherited by all CustomApp instances.

        handleHome : function(req) {
            this.showView("home");
        },

        handleEditor : function(req) {
            this.showView("editor");
        },

    }, {
        ATTRS : {
            routes : {
                value : [ {
                    path : '/',
                    callback : 'handleHome'
                }, {
                    path : '/editor/:record',
                    callback : 'handleEditor'
                } ]
            }
        }
    });

}, '0.0.1', {
    requires : [ 'app', 'console' ]
});