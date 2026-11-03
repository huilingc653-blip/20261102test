bt.use(['form', 'layedit', 'dateType', 'moneyType', 'dicts', 'btUpload', 'selectType', 'externalCorpBankAcc', 'rateType'], function () {
    var $ = bt.jquery, form = bt.form();
    fun = bt.moneyType.getFun();
    dateTypes = bt.dateType.init();//时间
    moneyTypes = bt.moneyType.init();//金额
    dicts = bt.dicts.init();//字典
    selectTypes = bt.selectType.init();//下拉框
    rateType = bt.rateType.init();
    initBankAccTable();

    var externalCorpBankAcc = bt.externalCorpBankAcc.init();
    bt.externalCorpBankAccParams = function () {
        //type 空是所有 0客商 1单位
        externalCorpBankAcc.set({
            "corpId": $("#corpId").val() ? $("#corpId").val() : "0",
            type: "0"
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
            $("#payee").val(data.name);
        } else {
            // 选择单位
            $("#onlinePaymentAcc").val(data.bankAcc);
            $("#onlineAccName").val(data.accName);
            $("#onlineBankName").val(data.bankName);
            $("#onlinePaymentAccId").val(data.id);
            $("#payee").val(data.accName);
        }
    };

    //监听提交
    form.on('submit', function (data) {
        var nm = $(this).attr("id");
        var saveType = "0";
        if (nm == "submit") {
            saveType = "1";
        }
        var action = "/cms/bond/cmsbondpayinterest/tempOrSubmit?sign=" + $("#downSign").val() + "&saveType=" + saveType;
        /* data.field.uploadId = up.getGroupId();*/
        var tempOrSubmitSign = $("#tempOrSubmitSign").val();
        data.field.tempOrSubmitSign = tempOrSubmitSign;
        submitForm(action);
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
function setSelectInterestData(issueId, bondIssueNum, issueMoney, unpaidMoney, planRepayDate, repayMoney, id) {
    $("#bondIssueNum").val(bondIssueNum);
    moneyTypes.get("issueMoney").val(issueMoney);
    moneyTypes.get("unpaidMoney").val(unpaidMoney);
    dateTypes.get("payDate").val(planRepayDate);
    moneyTypes.get("repayMoney").val(repayMoney);
    $("#issueId").val(issueId);
    bt.jquery("#planDtlId").val(id);
}

/**选择离线帐号的下拉列表初始化*/
function initBankAccTable() {
    //银行账号下拉表格初始化
    var corpId = $("#corpId").val();
    $.post(basePath + "/cms/provide/loanrepay/queryPaymentAccListByCoprIdCount", {"corpId": corpId}, function (result) {
        var htl = "";
        $("#accMsg").html(htl);
    }, 'JSON');
}

/**加载利息预计明细*/
function repayInterest() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('cms.button.cmsBondPayInterest.view'), basePath + "/cms/bond/cmsbondpayinterest/getPlanDtlList?registerId=" + $("#registerId").val() + "&planDtlId=" + $("#planDtlId").val() + "&showSign=1");
    }
}

/**切换发行编号*/
function typeCallBack(data) {
    $.ajax({
        type: "GET",
        url: basePath + "/cms/bond/issue/getInfo/" + data.id,
        dataType: "json",
        success: function (result) {
            moneyTypes.get("issueMoney").val(result.issueMoney);
            moneyTypes.get("unpaidMoney").val(result.unpaidMoney);
            selectTypes.get("fixPriceBase").val(result.fixPriceBase);
            rateType.get("faceValueRate").val(result.faceValueRate);
            $("#issueId").val(result.id)
        }
    });
}

