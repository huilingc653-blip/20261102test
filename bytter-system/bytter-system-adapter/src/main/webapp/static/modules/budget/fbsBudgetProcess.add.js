bt.use(['form', 'layedit', 'selectTree', 'select', 'dateType', 'moneyType', 'itemType', 'dicts', 'externalCorpBankAcc', 'selectType','corp'], function () {
    var $ = bt.jquery, form = bt.form();
    var dicts = bt.dicts.init();
    bt.externalCorpBankAcc.init();
    bt.externalCorpBankAccParams = function () {
        externalCorpBankAcc.set({
            "corpId": bt.corp.get("corpId").val() ? bt.corp.get("corpId").val() : "0",
            type: "2"
        });
        return true;
    }

    var dt = bt.dateType.init();
    $mt = bt.moneyType.init();

    //币种回调函数
    bt.curIdCallback = function (data) {
        $genOtherHtml();
    };
    var selectType = bt.selectType.init();

    //单位回调函数
    bt.corpCallback = function (id) {
        $isCorpForCollectFlag(id);
    };
    bt.corp.init();

    //查询
    $("#submit").click(function(){
        save("A");
    });
    $("#temp").click(function () {
        save("T");
    });

    function save(type) {
        if (!form.verifyFun(".verify")) {
            return;
        }
        var jsonArr = getFormData();
        $.ajax({
            url: basePath + "/budget/fbsBudgetProcess/save/" + type,
            method: 'POST',
            cache: false,
            data: JSON.stringify(jsonArr),//将对象序列化成JSON字符串
            dataType: "json",
            contentType: 'application/json;charset=utf-8', //设置请求头信息
            success: function (result) {
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (bt.navtab) {
                    bt.navtab.refreshCurrent();
                    bt.navtab.refreshParent();
                }
            }
        });
    }

    function getFormData() {
        var fieldData = [];
        var trs = $("#budgetProcessDataTable").find("div[row]");
        var innerSign = dicts.get("innerSign").val();
        // 遍历行数据
        trs.each(function (index, tr) {
            var obj = new Object();
            var rowIndex = $(tr).attr("row");
            obj.itemCode = bt.itemType.get("itemCode" + rowIndex).val();
            obj.itemName = $("#itemName" + rowIndex).val();
            obj.processMoney = $mt.get("processMoney" + rowIndex).val();
            obj.exchangeRate = $mt.get("exchangeRate" + rowIndex).val();
            obj.processCurMoney = $mt.get("processCurMoney" + rowIndex).val();
            obj.corpId = bt.corp.get("corpId").val();
            obj.processDate = dt.get("processDate").val();
            obj.billNo = $("#billNo").val();
            obj.payeeAccName = $("#payeeAccName").val();
            obj.innerSign = innerSign;
            obj.curId =  $("select[name='curId']").val();
            obj.rmk = $("#rmk").val();
            fieldData.push(obj);
        });
        return fieldData;
    }


    /**
     * 生成公共的属性
     * @return
     */
    $genOtherHtml = function () {
        var id = $('#id').val();
        var corpId = bt.corp.get("corpId").val();
        var defItemCode = $("#defItemCode").val();
        var processDate = bt.dateType.get("processDate").val();
        var curId = selectType.get("curId").val();
        var url = basePath + "/budget/fbsBudgetProcess/getItemDetail";
        var param = {
            id: id,
            corpId: corpId,
            curId: curId,
            itemCode: "",
            defItemCode: defItemCode,
            processDate: processDate,
            type: 'select',
            date: (new Date()).getTime()
        };
        $.ajax({
            url: url, method: 'POST', data: param, async: true, success: function (json) {
                $("#tagItemDetail").html("");
                $("#tagItemDetail").html(json);
                form.render();
            }
        });
    };

    //客商选择
    bt.payeeAccCallback = function (data, dom) {
        $("#payeeAccName").val(data.name);
    }

    //内转切换
    $changSign = function (sign) {
        if (sign == 1) {
            $("#corp1").attr("style", "display:none");
            $("#corp2").attr("style", "display:block");
        } else {
            $("#corp1").attr("style", "display:block");
            $("#corp2").attr("style", "display:none");
        }
        form.render();
        form.renderIcon();
    }

    /**
     * 查询是否是计划汇总单位
     * @return
     */
    $isCorpForCollectFlag = function (id) {
        var url = basePath + "/budget/fbsBudgetProcess/isCorpForCollectFlag/" + id;
        if (id && id != "") {
            $.ajax({
                url: url, method: 'GET', dataType: 'json',
                success: function (data) {
                    if (data && data.attributes != null) {
                        $genOtherHtml();
                    }
                }
            });
        }
    };

    /**
     * 是否内转回调
     * @param data
     */
    bt.innerSign = function (data) {
        $changSign(data.value);
    }

    resetCallback = function () {
        $changSign(0);
    }
});

/**
 * 预算执行输入金额汇总后的回调
 * @param data  汇总金额
 */
function sumMoneyCallBack(data) {
    $mt.get("processMoney").val(data);
}

function clickDate(value, $elem) {
    $genOtherHtml();
}



