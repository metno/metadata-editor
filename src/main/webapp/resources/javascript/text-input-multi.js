var TextInputMulti = Widget.extend({
	
    numValues : 0,
    
    nextIdNum : 0,
    
    attrName : "str",
    
    attrClass : "",
    
    buildUI : function () {
        
        var container = this.getContainer();        
        var fieldContainerId = this.generateFieldContainerId();
        var addButtonId = this.widgetId + '__add';
        
        var outerThis = this;
        var addFunc = function () {
            outerThis.addField(outerThis.nextIdNum);
        };
        
        var html = '<label>' + this.label + '</label>';
        html += '<div id="' + fieldContainerId + '"></div>';        
        html += '<button type="button" id="' + addButtonId + '">Add</button>';      
        container.html(html);
        
        jQuery('#' + addButtonId).bind("click", addFunc);
        
    },
    
    
    populateValues : function (varMaps) {
        
        this._super(varMaps);
        
        var content = varMaps["content"];
        
        for( var i = 0; i < content.length; i++ ){
            
            var inputElement = jQuery('#' + this.generateHtmlId(i));
            
            if( inputElement.size() == 0){
                this.addField(this.nextIdNum);
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
        
        var container = jQuery('#' + this.generateFieldContainerId());
        var htmlId = this.generateHtmlId(index);
        var buttonId = this.widgetId + "__remove__" + index;
        var fieldClass = this.widgetId + "__field";
        
        var html = '<div id="' + htmlId + '__container">';
        html += '<input class="' + fieldClass  + '" type="text" name="' + htmlId + '" id="' + htmlId + '" />';
        html += '<button type="button" id="' + buttonId + '">Remove</button>';
        html += '</div>';
        container.append(html);
        
        var outerThis = this;
        var removeFunc = function () {
            outerThis.removeField(index);
        };
        
        jQuery("#" + buttonId).bind("click", removeFunc );
        
        this.numValues++;
        this.nextIdNum++;
                
    },
    
    removeField : function(index){
        
        var htmlId = this.generateHtmlId(index);
        jQuery('#' + htmlId + '__container' ).detach();
        
        this.numValues--;
        
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
    },
    
    generateFieldContainerId : function () {
        return this.widgetId + "__fieldcontainer";
    }
    
});