/**
 * Created by chuxianming on 16/9/13.
 */
function executor(){
    var it = $("#it").val();
    var xsp = $("#xsp").val();
    $("#ot").text(it);
}
$(document).ready(function(){
    $(".xx").bind("keyup",executor);
});
