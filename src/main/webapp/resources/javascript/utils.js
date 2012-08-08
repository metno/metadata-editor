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