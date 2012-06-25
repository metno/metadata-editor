var TextInput = Widget.extend({
	
    attrName : "str",
    
    attrClass : "",   
    
	buildUI : function () {
	    
	    var container = this.getContainer();	    
	    var htmlId = this.generateHtmlId();
	    var html = '<input type="text" value="" name="' + htmlId + '" id="' + htmlId + '" />';	    
	    container.html(html);
	    
	},
	
	populateValues : function (varMaps) {
	    
	    this._super(varMaps);
	    
	    var content = varMaps["content"];
	    
	    if( content.length > 1 ){
	        console.log("TextInput does not support a list of values");
	        return;
	    }
	    
	    content = content[0];
	    var attrs = content["attrs"];
	    
	    this.attrClass = attrs["@class"];
	    
	    if( !(this.attrName in attrs)){
	        console.log("TextInput expects a single attribute called 'value'");
	    }
	    
	    var element = jQuery('#' + this.generateHtmlId());
	    element.val(attrs[this.attrName]);
	    
	},
	
	getContent : function () {
	    
	    var content = [];
	    
	    var htmlId = this.generateHtmlId();
	    var element = jQuery('#' + htmlId);
        var currentValue = element.val();
	    
        var attrs = { "@class" : this.attrClass };
        attrs[this.attrName] = currentValue;
        content.push( { "attrs" : attrs, "children" : {} });
        
        return { content : content }; 
	    
	},
	
	generateHtmlId : function () {
	    return this.widgetId + '__' + this.attrName;
	}
	
});