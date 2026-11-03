var rateType, moneyType, dateType, moneyfun;
bt.use(["jquery", "form", "dateType", "rateType", "moneyType","btUpload"], function () {
    $ = bt.jquery;
    form = bt.form();
    rateType = bt.rateType.init();//利率
    moneyType = bt.moneyType.init();//金额
    dateType = bt.dateType.init();//日期时间
    moneyfun = bt.moneyType.getFun();

    var gid = $("#gid").val();
    var up = bt.btUpload.init({
        elem: "#uploader",
        isExpand: true,
        auto: false,
        gid: gid
    });

    /**
     * 资金计划挂接
     */
    renderBudget();

    form.on("submit(submit)", function (data) {
        var repayMoney, interest, serviceCharge;
        var surplusMoney = $("#surplusMoney").val();
        var allRepayMoney = $("#allRepayMoney").val();
        repayMoney = moneyType.get("repayMoney").val();
        interest = moneyType.get("interest").val();
        serviceCharge = moneyType.get("serviceCharge").val();
        if (isEmpty(repayMoney) && isEmpty(interest) && isEmpty(serviceCharge)) {
            layer.alert(bt.lang("df.input.one.item"), {icon: 2})
            return false;
        }
        if (isNotEmpty(repayMoney)) {
            if (Number(surplusMoney) - Number(allRepayMoney) < repayMoney) {
                layer.alert(bt.lang("df.repayMoney.less"), {icon: 2})
                return false;
            }
        }
        var isBudget = $("#isBudget").val();
        if ("1" == isBudget) {
            //判断直融业务是否挂资金计划
            if (isNotEmpty($("#tagItemDetail").html())) {
                var dfRepayItem = $("#dfRepayItem").val();
                if(dfRepayItem != null && dfRepayItem != ""){
                    var repayMoney = moneyType.get("repayMoney").val();
                    var serviceCharge = moneyType.get("serviceCharge").val();
                    var interest = moneyType.get("interest").val();
                    var repayTotal = moneyfun.add(moneyfun.add(repayMoney,serviceCharge),interest)
                    var processMoneys = $("input[name = 'processMoney']");
                    var totalMoney = 0;
                    var itemCode = [];
                    var itemName = [];
                    var processMoney = [];
                    $.each(processMoneys, function (index,item) {
                        totalMoney = moneyfun.add(totalMoney, moneyType.get("processMoney" + index).val());
                        itemCode.push($("input[name = 'itemCode']")[index].value);
                        itemName.push($("input[name = 'itemName']")[index].value);
                        processMoney.push(moneyType.get("processMoney" + index).val());
                    });
                    if (Number(repayTotal) != Number(totalMoney)) {
                        layer.alert(bt.lang("df.repay.isBudget.money.check"), {icon: 2});
                        return false;
                    } else {
                        data.field.itemCode = itemCode;
                        data.field.itemName = itemName;
                        data.field.processMoney = processMoney;
                    }
                }
            }
        }
        data.field.status = this.value;
        data.field.uploadId = up.getGroupId();
        layer.confirm(bt.lang("df.repay.interest.confirm"), {icon: 3}, function(index){
            var action = "/cms/dfDirectFinanceRepay/save";
            bt.loading.show();
            jQuery.ajaxSettings.traditional = true;
            $.post(basePath + action, data.field, function (data) {
                parent.layer ? parent.layer.alert(data.msg, {icon: data.success ? 1 : 2}) : layer.alert(data.msg, {icon: data.success ? 1 : 2});
                if (data.success) {
                    if (!bt.navtab) {
                        bt.navtab.refreshParentTable();
                    } else {
                        bt.navtab.closeCurrent();
                    }
                } else {
                    bt.loading.hide();
                }
            }, 'JSON');
            layer.close(index);
        });
        return false;
    });
});


//关闭窗口
function doClose() {
    if (bt.navtab) {
        bt.navtab.closeCurrent();
    }
}

function isEmpty(value) {
    if (null == value || "" == value || " " == value || undefined == value) {
        return true;
    } else {
        return false;
    }
}

function isNotEmpty(value) {
    return  !isEmpty(value)
}



/**
 * 资金计划挂接
 */
function renderBudget(){
    //判断支付结算是否挂接资金计划
    var isBudget = $("#isBudget").val();
    if(isBudget == "1"){
        //判断银行借款是否挂资金计划
        var dfRepayItem = $("#dfRepayItem").val();
        if(dfRepayItem != null && dfRepayItem != ""){
            genOtherHtml();
        }else{
            $("#tagItemDetail").html("");
        }
    }
}


/**
 * 生成公共的属性
 * @return
 */
function genOtherHtml(){
    var itemCode = $("#dfRepayItem").val();
    var defItemCode = $("#defItemCode").val();
    var processDate = dateType.get("repayDate").val();
    var corpId = $("#corpId").val();
    var curId = $("#curId").val();//币别
    var param = {
        corpId: corpId,
        itemCode: itemCode,
        billNo: $("#billCode").val(),
        billId: $("#id").val(),
        businessName: $("#businessName").val(),
        processSysname: $("#processSysname").val(),
        defItemCode: defItemCode,
        processDate: processDate,
        curId: curId,
        type: 'select',
        date: (new Date()).getTime()
    };
    $.ajax({
        url: basePath + "/budget/fbsBudgetProcess/getEditItemDetail",
        method: 'POST',
        data: param,
        async:true,
        success: function (json) {
            $("#tagItemDetail").html("");
            $("#tagItemDetail").html(json);
            bt.form().render();
        }
    });
}



/**
 * 日期改变重置资金计划挂接
 */
function repayDateCallBack() {
    renderBudget();
}