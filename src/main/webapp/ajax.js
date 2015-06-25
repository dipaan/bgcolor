$(document).ready(function() {
    $("#set").attr('disabled','disabled');
    $("#fetch").click(function() {
        $.get('/BackgroundColorServlet',
            function(data) { // on success
                bgColorId = data.bgColorId;
                bgColor = data.bgColor;
                // disable fetch
                $("#fetch").attr('disabled','disabled');
                // enable set
                $("#set").removeAttr('disabled');
                timeout = setTimeout(expire, 15000);
            })
            .fail(function() { // on failure
                alert("Request failed. Please try again.");
            });
    });
    $("#set").click(function() {
        $("body").css({'background-color' : '#' + bgColor});
        expire();
        clearTimeout(timeout);
        $.post('/BackgroundColorServlet', { "bgColorId" : bgColorId },
            function() { // on success
            })
            .fail(function() { // on failure
                alert("Failed to notify server for bgColorId=" + bgColorId);
            });
    });
    function expire() {
        // disable set
        $("#set").attr('disabled','disabled');
        // enable fetch
        $("#fetch").removeAttr('disabled');
    }
});
