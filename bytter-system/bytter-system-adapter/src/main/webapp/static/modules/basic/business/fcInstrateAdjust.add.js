var topId = '';
bt.use(['form', 'layedit', 'selectType', 'corp', 'dateType', 'rateType'], function () {
    var $ = bt.jquery, form = bt.form();
    var corp = bt.corp.init();
    var select = bt.selectType.init();
    var time = bt.dateType.init();
    var rate = bt.rateType.init();

    function getJsonVal() {
        var jsonArray = [];
        var obj = new Object();
        var newVal = "";
        var checksVal = $("input[name='checks']");
        for (var i = 0; i < checksVal.length; i++) {
            if (checksVal[i].checked == true) {
                obj = new Object();
                newVal = checksVal[i].value;
                obj.account = newVal.split('A')[0];
                obj.instrtType = newVal.split('A')[1];
                obj.oldInstrate = $("#" + newVal.split('A')[0] + "O" + newVal.split('A')[1]).val();
                obj.newInstrate = $("#" + newVal).val();
                obj.lastAdjustTime = $("#" + newVal.split('A')[0] + "T" + newVal.split('A')[1]).val();
                obj.accStartDate = $("#" + newVal.split('A')[0] + "S" + newVal.split('A')[1]).val();
                jsonArray.push(obj);
            }
        }

        var arr = [];
        var types = $("#types").val();
        if (types) {
            var type = types.split(",");
            for (var i = 0; i < type.length; i++) {
                obj = new Object();
                obj.adjustTime = time.get("adjustTime" + type[i]).val();
                obj.instrtType = type[i];
                arr.push(obj);
            }
        }
        var changType = $("select[name='changType']").val();
        for (var i = 0; i < jsonArray.length; i++) {
            jsonArray[i].changtype = changType;
            for (var j = 0; j < arr.length; j++) {
                if (jsonArray[i].instrtType == arr[j].instrtType) {
                    jsonArray[i].adjustTime = arr[j].adjustTime;
                    break;
                }
            }
        }
        return jsonArray;
    }

    $("#submit").on("click", function () {
        if (!form.verifyFun(".verify")) {
            return;
        }
        var jsonArr = getJsonVal();

        if (jsonArr.length == 0) {
            // sysAlert("请勾选需要调整的账号行记录！");
            sysAlert(bt.lang("common.message.confirm.selectData"));
            return;
        }
        for (var i = 0; i < jsonArr.length; i++) {
            var lastAdjustTimes = new Date(Date.parse(jsonArr[i].lastAdjustTime));
            var adjustTime = new Date(Date.parse(jsonArr[i].adjustTime));
            var accStartDate = new Date(Date.parse(jsonArr[i].accStartDate));
            var account = jsonArr[i].account;
            if (isNotNull(jsonArr[i].lastAdjustTime)) {
                if (lastAdjustTimes > adjustTime) {
                    sysAlert(bt.lang("sys.message.effective.date.lt.last.adjust.date"));
                    return;
                }
            } else {
                if (accStartDate > adjustTime) {
                    sysAlert(bt.lang("sys.message.effective.date.activation.date"));
                    return;
                }
            }

            if (bt.rateType.getFun().format(jsonArr[i].oldInstrate) === bt.rateType.getFun().format(jsonArr[i].newInstrate)) {
                sysAlert(bt.lang("sys.message.new.instrate.not.equal.old.instrate"));
                return;
            }
            delete jsonArr[i]["lastAdjustTime"];
            delete jsonArr[i]["accStartDate"];
        }

        var poolSign = $("#poolSign").val();

        $.ajax({
            url: basePath + "/basic/business/fcinstrateadjust/save/" + poolSign,
            method: 'POST',
            cache: false,
            data: JSON.stringify(jsonArr),//将对象序列化成JSON字符串
            dataType: "json",
            contentType: 'application/json;charset=utf-8', //设置请求头信息
            success: function (result) {
                layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    if (bt.navtab) {
                        bt.navtab.refreshCurrent();
                        bt.navtab.refreshParent();
                    } else {
                        parent.layer ? parent.layer.closeAll() : layer.closeAll();
                    }
                }
            }
        });
    });

    function sysAlert(val) {
        // layer.alert(val, {
        //     skin: 'layui-layer-molv', //样式类名
        //     closeBtn: 0
        // });
        layer.msg(val, {
            icon: 5
            , shift: 6
        });
    }

    $("#close").on("click", function () {
        if (bt.navtab) {
            bt.navtab.refreshParent();
            bt.navtab.closeCurrent();
        }
    });

    form.on('select(changType)', function (data) {
        changeTypeShow(data.value);
        $dyncTable();
    });

    form.on('checkbox(instrtType)', function (data) {
        $dyncTable();

    });

    form.on('checkbox(controlAll)', function (data) {
        $("input[name='checks']").each(function () {
            if (data.elem.checked) {
                $(this).prop("checked", true);
            } else {
                $(this).prop("checked", false);
            }
        });
        form.render();
    });

    var changeTypeShow = function (val) {
        $(".tdIns").empty();
        if (val == '2') {
            $(".tdIns").append("<span>" + bt.lang('table.percentage.values') + "</span>");
        } else {
            $(".tdIns").append("<span>" + bt.lang('table.fcInstrateAdjust.newInstrate') + "</span>");
        }
    };

    $dyncTable = function () {
        var param = $validInfo();
        if (!param) {
            return;
        }
        $.ajax({
            url: basePath + "/basic/business/fcinstrateadjust/dyncTable",
            method: 'POST',
            dataType: 'json',
            data: param,
            async: false,
            success: function (data) {
                if (data) {
                    var html = "";
                    $.each(data, function (index) {
                        var item = data[index];
                        html += "<tr>";
                        html += "<td>";
                        html += "<input type=\"hidden\"  class='oldInterests" + item.instrtType + "' id=\"" + item.account + "O" + item.instrtType + "\" value=\"" + (item.oldInstrate == null ? "" : item.oldInstrate) + "\">";
                        html += "<input type=\"hidden\"  class='lastAdjustTime" + item.instrtType + "' id=\"" + item.account + "T" + item.instrtType + "\" value=\"" + (item.adjustTime == null ? "" : item.adjustTime) + "\">";
                        html += "<input type=\"hidden\"  class='accStartDate" + item.instrtType + "' id=\"" + item.account + "S" + item.instrtType + "\" value=\"" + (item.accStartDate == null ? "" : item.accStartDate) + "\">";
                        html += "<input type=\"hidden\" name=\"blnctrAccs\" id=\"blnctrAcc\"" + item.account + " value=\"" + item.account + "\">";
                        html += "<input type=\"checkbox\" name=\"checks\" lay-skin=\"primary\" value=\"" + item.account + "A" + item.instrtType + "\"  >";
                        html += "</td>";
                        html += "<td>" + (index + 1) + "</td>";
                        html += "<td>" + item.account + "</td>";
                        html += "<td>" + item.accName + "</td>";
                        html += "<td><input type='hidden' name=\"interestType\" value=\"" + item.instrtType + "\">" + item.interestName + "</td>";
                        html += "<td>" + (item.accStartDate == null ? "" : bt.date.format(item.accStartDate, "yyyy-MM-dd")) + "</td>";
                        html += "<td>" + (item.lastAdjustTime == null ? "" : bt.date.format(item.lastAdjustTime, "yyyy-MM-dd")) + "</td>";
                        html += "<td>" + (item.oldInstrate == null ? "" : bt.rateType.getFun().format(item.oldInstrate)) + "</td>";
                        html += "<td><input type=\"text\" class='layui-input bt-biz-rateType newInterests" + item.instrtType + "' data-ratetype=\"percent\" data-name=\"" + item.account + "A" + item.instrtType + "\" data-biz-name=\"" + item.account + "A" + item.instrtType + "\" ></td>";
                        html += "</tr>";
                    });
                    $("#dyncTable").append(html);
                    rate = bt.rateType.init();
                    form.render();
                }
            }
        });
    };

    var $validInfo = function () {
        var poolSign = $("#poolSign").val();
        $("#dyncTable").empty();
        var checkedinstrtTypes = "";
        var instrtTypes = $("input[name='instrtType']");
        for (var i = 0; i < instrtTypes.length; i++) {
            var obj = instrtTypes[i];
            if (obj.checked == true) {
                checkedinstrtTypes += $(obj).val() + ",";
                rate.get("newInstrate" + $(obj).val()).attr("lay-verify", "required");
                time.get("adjustTime" + $(obj).val()).attr("lay-verify", "required");
            } else {
                rate.get("newInstrate" + $(obj).val()).attr("lay-verify", "");
                time.get("adjustTime" + $(obj).val()).attr("lay-verify", "");
            }
            rate.get("newInstrate" + $(obj).val()).val("");
        }
        form.render();
        form.renderIcon();
        var instrtType = checkedinstrtTypes.substring(0, checkedinstrtTypes.length - 1);
        var changType = $("select[name='changType']").val();
        var netId;
        var corpId;
        if (poolSign == "1") {
            netId = select.get("netId").val();
            corpId = select.get("corpId").val();
        } else {
            netId = $('input[name="netId"]').val();
            corpId = $('input[name="corpId"]').val();
        }
        var subjectId = select.get("subjectId").val();
        var account = $('input[name="account"]').val();
        var curId = select.get("curId").val();
        if (!instrtType || !changType || !netId || !subjectId || !curId) {
            return;
        }
        if (instrtType.length == 0) {
            return;
        }

        $("#types").val(instrtType);

        var param = {
            netId: netId,
            corpId: corpId,
            subjectId: subjectId,
            curId: curId,
            account: account,
            instrtType: instrtType,
            type: 'add',
            date: (new Date()).getTime()
        };
        return param;
    };


    $soosRate = function (value, $elem) {
        var decimalLength = 6;
        var interestType = $elem.attr("instrtType");
        var changType = $("select[name='changType']").val();
        if (changType == "1") {//利率方式
            var newVal = "", instrtType = "";
            var checksVal = $("input[name='checks']");
            for (var i = 0; i < checksVal.length; i++) {
                newVal = checksVal[i].value;
                instrtType = newVal.split('A')[1];
                if (instrtType == interestType && $("#" + newVal).length > 0) {
                    rate.get(newVal).val(value);
                }
            }
        } else if (changType == "2") {//比例方式
            var newVal = "", instrtType = "", account = "";
            var checksVal = $("input[name='checks']");
            for (var i = 0; i < checksVal.length; i++) {
                newVal = checksVal[i].value;
                account = newVal.split('A')[0];
                instrtType = newVal.split('A')[1];
                if (instrtType == interestType && $("#" + newVal).length > 0 && $("#" + account + "O" + instrtType).length > 0) {
                    var oVal = parseFloat($("#" + account + "O" + instrtType).val());
                    // console.log(oVal*(1+parseFloat(value)));
                    // var newInter = bt.money.format(oVal*(1+parseFloat(value)),{scale:decimalLength});
                    // console.log(newInter);
                    rate.get(newVal).val(oVal * (1 + parseFloat(value)));
                }
            }
        }
    };


    /**
     * 资金池回调
     * @param data
     */
    netIdCallback = function (data) {
        // 重新设置资金池的子单位
        var corpPoolChildUrl = basePath + "/basic/business/fcinstrateadjust?getUserCorpPoolChilds&poolId=" + data.id;
        bt.selectType.get("corpId").attr("data-url", corpPoolChildUrl);
    }

    setTimeout(function () {
        form.addrequiredIcon();
    }, 500);
});

function callData(data) {
    $dyncTable();
}

function netChange(id) {
    $dyncTable();
}

function corpChange(id) {
    $dyncTable();
}

function calRate(value, $elem) {
    $soosRate(value, $elem);
}

function poolCallBack(data) {
    $poolCallBack(data);
}

function beforeInit(text, data) {
    if (data.subjectCode) {
        return "(" + data.subjectCode + ")" + data.subjectName;
    }
    return text;
}

function isNotNull(value) {
    if (value == "" || value == null || value == undefined) {
        return false;
    } else {
        return true;
    }

}

/**
 * 资金池回调
 * @param data
 */
function netIdCallback(data) {
    netIdCallback(data);
}
