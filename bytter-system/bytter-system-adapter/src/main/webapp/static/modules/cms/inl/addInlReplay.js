var form, moneyType, dateType, dicts, rateType, moneyfun;
bt.use(["jquery", "form", "btUpload", "moneyType", "dateType", "dicts", "rateType"], function () {
    $ = bt.jquery;
    form = bt.form();
    moneyType = bt.moneyType.init();
    dateType = bt.dateType.init();
    rateType = bt.rateType.init();
    dicts = bt.dicts.init();
    moneyfun = bt.moneyType.getFun()
    var gid = $("#sendAttachmentId").val();
    var repayAttachmentId = $("#repayAttachmentId").val();
    var up = bt.btUpload.init({
        elem: "#sendUploader",
        hasUpload: false,
        hasDelete: false,
        isExpand: true,
        auto: false,
        gid: gid
    });

    var repayUpload = bt.btUpload.init({
        elem: "#repayUploader",
        isExpand: true,
        auto: false,
        gid: repayAttachmentId
    });

    form.on("submit(submit)", function (data) {
        var isBudget = $("#isBudget").val();
        if ("1" == isBudget) {
            if (isNotEmpty($("#tagItemDetail").html())) {
                //判断内部拆借是否挂资金计划
                var inlRepayItem = $("#inlRepayItem").val();
                if (inlRepayItem != null && inlRepayItem != "") {
                    var money = moneyType.get("totalMoney").val();
                    var processMoneys = $("input[name = 'processMoney']");
                    var totalMoney = 0;
                    var itemCode = [];
                    var itemName = [];
                    var processMoney = [];
                    $.each(processMoneys, function (index, item) {
                        totalMoney = moneyfun.add(totalMoney, moneyType.get("processMoney" + index).val());
                        itemCode.push($("input[name = 'itemCode']")[index].value);
                        itemName.push($("input[name = 'itemName']")[index].value);
                        processMoney.push(moneyType.get("processMoney" + index).val());
                    });
                    if (Number(money) != Number(totalMoney)) {
                        layer.alert(bt.lang("资金计划执行金额与拆借金额相等"), {icon: 2});
                        return false;
                    } else {
                        data.field.itemCode = itemCode;
                        data.field.itemName = itemName;
                        data.field.processMoney = processMoney;
                    }
                }
            }
        }
        var action = "/inl/repay/save";
        bt.loading.show();
        data.field.status = this.value;
        data.field.attachmentId = repayUpload.getGroupId();
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
        return false;
    });

    // 资金计划挂接
    renderBudget()
});


//关闭窗口
function doClose() {
    if (bt.navtab) {
        bt.navtab.closeCurrent();
    }
}

/**
 * 为空判断
 * @param value
 * @returns {boolean}
 */
function isEmpty(value) {
    if (value == null || value == "" || value == undefined || value == " ") {
        return true;
    } else {
        return false;
    }
}

/**
 * 非空判断
 * @param value
 * @returns {boolean}
 */
function isNotEmpty(value) {
    return !isEmpty(value);
}

/**
 * 选择利息明细
 */
function selectRepay() {
    /**加载利息预计明细*/
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('cms.button.provideInterest'),basePath + "/cms/provide/cmsprovideinterest/list?sendBillCode="+$("#inlSendBillNo").val()+"&interestID="+$("#inlInterestId").val()+"&repairSign=1");
    }
}

/**
 *加载还款信息
 */
function setSelectInterestData(repayDate,repayMoney,payInterest,id) {
    var totalMoney= moneyfun.add(repayMoney, payInterest);
    moneyType.get("totalMoney").val(totalMoney);
    dateType.get("repayDate").val(repayDate);
    moneyType.get("repayMoney").val(repayMoney);
    moneyType.get("repayInterest").val(payInterest);
    $("#repayMoneyShow").html(moneyType.getFun().format(repayMoney));
    $("#repayInterestShow").html(moneyType.getFun().format(payInterest));
    $("#inlInterestId").val(id);
    /**
     * 还款日期改变重新加载资金计划
     */
    renderBudget();
}

/**
 * 账号选择
 * @param obj
 * @returns {boolean}
 */
