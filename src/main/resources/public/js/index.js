/**
 * Created by chuxianming on 16/9/13.
 */
//全局变量区
var index = 0;//添加自定义规则的索引数

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
function addxsp(){
    $("#xspdiv").append('<p id="xspp'+index+'">将:<input class="xx" id="xspi'+index+'" type="text" width="50"/>按:<input class="xx" id="xsp'+index+'" type="text" width="50"/>分隔<button onclick="delxsp('+index+')">&nbsp;&nbsp;-&nbsp;&nbsp;</button></p>');
    index++;
}
function delxsp(i){
    $("#xspp"+i).remove();
}
$(document).ready(function(){
    $(".xx").bind("keyup",executor);
    $("#ab").bind("click",addxsp);
});
