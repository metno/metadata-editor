function ignore_enter_key () {
    if ( event.which == 13 ) {
        event.preventDefault();
    }
}


function input_field_added ( data ) {
    
    // when a new input field is added we need to disable the enter key in
    // this field as well. Since we do not have access to the exact field here
    // we do the brute force approach and just reapply it to all input fields.
    if( data.status == "success" ){
        jQuery('input').keypress(ignore_enter_key);
    }
    
}

function jsf_element(jsf_id){
    
    return jQuery("[id='" + jsf_id + "']");
    
}

var map;
var vectorLayer;
function init_map (mapDivId) {
    
    map = new OpenLayers.Map(mapDivId, { theme : 'resources/theme/default/style.css' });
    var layer = new OpenLayers.Layer.OSM("Simple OSM Map");
    map.addLayer(layer);
     
    map.setCenter(
            new OpenLayers.LonLat(0, 0).transform(
                new OpenLayers.Projection("EPSG:4326"),
                map.getProjectionObject()
            ), 1
        );    
    
    // allow testing of specific renderers via "?renderer=Canvas", etc
    var renderer = OpenLayers.Util.getParameters(window.location.href).renderer;
    renderer = (renderer) ? [renderer] : OpenLayers.Layer.Vector.prototype.renderers;

    /*
     * Layer style
     */
    // we want opaque external graphics and non-opaque internal graphics
    var layer_style = OpenLayers.Util.extend({}, OpenLayers.Feature.Vector.style['default']);
    layer_style.fillOpacity = 0.2;
       
    vectorLayer = new OpenLayers.Layer.Vector("Simple Geometry", {
        style: layer_style,
        renderers: renderer
    });
    map.addLayer(vectorLayer);
    
}

/**
 * Draw a square bounding box on the map
 * @param coordContainer The container that contains the input fields with the bounding box
 * values.
 */
function add_lat_lon_bounding_box(coordContainer) {
    
    var north = jQuery('.lat-lon-north', coordContainer).val();
    var south = jQuery('.lat-lon-south', coordContainer).val();
    var east = jQuery('.lat-lon-east', coordContainer).val();
    var west = jQuery('.lat-lon-west', coordContainer).val();
    
    var proj = new OpenLayers.Projection("EPSG:4326");
    var upperLeft = new OpenLayers.Geometry.Point(west, north).transform(proj, map.getProjectionObject());
    var lowerLeft = new OpenLayers.Geometry.Point(west, south).transform(proj, map.getProjectionObject());
    var lowerRight = new OpenLayers.Geometry.Point(east, south).transform(proj, map.getProjectionObject());
    var upperRight = new OpenLayers.Geometry.Point(east, north).transform(proj, map.getProjectionObject());    
    
    add_polygon([upperLeft, lowerLeft, lowerRight, upperRight, upperLeft]);
}

/**
 * Draw a polygon bounding box to the map
 * @param showMapButton The button that was clicked to show the map. Used to find the values
 * to display.
 */
function add_polygon_bounding_box(showMapButton) {
    
    // this hack for getting the correct table was necessary since p:component in JSF did not
    // work for some unexplained reason.
    var table = jQuery(showMapButton).siblings('.polygon-bounding-box-table');

    var proj = new OpenLayers.Projection("EPSG:4326");
    var points = [];
    jQuery('.polygon-point', table).each(function (index){
        
        var value = jQuery(this).val();
        var values = value.split(" ");
        
        if( values.length != 2 ){
            return;
        }
        
        var point = new OpenLayers.Geometry.Point(values[0], values[1]).transform(proj, map.getProjectionObject());
        points.push(point);
    });
    
    // need at least 3 points in a polygon
    if( points.length < 3 ){
        return;
    }
    
    points[points.length] = points[0];
    
    add_polygon(points);
}

function add_polygon(points) {
    
    vectorLayer.removeAllFeatures();
    
    var linearRing = new OpenLayers.Geometry.LinearRing(points);
    var polygonFeature = new OpenLayers.Feature.Vector(
        new OpenLayers.Geometry.Polygon([linearRing]));
    
    vectorLayer.addFeatures([polygonFeature]);
    
}