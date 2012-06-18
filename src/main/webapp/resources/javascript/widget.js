var Widget = Class.extend({

	widgetId : "",
	
	init : function(widgetId) {
		this.widgetId = widgetId;
		this.buildUI();
	},
	
	buildUI : function () {
		console.log("Override in sub class");
	},
	
	populateValues : function (varMap) {
		console.log("Override in sub class");
	},
	
	getContainer : function () {
		
		var container = jQuery('#' + this.widgetId );
		
		if( container.size() == 0 ){
			console.log("Container for '" + this.widgetId + "' not found");
			return null;
		} else if ( container.size() > 1 ){
			console.log("Multiple containers for '" + this.widgetId + "' found");
			return null;			
		}
		
		return container;
	}

});

