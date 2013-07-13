$(document).ready(startup);
function startup() {
    update();
}
function update() {
    $('.master').mouseover(function() {
        $(this).addClass("darken");
    });
    $('.master').mouseout(function() {
        $(this).removeClass("darken");
    });
}