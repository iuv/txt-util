/**
 * Created by chuxianming on 16/9/13.
 */
function executor(){
    var res = "";//拆分数据显示
    var it = $("#it").val();//输入待处理文本一行
    var xsp = $("#xsp").val();//拆分字符
    var ots = $("#ots").val();//输出格式
    if(!xsp){
        return;
    }
    var its = it.split(xsp);
    $(its).each(function(i, v){
        res += i+":"+v+"<br/>";
        ots = ots.split("$("+i+")").join(v);
    });
    $("#ot").html(ots);
    $("#xspres").html(res);
}
$(document).ready(function(){
    $(".xx").bind("keyup",executor);
});
