var Widget = Class.extend({

	widgetId : "",
	
	label : "",
	
	type : "",
	
	maxOccurs : null,
	
	minOccurs: null,
	
	resources : {},
	
	attrsType : {},
	
	serverClass : null,
	
	init : function(widgetId, label) {
		this.widgetId = widgetId;
		this.label = label;
		this.buildUI();
	},
	
	buildUI : function () {
		console.log("Override in sub class");
	},
	
	populateValues : function (varMap) {
	    this.populateAttributes(varMap);
	},
	
	populateAttributes : function (varMap){
	    
	    this.resources = varMap["resources"];
	    this.type = varMap["type"];
	    this.maxOccurs = varMap["maxOccurs"];
	    this.minOccurs = varMap["minOccurs"];
	    this.attrsType = varMap["attrsType"];
	    
	},
	
	getContent : function () {
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

