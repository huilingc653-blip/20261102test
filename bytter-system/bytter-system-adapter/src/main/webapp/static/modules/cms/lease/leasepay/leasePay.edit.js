bt.use(['form', 'layedit', 'dateType', 'moneyType', 'dicts', 'btUpload', 'selectType', 'externalCorpBankAcc'], function () {
    var $ = bt.jquery, form = bt.form();
    fun = bt.moneyType.getFun();
    dateTypes = bt.dateType.init();//时间
    moneyTypes = bt.moneyType.init();//金额
    dicts = bt.dicts.init();//字典
    selectTypes = bt.selectType.init();//下拉框
    initBankAccTable();
    /* var gid = $("#annexName").val();
     var up = bt.btUpload.init({
         elem: "#uploader",
         isExpand: true,
         auto: false,
         gid: gid
     });*/

    var externalCorpBankAcc = bt.externalCorpBankAcc.init();
    bt.externalCorpBankAccParams = function () {
        //type 空是所有 0客商 1单位
        externalCorpBankAcc.set({
            "corpId": $("#corpId").val() ? $("#corpId").val() : "0",
        });
        return true;
    }

    //供应商选择
    bt.externalCorpCallback = function (data, dom) {
        if (typeof data.netId === "undefined") {
            // 选择客商
            $("#onlinePaymentAcc").val(data.externalAcc);
            $("#onlineAccName").val(data.name);
            $("#onlineBankName").val(data.bank);
            $("#onlinePaymentAccId").val(data.id);
        } else {
            // 选择单位
            $("#onlinePaymentAcc").val(data.bankAcc);
            $("#onlineAccName").val(data.accName);
            $("#onlineBankName").val(data.bankName);
            $("#onlinePaymentAccId").val(data.id);
        }
    };

    //监听提交
    form.on('submit', function (data) {
        var nm = $(this).attr("id");
        var saveType = "0";
        if (nm == "submit") {
            saveType = "1";
        }

        //var action = "/cms/provide/loanrepay/tempOrSubmit?sign=" + $("#downSign").val() + "&saveType=" + saveType;
        var action = "/cms/lease/leasepay/tempOrSubmit?sign=" + $("#downSign").val() + "&saveType=" + saveType;
        /* data.field.uploadId = up.getGroupId();*/
        var tempOrSubmitSign = $("#tempOrSubmitSign").val();
        data.field.tempOrSubmitSign = tempOrSubmitSign;
        submitForm(action);
        /* parent.layer.confirm(bt.lang('table.cmsProvideLoanRepay.repayAccrualMsgOk'), function () {
             if ($("#overdueSign").val() == "1") {
                 parent.layer.confirm(bt.lang('table.cmsProvideLoanRepay.alert.info'), function () {
                     submitForm(action);
                 });
             } else {
                 submitForm(action);
             }
         });*/
        return false;
    });
});

function submitForm(action) {
    bt.loading.show();
    $.post(basePath + action, $("#inputForm").serialize(), function (result) {
        parent.layer.msg(result.msg, {icon: result.success ? 1 : 2});
        if (result.success) {
            if (!bt.navtab) {
                bt.navtab.refreshCurrentTable();
                parent.layer ? parent.layer.closeAll() : layer.closeAll();
            } else {
                bt.navtab.closeCurrent();
                bt.navtab.refreshCurrentTable();
                var webObj = bt.navtab.getParentDom();
                webObj.frames['repayApp'].$.fn.dataTable.tables({api: true}).draw();
                webObj.frames['repayFollow'].$.fn.dataTable.tables({api: true}).draw();
                bt.navtab.closeCurrent();
            }
        } else {
            bt.loading.hide();
        }
    }, 'JSON');
}

/**选择还款计划后执行的方法*/
function setSelectInterestData(payDate, money, interest, id) {
    dateTypes.get("payDate").val(payDate);
    moneyTypes.get("money").val(money);
    moneyTypes.get("interest").val(interest);
    bt.jquery("#leasePlanDelId").val(id);
    countOnlineMoney();

}

/**设置自动计算在线金额*/
function countOnlineMoney() {
   /* if ("1" == $("#onlineSign").val()) {//在线*/
        var interest = fun.toNumber(moneyTypes.get("interest").val());
        var money = fun.toNumber(moneyTypes.get("money").val());
        moneyTypes.get("leaseMoney").val(fun.add(interest + money));
   /* }*/
}

/**选择离线帐号的下拉列表初始化*/
function initBankAccTable() {
    //银行账号下拉表格初始化
    var corpId = $("#corpId").val();
    $.post(basePath + "/cms/provide/loanrepay/queryPaymentAccListByCoprIdCount", {"corpId": corpId}, function (result) {
        var htl = "";
       /* if ("0" == result) {//没有帐号
            htl = bt.lang('table.cmsProvideLoanRepay.onlinePaymentAccMsg');
        }*/
        $("#accMsg").html(htl);
    }, 'JSON');
}

/**加载还款计划*/
function repayLan() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('cms.button.repayPlan'), basePath + "/cms/basedata/repaymentschedule/toRepayPlanSave?billCode=" + $("#sendCode").val());
    }
}

/**加载利息预计明细*/
function repayInterest() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('cms.button.leaseCapitalPlan.view'), basePath + "/cms/lease/leasecapitalplan/getPlanDelList?leaseId=" + $("#leaseId").val() + "&leasePlanDelId=" + $("#leasePlanDelId").val() + "&showSign=1");
    }
}

