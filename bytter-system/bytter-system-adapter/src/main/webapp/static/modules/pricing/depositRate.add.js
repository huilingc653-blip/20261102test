var dict, form, rateType;
bt.use(['form', 'layedit', 'dicts', 'moneyType', 'selectType', 'select', 'rateType', 'dateType'], function () {
    var $ = bt.jquery, moneyFun = bt.moneyType.getFun();
    form = bt.form();
    dict = bt.dicts.init();
    bt.selectType.init();
    rateType = bt.rateType.init();
    bt.dateType.init();

    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "") {
            return true;
        }
        return false;
    }

    validateForm = function () {
        var period = $("#period").val().trim();
        if (period !== "" && period === "0") {
            layer.alert(bt.lang("cms.TbCreditPeriod.period.isnotnulll"), {icon: 5});
            return false;
        }
        return true;
    }


    // 监听提交
    form.on('submit', function (data) {
        var action = "/pricing/depositRate/interestRatePreserve/save";
        // if(!validateForm()) return false;
        var nm = $(this).attr("lay-filter");
        if (nm == "submit") {
            data.field.saveType = "1";
        } else if (nm == "temp") {
            data.field.saveType = "0";
        }
        $.post(basePath + action, data.field, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                if (bt.navtab) {
                    bt.navtab.closeCurrent();
                }
            }
        }, 'JSON');
        return false;
    });
});

/**
 * 类型编码变更
 */
codeChange = function (data) {
    if (!bt.empty(data)) {
        var str = data.name;
        var index = str.indexOf(")");
        var rateTypeName = str.substr(index + 1, str.length);
        $("#rateTypeName").val(rateTypeName);
        var depositClassName = bt.dict("pricing.deposit.type", data.depositclass);
        $("#depositClass").val(data.depositclass);
        $("#depositClassName").val(depositClassName);
        $("#depositRateType").val(data.id);
        var depositAccSign = "";
        if (data.depositaccsign == null) {
            depositAccSign = bt.dict("pricing.depAccSign", 2);//为空时默认为资金池内部账户
        } else {
            depositAccSign = bt.dict("pricing.depAccSign", data.depositaccsign);
        }
        $("#depositAccSign").val(depositAccSign);

        if (data.depositclass == "04") {//活期时显示控制
            $(".HqRateShow").show();
            $(".rateShow").hide();
            dict.get("referenceRateType").attr("data-required", true);
            $("input[data-id='rateShow1']").attr("lay-verify", "required");
            $("input[data-id='rate']").removeAttr("lay-verify", "required");
            var html = '<td class="textRight"><span class="pl"><span class="required-icon">*</span></span>' + bt.lang("table.LoanRate.Upper.limit.of.floating.range") + '(%)</td>\n' +
                '<td><input type="text" id="rateUpLimit" data-name="rateUpLimit" lay-verify="required" data-max="100" data-min="-100" data-minus="true"  data-decimal="2" class="layui-input bt-biz-moneyType" ></td>\n' +
                '<td class="textRight" ><span class="pl"><span class="required-icon">*</span></span>' + bt.lang("table.LoanRate.Lower.limit.of.floating.range") + '(%)</td>\n' +
                '<td><input  type="text" data-moneyendid="rateUpLimit" data-name="rateDownLimit"  lay-verify="required" data-min="-100" data-decimal="2" data-minus="true" class="layui-input bt-biz-moneyType" ></td>';
            $(".floatRateShow").html("");
            $(".floatRateShow").prepend(html);
            $(".floatRateShow").show();
        } else {
            $(".HqRateShow").hide();
            $(".rateShow").show();
            $(".floatRateShow").html("");
            dict.get("referenceRateType").attr("data-required", false);
            $("input[data-id='rateShow1']").removeAttr("lay-verify");
            $("input[data-id='rate']").attr("lay-verify", "required");
        }
    }
    bt.form().render();
    bt.moneyType.init();
}

//利率类型变化
function setRatShow() {
    var rateType = dict.get("referenceRateType").val();
    var html1 = '<td class="textRight"><span class="pl"><span class="required-icon">*</span></span>' + bt.lang("table.LoanRate.Upper.limit.of.floating.range") + '(%)</td>\n' +
        '<td><input type="text" id="rateUpLimit" data-name="rateUpLimit" lay-verify="required" data-max="100" data-min="-100" data-minus="true"  data-decimal="2" class="layui-input bt-biz-moneyType" ></td>\n' +
        '<td class="textRight" ><span class="pl"><span class="required-icon">*</span></span>' + bt.lang("table.LoanRate.Lower.limit.of.floating.range") + '(%)</td>\n' +
        '<td><input  type="text" data-moneyendid="rateUpLimit" data-name="rateDownLimit"  lay-verify="required" data-min="-100" data-decimal="2" data-minus="true" class="layui-input bt-biz-moneyType" ></td>';
    var html2 = '<td class="textRight"><span class="pl"><span class="required-icon">*</span></span>' + bt.lang("table.LoanRate.Upper.limit.of.floating.range") + '(BP)</td>\n' +
        '<td><input type="text" id="rateUpLimit" data-name="rateUpLimit" lay-verify="required" data-max="100" data-min="-100" data-minus="true"  data-decimal="2" class="layui-input bt-biz-moneyType" ></td>\n' +
        '<td class="textRight" ><span class="pl"><span class="required-icon">*</span></span>' + bt.lang("table.LoanRate.Lower.limit.of.floating.range") + '(BP)</td>\n' +
        '<td><input  type="text" data-moneyendid="rateUpLimit" data-name="rateDownLimit"  lay-verify="required" data-min="-100" data-decimal="2" data-minus="true" class="layui-input bt-biz-moneyType" ></td>';
    if (rateType == "1") {//基础利率类型
        $(".rateShow2").show();//显示基础利率输入框
        $(".rateShow1").hide();//隐藏基准利率输入框
        $("input[data-id='rateShow2']").attr("lay-verify", "required");
        $("input[data-id='rateShow1']").removeAttr("lay-verify");
        $(".floatRateShow").html("");
        $(".floatRateShow").prepend(html2);
    } else {//基准利率类型
        $(".rateShow1").show();//显示基准利率输入框
        $(".rateShow2").hide();//隐藏基础利率输入框
        $("input[data-id='rateShow1']").attr("lay-verify", "required");
        $("input[data-id='rateShow2']").removeAttr("lay-verify");
        $(".floatRateShow").html("");
        $(".floatRateShow").prepend(html1);
    }
    bt.form().render();
    bt.moneyType.init();
}

//关闭当前页面
function closecurrent() {
    if (!bt.navtab) {
        parent.layer ? parent.layer.closeAll() : layer.closeAll();
    } else {
        bt.navtab.closeCurrent();
    }
    return false;
}

//选择利率类型前回调
rateTypeBefore = function (data) {
    if (data && data.length) {
        for (var i = 0; i < data.length; i++) {
            data[i].name = "(" + data[i].code + ")" + data[i].name;
        }
    }
    return data
    // var show = "(" + arguments.code + ")" + arguments.name;
    //return show;
}