function selectAcc(obj) {
    var url;
    var accId;
    var flag = $(obj).data("acc");
    if (flag == "borrowOutAcc") {
        accId = $("#borrowInAccId").val();
    }else{
        accId = $("#borrowOutAccId").val();
    }
    if (isNotEmpty(accId)) {
        url = "/inl/info/selectAcc";
    } else {
        url = "/inl/info/selectAcc2";
    }
    var borrowInCorp = $("#borrowInCorp").val();
    var borrowOutCorp = $("#borrowOutCorp").val();
    if (isEmpty(borrowInCorp) || isEmpty(borrowOutCorp)) {
        layer.alert(bt.lang("请选择拆入，拆出单位"), {icon: 2});
        return false;
    }
    var curId = $("#curId").val();
    if (isEmpty(curId)) {
        layer.alert(bt.lang("请选择币种"), {icon: 2});
        return false;
    }
    url += "?&borrowInCorp=" + borrowInCorp + "&borrowOutCorp=" + borrowOutCorp + "&accId=" + accId + "&curId=" + curId + "&flag=" + flag;
    parent.layer.open({
        title: "选择账号",
        type: 2,
        area: ["50%", "70%"],
        btn: [bt.lang("common.button.confirm"), bt.lang("common.button.close")],
        btnAlign: "c",
        content: basePath + url,
        yes: function (index, layero) {
            //按钮【按钮一】的回调
            var iframeObj = layero.find("iframe")[0];
            var selectData = iframeObj.contentWindow.getSelectDate();
            if (null == selectData) {
                parent.layer.alert(bt.lang("请选择数据"), {icon: 2});
                return;
            }
            /**
             * 业务逻辑处理
             * selectData为选中返回数据
             *
             */
            doBusiness(obj, selectData[0]);

            parent.layer.close(index);
        },
        btn2: function (index, layero) {
            parent.layer.close(index);
        }
    });
}

/**
 * 业务数据设置
 * @param obj
 * @param data
 */
function doBusiness(obj, data) {
    var acc = null;
    if (isNotEmpty(data.bankAcc)) {
        acc = data.bankAcc;
    }else {
        acc = data.blnctrAcc;
    }
    if ($(obj).data("acc") == "borrowOutAcc") {
        $("#borrowOutAccId").val(data.id);
        $("#borrowOutAcc").val(acc);
    } else {
        $("#borrowInAccId").val(data.id);
        $("#borrowInAcc").val(acc);
    }
}

/**
 * 资金计划挂接
 */
function renderBudget(){
    //判断支付结算是否挂接资金计划
    var isBudget = $("#isBudget").val();
    if(isBudget == "1"){
        var inlRepayItem = $("#inlRepayItem").val();
        // 跳过发放时才允许挂接资金计划
        if (inlRepayItem != null && inlRepayItem != "") {
            genOtherHtml();
        } else {
            $("#tagItemDetail").html("");
        }
    }
}



/**
 * 生成公共的属性
 * @return
 */
function genOtherHtml(){
    var itemCode = $("#dfInfoItem").val();
    var defItemCode = $("#defItemCode").val();
    var processDate = dateType.get("repayDate").val();
    var corpId = $("#borrowInCorp").val();
    var curId = $("#curId").val();//币别
    var url = "";
    var param = {};
    if (isEmpty($("#id").val())) {
        param = {
            corpId:corpId,
            itemCode:itemCode,
            defItemCode:defItemCode,
            processDate:processDate,
            curId:curId,
            type:'select',
            date:(new Date()).getTime()
        };
        url = "/budget/fbsBudgetProcess/getItemDetail";
    } else {
        url = "/budget/fbsBudgetProcess/getEditItemDetail";
        param = {
            corpId: corpId,
            itemCode: itemCode,
            billNo: $("#billNo").val(),
            billId: $("#id").val(),
            businessName: $("#businessName").val(),
            processSysname: $("#processSysname").val(),
            defItemCode: defItemCode,
            processDate: processDate,
            curId: curId,
            type: 'select',
            date: (new Date()).getTime()
        };
    }
    $.ajax({
        url: basePath + url,
        method: 'POST',
        data: param,
        async: true,
        success: function (json) {
            $("#tagItemDetail").html("");
            $("#tagItemDetail").html(json);
            bt.form().render();
        }
    });
}
