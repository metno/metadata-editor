var editor = {

    recordIdentifier : null,

    postPath : "rest/",

    getPath : "rest/",

    widgets : {},

    init : function(recordIdentifier) {
        this.recordIdentifier = recordIdentifier;
        
        this.buildUI();
        this.retrieveData();
    },
    
    reset : function () {
        this.buildUI();
        this.retrieveData();
    },

    buildUI : function() {

        var widgetContainers = jQuery(".widgetContainer");

        var outerThis = this;
        widgetContainers.each(function(index, domElement) {
            var element = jQuery(domElement);
            outerThis.addWidget(element.attr('id'), element.data('widgettype'), element.data('label'));
        });

    },

    addWidget : function(widgetId, widgetType, label) {

        var widget;
        if(widgetType == 'TextInput'){
            widget = new TextInput(widgetId, label);
        } else if ( widgetType = 'TextInputMulti'){
            widget = new TextInputMulti(widgetId, label);
        } else {
            console.log("Unknow widget type: " + widgetType );
            return;
        }
        
        this.widgets[widgetId] = widget;
    },

    populate : function(namespace, varMap) {

        for (varName in varMap) {

            if( !(varName in this.widgets)){
                console.log("Found not widget for variable: " + varName);
                continue;
            }
            
            var widget = this.widgets[varName];
            widget.populateValues(varMap[varName]);
            
        }

    },

    retrieveData : function() {

        jQuery.ajax(this.getPath + this.recordIdentifier, {

            dataType : 'json',
            type : 'GET',
            context : this,

            success : function(data, textStatus) {
                this.populate("", data);
            },

            error : this.reportError

        });

    },
    
    save : function () {
        
        var content = this.getEditorContent();
        this.postEditorContent(content);
        
        console.log(content);
        
    },
    
    getEditorContent : function () {

        var varMap = {};
        for( varName in this.widgets ){
            
            var widget = this.widgets[varName];
            varMap[varName] = widget.getContent();                        
        }
        
        return varMap;
    },
    
    postEditorContent : function(content) {
                
        jQuery.ajax(this.postPath + this.recordIdentifier, {

            dataType : 'json',
            contentType : "application/json",
            data : JSON.stringify(content),
            type : 'POST',
            context : this,

            success : function(data, textStatus) {
                console.log("Data posted:" + data);
            },

            error : function (jqXHR, textStatus, errorThrown) {
                
                if(jqXHR.status == 404){
                    this.recordDoesNotExist();
                } else {
                    this.reportError();    
                }
            }

        });        
        
    },

    reportError : function() {

        console.log("Error");

    },
    
    recordDoesNotExist : function () {
        
        jQuery('#recordDoesNoExist').dialog({ modal : true } );
        
    }
};