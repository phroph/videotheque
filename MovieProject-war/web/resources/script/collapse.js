$(document).ready(function() {
    $('.collapsible').addClass('collapsed');
    $('.collapsor').click(function () {
       $('.collapsible').addClass('collapsed'); 
    });
    $('.expander').click(function () {
            $('.collapsible').removeClass('collapsed');
    });
    $('.parent').click(function () {
        $('.collapsible').addClass('collapsed');
        $(this).next().toggleClass('collapsed');
    });
});

