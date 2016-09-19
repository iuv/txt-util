/**
 * Created by chuxianming on 16/9/13.
 */
//全局变量区
var index = 0;//添加自定义规则的索引数
var splitArr = {};

function executor() {
    var res = "";//拆分数据显示
    var it = $("#it").val();//输入待处理文本一行
    var xsp = $("#xsp").val();//拆分字符
    var ots = $("#ots").val();//输出格式
    if (!xsp) {
        return;
    }
    //主规则处理
    var its = it.split(xsp);
    $(its).each(function (i, v) {
        res += "$(" + i + "):" + v + "<br/>";
        splitArr["$(" + i + ")"] = v;
    });
    //子规则处理
    for (var i = 0; i < index; i++) {
        var xspi = $("#xspi" + i).val();
        var xspx = $("#xsp" + i).val();
        if (xspi && xspx && splitArr[xspi]) {
            var _tmp = splitArr[xspi].split(xspx);
            $(_tmp).each(function (i, v) {
                res += xspi + "(" + i + "):" + v + "<br/>";
                splitArr[xspi + "(" + i + ")"] = v;
            });
        }
    }
    var ks = Object.keys(splitArr);
    for(var i = ks.length; i>=0 ; i--){
        ots = ots.split(ks[i]).join(splitArr[ks[i]]);
    }
    $("#ot").html(ots);
    $("#xspres").html(res);
}
function addxsp() {
    $("#xspdiv").append('<p id="xspp' + index + '">将:<input onkeyup="executor()" id="xspi' + index + '" type="text" width="50"/>按:<input onkeyup="executor()" id="xsp' + index + '" type="text" width="50"/>分隔<button onclick="delxsp(' + index + ')">&nbsp;&nbsp;-&nbsp;&nbsp;</button></p>');
    index++;
}
function delxsp(i) {
    $("#xspp" + i).remove();
    executor();
}
$(document).ready(function () {
    $(".xx").bind("keyup", executor);
    $("#ab").bind("click", addxsp);
});
