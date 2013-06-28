
$(function() {
    $(':input').on('change', function() {
        setConfirmUnload(true);
    });

    $('form').on('submit', function() {
        setConfirmUnload(false);
    });
});

function setConfirmUnload(on) {
    var message = "You have unsaved data. Are you sure to leave the page?";
    window.onbeforeunload = (on) ? function() {
        return message;
    } : null;
}



