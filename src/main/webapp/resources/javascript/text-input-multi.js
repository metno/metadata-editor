var TextInputMulti = Widget.extend({
	
    numValues : 0,
    
    attrName : "str",
    
    attrClass : "",
    
    buildUI : function () {
        
        var container = this.getContainer();        
        var html = '<button>Add</button>';      
        container.html(html);
        
    },
    
    
    populateValues : function (varMaps) {
        
        this._super(varMaps);
        
        var content = varMaps["content"];
        
        for( var i = 0; i < content.length; i++ ){
            
            var inputElement = jQuery('#' + this.generateHtmlId(i));
            
            if( inputElement.size() == 0){
                this.addField(i);
                inputElement = jQuery('#' + this.generateHtmlId(i));
            }
            
            var varMap = content[i];
            var attrs = varMap['attrs'];
            
            this.attrClass = attrs["@class"];
            
            if(!( this.attrName in attrs)){
                console.log("TextInputMulti expects a single attribute called 'value'");
                continue;
            }
            
            inputElement.val(attrs[this.attrName]);

        }
        
        
    },
    
    addField : function (index) {
        
        var container = this.getContainer();
        var htmlId = this.generateHtmlId(index);
        
        var html = '<input type="text" name="' + htmlId + '" id="' + htmlId + '" />';
        container.append(html);
        
        this.numValues++;
                
    },
    
    getContent : function () {
        
        var content = [];
        for( var i = 0; i < this.numValues; i++ ){
            
            var htmlId = this.generateHtmlId(i);
            var element = jQuery('#' + htmlId);
            
            var attrs = { "@class" : this.attrClass };
            attrs[this.attrName] = element.val();
            content.push({ "attrs" : attrs, "children" : {} });
                        
        }
        
        return { "content" : content };
        
    },
    
    generateHtmlId : function (index) {
        return this.widgetId + "__" + index + "__" + this.attrName;       
    }
    
});