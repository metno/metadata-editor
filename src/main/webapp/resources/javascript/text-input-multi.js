var TextInputMulti = Widget.extend({
	
    numValues : 0,
    
    buildUI : function () {
        
        var container = this.getContainer();        
        var html = '<button>Add</button>';      
        container.html(html);
        
    },
    
    
    populateValues : function (varMaps) {
        
        for( var i = 0; i < varMaps.length; i++ ){
            
            var inputElement = jQuery('#' + this.generateHtmlId(i));
            
            if( inputElement.size() == 0){
                this.addField(i);
                inputElement = jQuery('#' + this.generateHtmlId(i));
            }
            
            var varMap = varMaps[i];
            var attrs = varMap['attrs'];
            
            if(!( "value" in attrs)){
                console.log("TextInputMulti expects a single attribute called 'value'");
                continue;
            }
            
            inputElement.val(attrs["value"]);
        }
        
        
    },
    
    addField : function (index) {
        
        var container = this.getContainer();
        var htmlId = this.generateHtmlId(index);
        
        var html = '<input type="text" name="' + htmlId + '" id="' + htmlId + '" />';
        container.append(html);
                
    },
    
    generateHtmlId : function (index) {
        return this.widgetId + "__" + index + "__value";       
    }
    
});