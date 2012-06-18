var editor = {

    recordIdentifier : "test",

    postPath : "rest/",

    getPath : "rest/",

    widgets : {},

    init : function() {
        this.buildUI();
        this.retrieveData();
    },

    buildUI : function() {

        var widgetContainers = jQuery(".widgetContainer");

        var outerThis = this;
        widgetContainers.each(function(index, domElement) {
            var element = jQuery(domElement);
            outerThis.addWidget(element.attr('id'), element.data('widgettype'));
        });

    },

    addWidget : function(widgetId, widgetType) {

        var widget;
        if(widgetType == 'TextInput'){
            widget = new TextInput(widgetId);
        } else if ( widgetType = 'TextInputMulti'){
            widget = new TextInputMulti(widgetId);
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
            
//            var values = varMap[varName];
//            for ( var i = 0; i < values.length; i++) {
//                this.populateVariable(namespace, varName, i, values[0]);
//            }
        }

    },

    populateVariable : function(namespace, varName, index, varData) {

        var attrs = varData['attrs'];

        for ( var attrName in attrs) {
            var htmlId = '#' + namespace + varName + '__' + index + '__value';
            var value = attrs[attrName];
            var element = jQuery(htmlId);

            if (element.size() == 1) {
                element.val(value);
            } else {
                console.log("Failed to find element '" + htmlId + "'");
            }

        }

        this.populate(namespace + varName + "__", varData['children']);

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

    reportError : function() {

        console.log("Error");

    }
};