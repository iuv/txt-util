/**
 * Created by ixx on 16/9/13.
 */
//全局变量区
var index = 0;//添加自定义规则的索引数
var splitArr = {};//存储拆分的数组
var rule = {};//上传规则格式化

function executor() {
    splitArr = {};//清空缓存
    rule = {"z":[]};
    var res = "";//拆分数据显示
    var it = $("#it").val();//输入待处理文本一行
    var xsp = $("#xsp").val();//拆分字符
    var ots = $("#ots").val();//输出格式
    var template = ots;
    if (!xsp) {
        return;
    }
    //根据换行拆分输入,多行处理
    // var itLines = it.split("\n");
    //主规则处理
    var its = it.split(xsp);
    rule["root"] = xsp;
    $(its).each(function (i, v) {
        res += formatRes("$(",i,v);
    });
    //子规则处理
    for (var i = 0; i < index; i++) {
        var xspi = "$("+$("#xspi" + i).val()+")";
        var xspx = $("#xsp" + i).val();
        rule.z.push(xspi+xspx);
        if (xspi && xspx && splitArr[xspi]) {
            var xspiFix = xspi.substr(0,xspi.length-1)+".";//去掉最后一个")"
            var _tmp = splitArr[xspi].split(xspx);
            $(_tmp).each(function (i, v) {
                res += formatRes(xspiFix, i, v);
            });
        }
    }
    var ks = Object.keys(splitArr);
    for(var i = ks.length; i>=0 ; i--){
        ots = ots.split(ks[i]).join("<span class='blc'>"+splitArr[ks[i]]+"</span>");
    }
    $("#ot").html(ots);
    $("#xspres").html(res);
    //上传属性设值
    $("#template").val(template);
    $("#rule").val(JSON.stringify(rule));
    $("#show_rule").text(JSON.stringify(rule));
    $("#show_rule_class").text(JSON.stringify(rule).split("\"").join("\\\""));
}
//格式化输出拆分数据
function formatRes(head, i, v){
    var key = head + i + ")";
    var res = "<span class='keyspan' onclick='setKey(\""+key+"\")'>" + key + "</span>:" + v + "<br/>";
    splitArr[key] = v;
    return res;
}
//在模板中设置key
function setKey(key){
    $("#ots").insertAtCaret(key);
    executor();//重新输出内容
}
//添加子规则
function addxsp() {
    $("#xspdiv").append('<p id="xspp' + index + '">将:$(<input onkeyup="executor()" id="xspi' + index + '" type="text" width="50"/>)按:<input onkeyup="executor()" id="xsp' + index + '" type="text" width="50"/>分隔<button onclick="delxsp(' + index + ')">&nbsp;&nbsp;-&nbsp;&nbsp;</button></p>');
    index++;
}
//删除子规则
function delxsp(i) {
    $("#xspp" + i).remove();
    executor();
}
//绑定事件
$(document).ready(function () {
    $(".xx").bind("keyup", executor);
    $("#ab").bind("click", addxsp);
});
