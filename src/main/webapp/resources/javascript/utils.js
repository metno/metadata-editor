function ignore_enter_key () {
    if ( event.which == 13 ) {
        event.preventDefault();
    }
}