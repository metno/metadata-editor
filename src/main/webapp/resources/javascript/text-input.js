var TextInput = Widget.extend({
	
	buildUI : function () {
	    
	    var container = this.getContainer();	    
	    var htmlId = this.generateHtmlId();
	    var html = '<input type="text" value="" name="' + htmlId + '" id="' + htmlId + '" />';	    
	    container.html(html);
	    
	},
	
	populateValues : function (varMaps) {
	    
	    if( varMaps.length > 1 ){
	        console.log("TextInput does not support a list of values");
	        return;
	    }
	    
	    var varMap = varMaps[0];
	    var attrs = varMap["attrs"];
	    
	    if( !("value" in attrs)){
	        console.log("TextInput expects a single attribute called 'value'");
	    }
	    
	    var element = jQuery('#' + this.generateHtmlId());
	    element.val(attrs["value"]);
	    
	},
	
	generateHtmlId : function () {
	    return this.widgetId + '__value';
	}
	
});