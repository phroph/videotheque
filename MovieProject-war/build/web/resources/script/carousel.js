var timeoutId;
$(document).ready(function() {
    imgOne();
});
function imgOne() {
    window.clearTimeout(timeoutId);
    $("img.slideshow").attr("src", "/MovieImages/GodfatherCover.png");
    $("a#1").css("color", "#FFF");
    $("a#2").css("color", "#999");
    $("a#3").css("color", "#999");
    $("a#4").css("color", "#999");
    timeoutId = window.setTimeout(imgTwo, 3000);
}
function imgTwo() {
    window.clearTimeout(timeoutId);
    $("img.slideshow").attr("src", "/MovieImages/Taken2.png");
    $("a#2").css("color", "#FFF");
    $("a#1").css("color", "#999");
    $("a#3").css("color", "#999");
    $("a#4").css("color", "#999");
    timeoutId = window.setTimeout(imgThree, 3000);
}
function imgThree() {
    window.clearTimeout(timeoutId);
    $("img.slideshow").attr("src", "/MovieImages/InceptionCover.png");
    $("a#3").css("color", "#FFF");
    $("a#2").css("color", "#999");
    $("a#1").css("color", "#999");
    $("a#4").css("color", "#999");
    timeoutId = window.setTimeout(imgFour, 3000);
}
function imgFour() {
    window.clearTimeout(timeoutId);
    $("img.slideshow").attr("src", "/MovieImages/AvatarCover.png");
    $("a#4").css("color", "#FFF");
    $("a#2").css("color", "#999");
    $("a#3").css("color", "#999");
    $("a#1").css("color", "#999");
    timeoutId = window.setTimeout(imgOne, 3000);
}