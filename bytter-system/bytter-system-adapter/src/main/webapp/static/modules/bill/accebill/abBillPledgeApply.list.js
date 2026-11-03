var corp, up, fromObj, dicts, form, fromObj, externalCorpBankAcc;
var idsValArray = [];
bt.use(['form', 'layedit','mulselect', 'select', 'datatable', 'moneyType', 'moneynum', 'dateType', 'btdate', 'jquery', 'corp', 'selectTable', 'dateForm', 'bank', 'bankInfo', 'dicts', 'externalCorpBankAcc', 'element', 'accountTable', 'btImport'], function () {
    $ = bt.jquery, form = bt.form();
    fromObj = form;
    corp = bt.corp.init();
    dicts = bt.dicts.init();
    bt.bankInfo.init();
    bt.dateType.init();
    bt.mulselect.init();
    externalCorpBankAcc = bt.externalCorpBankAcc.init();
    externalCorpBankAcc.set({"type": 4});
    bt.accountTable.init({urldata: {accountType: '2,3'}});
    bt.moneyType.init();
    bt.bank.init({
        elem: '.bank'
    })
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/abbillpledgeapply/datatable"
        },
        buttons: [
            {
                text: bt.lang('bill.accebill.update.pledgeApply'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    submitBatch(data);
                }
            },
            {
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            },/*{
                text: bt.lang('common.button.exports'),
                action: function ( e, dt, node, config ) {

                }
            },*/{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
            {data: "billTypeName", bSortable: true},
            {data: "billFlagName", bSortable: true},
            {data: "applyCorpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.applyCorpCode + ')' + full.applyCorpName;
                }
            },
            {
                data: "corpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.corpCode + ')' + full.corpName;
                }
            },
            {data: "busCorpName", bSortable: true},
            {data: "bankTypeName", bSortable: true},
            {data: "busPersonnel", bSortable: true},
            {data: "payer", bSortable: true},
            {data: "acceptor", bSortable: true},
            {data: "origNo", bSortable: true},
            {
                data: "billMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.billMoney);
                }
            },
            {data: "startDate", bSortable: true},
            {data: "endDate", bSortable: true},
            {data: "payee", bSortable: true},
            {data: "cnAccrecords", bSortable: true},
            {data: "statusName", bSortable: true},
            {data: "enterStockName", bSortable: true},
            {data: "briefIdName", bSortable: true},
            {data: "billSourceName", bSortable: true},
            {data: "newStmIssuofpprIndName", bSortable: true},
            {data: "blgrdbagcrclPrmtIndName", bSortable: true},
            {data: "sbbllRngStrtSn", bSortable: true},
            {data: "sbbllRngEndSn", bSortable: true},
            {data: "blStdAmt", bSortable: true,
                render: function ( data, type, full, meta ) {
                    if(full.newStmIssuofpprInd == 1){
                        return full.blStdAmt;
                    }else {
                        return "";
                    }
                }},
            {data: "socCode", bSortable: true},
            {
                data: "transMoney", bSortable: true, className: 'textRight', width: 120,
                render: function (data, type, full, meta) {
                    if (full.newStmIssuofpprInd === '1' && full.blgrdbagcrclPrmtInd === '1') {
                        return '<input lay-verify="required" style="width: 100px" class="layui-input bt-input-inline bt-biz-moneyType trans-money-input" ' +
                            'value="'+bt.money.format(full.transMoney)+'" type="text" data-decimal="2" data-min="0.01" data-max="' + full.transMoney + '"' +
                            'data-id="transMoney_'+full.accId+'" data-key="'+full.accId+'" data-name="transMoney_'+full.accId+'" data-biz-name="transMoney_'+full.accId+'" />';
                    }
                    return bt.money.format(full.transMoney);
                }
            },
            {
                data: "attachmentNum",
                bSortable: false,
                className: 'center',
                render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="attachmentView(this)">' + bt.lang("bill.accebill.attachment.num", [(full.attachmentNum || 0)]) + '</a>';
                    return html;
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';

                    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.accId+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    return html;
                }
            }
        ],
        /*fnDrawCallback: function () {
            idsValArray = new Array();
            setSumMoney();
        },*/
        btClickRowCallback: function (data) {
            setVerify();
            setExternalCorpBankAccCorpId();
            opIdsValArray(data);
        },
        btDrawCallback: function (setting) {
            bt.moneyType.init();
            idsValArray = []
            setSumMoney();
        },
        fixedColumns: {
            rightColumns: 3
        },
        summary: {
            pageSum: true,
            selectSum: true,
            sumList: [
                {
                    colNum: '10',
                    dataName: 'billMoney'
                }
                ,
                {
                    colNum: '25',
                    dataName: 'transMoney'
                }
            ]
        }
    });

    bt.btdate.init({
        elem: "#startDate",
        startname: "ged_startDate",
        endname: "led_startDate"
    });

    bt.btdate.init({
        elem: "#endDate",
        startname: "ged_endDate",
        endname: "led_endDate"
    });

    //供应商选择
    bt.externalCorpCallback = function (data, dom) {
//    	console.log(data);
        $("#payeeAcc").val(data.externalAcc);
        $("#payeeAccName").val(data.name);
        $("#payeeBankCode").val(data.bankSourceCode);
        $("#payeeBank").val(data.bank);
    }

    bt.bailCorpCallback = function (data, dom) {
        $("#payeeAccName").val(data.name);
        $("#payeeAcc").val(data.externalAcc);
        $("#payeeBankCode").val(data.bankSourceCode);
        $("#payeeBank").val(data.bank);
    }

    //银行选择
    bt.bailBankInfoCallback = function (data, dom) {
        $("#payeeAccName").val(data.bankName);
        $("#payeeAcc").val("0");
        $("#payeeBankCode").val(data.bankCode);
        $("#payeeBank").val(data.bankName);
    }
    //开户行选择
    bt.bankinfoCallback = function (data, dom) {
        $("#payeeBankCode").val(data.bankCode);
        $("#payeeBank").val(data.bankName);
    }
    corpCallback("");

    //导入初始化
    importExcel = function () {
        //证书签名
        var signDataMsg = signData("质押申请导入", 9);
        if (signDataMsg != "") {
            bt.loading.hide();
            layer.alert(bt.lang(signDataMsg), {icon: 2});
            return false;
        }
        up = bt.btImport.init({
            code: "512880455c5dcd90015c5dcdda790L02",
            titleRows: 0,
            width: "90%",
            height: "90%",
            headRows: 1,
            autoCloseWindown:false,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                bt.loading.show();
                $.ajax({
                    type: "POST",
                    url: basePath + "/bill/accebill/abbillpledgeapply/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(select),
                    success: function (result) {
                        bt.loading.hide();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        if (result.success) {
                            up.closeWindown();
                            bt.datatable.refreshCurrentPage();
                        }
                    },
                    error:function(){
                        bt.loading.hide();
                    }
                });
            }
        });
    }
});

// 表格输入框离开事件，更新表格汇总功能
var timeId = null
$("#appTable").on("blur",".trans-money-input",function () {
    var that = this
    if(timeId){
        clearTimeout(timeId);
        timeId = null
    }
    timeId = setTimeout(function () {
        var key = $(that).data("key");
        var tableData = $("#appTable").DataTable().rows().data()
        if(key && tableData && tableData.length){
            for (var i = 0; i < tableData.length; i++) {
                if(tableData[i].accId === key){
                    tableData[i].transMoney = $("#transMoney_"+key).val();
                    break
                }
            }
        }
        bt.datatable.updateSummary("#appTable");
    },50)

})

function transMoneyView(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    // console.log(data);
}
//选择出票人账户回调方法
function accountPayeeCallBack(data) {
    var $ = bt.jquery;
    $("#pledgeeName").val(data.accName);//【出票人以户名为准】
    $("#pledgeeAcc").val(data.bankAcc);
    $("#pledgeeBankName").val(data.bankName);
    $("#pledgeeBankNo").val(data.bankCode);
}


//所属库存改变时改变界面显示
function change(data) {
    var enterStock = bt.dicts.init().get("enterStock").val();
//	datatable.draw();
    bt.datatable.refreshCurrentPage();
    if (enterStock == 11) {
        bt.corp.init().get("corpId").setUrl("net");
        dicts.get("interiorApply").attr({"data-disabledValue": "0"});
        dicts.get("interiorApply").val(1);
        $("#bt-biz-externalCorpBankAcc").hide();
        $("#corpDiv").show();
        $("#corpId").show();
        $("#pledgeeAccDiv").show();
        $("#pledgeeBankNoDiv").show();
        $("#pledgeeBankNameDiv").show();
        $("#pledgeeDiv").show();
        $("#payeeAccNameDiv").hide();
        $("#payeeAccDiv").hide();
        $("#selPayeeBankButDiv").hide();
        $("#payeeBankCodeDiv").hide();
        $("#payeeBankDiv").hide();
        $("#pledgeeNameDiv").hide();
        $("#pledgeeBankAccDiv").hide();
        $("#pledgeeAcc").val("");
        $("#pledgeeName").val("");
        $("#pledgeeBankNo").val("");
        $("#pledgeeBankName").val("");
    } else if (enterStock == 12) {
        dicts.get("interiorApply").attr({"data-disabledValue": "1"});
        dicts.get("interiorApply").val(0);
        $("#bt-biz-externalCorpBankAcc").hide();
        $("#corpDiv").hide();
        $("#corpId").hide();
        $("#pledgeeAccDiv").hide();
        $("#pledgeeBankNoDiv").hide();
        $("#pledgeeBankNameDiv").hide();
        $("#pledgeeDiv").hide();
        $("#payeeAccNameDiv").show();
        $("#payeeAccDiv").show();
        $("#selPayeeBankButDiv").hide();
        $("#payeeBankCodeDiv").show();
        $("#payeeBankDiv").show();
        $("#pledgeeNameDiv").show();
        $("#pledgeeBankAccDiv").show();
    } else {
        bt.corp.init().get("corpId").setUrl("corp");
        dicts.get("interiorApply").attr({"data-disabledValue": ""});
        dicts.get("interiorApply").val(1);
        $("#bt-biz-externalCorpBankAcc").hide();
        $("#corpDiv").show();
        $("#corpId").show();
        $("#pledgeeAccDiv").show();
        $("#pledgeeBankNoDiv").show();
        $("#pledgeeBankNameDiv").show();
        $("#pledgeeDiv").show();
        $("#payeeAccNameDiv").hide();
        $("#payeeAccDiv").hide();
        $("#selPayeeBankButDiv").hide();
        $("#payeeBankCodeDiv").hide();
        $("#payeeBankDiv").hide();
        $("#pledgeeNameDiv").hide();
        $("#pledgeeBankAccDiv").hide();
        $("#pledgeeAcc").val("");
        $("#pledgeeName").val("");
        $("#pledgeeBankNo").val("");
        $("#pledgeeBankName").val("");
    }
}

//重置查询条件
function resetUpdate() {
    bt.corp.init().get("corpId").setUrl("all");
    dicts.get("interiorApply").attr({"data-disabledValue": ""});
    dicts.get("interiorApply").val(1);
    $("#bt-biz-externalCorpBankAcc").hide();
    $("#corpDiv").show();
    $("#corpId").show();
    $("#pledgeeAccDiv").show();
    $("#pledgeeBankNoDiv").show();
    $("#pledgeeBankNameDiv").show();
    $("#pledgeeDiv").show();
    $("#payeeAccNameDiv").hide();
    $("#payeeAccDiv").hide();
    $("#selPayeeBankButDiv").hide();
    $("#payeeBankCodeDiv").hide();
    $("#payeeBankDiv").hide();
    $("#pledgeeNameDiv").hide();
    $("#pledgeeBankAccDiv").hide();
    $("#pledgeeAcc").val("");
    $("#pledgeeName").val("");
    $("#pledgeeBankNo").val("");
    $("#pledgeeBankName").val("");
    bt.datatable.refreshCurrentPage();
    //$.fn.dataTable.tables( { visible: true, api: true } ).ajax.url( basePath+"/bill/accebill/abbillpledgeapply/datatable" ).load();
    // bt.datatable.refreshCurrentPage();
}

//重置下面需要填的项目
function resetUpdate2() {
    var enterStock = bt.dicts.init().get("enterStock").val();
    bt.corp.init().get("corpId").setUrl("all");
    if (enterStock != 12) {
        if (enterStock == 10) {
            dicts.get("interiorApply").attr({"data-disabledValue": ""});
            dicts.get("interiorApply").val(1);
        }
        if (enterStock == 11) {
            dicts.get("interiorApply").attr({"data-disabledValue": "0"});
            dicts.get("interiorApply").val(1);
        }

        $("#bt-biz-externalCorpBankAcc").hide();
        $("#corpDiv").show();
        $("#corpId").show();
        $("#pledgeeAccDiv").show();
        $("#pledgeeBankNoDiv").show();
        $("#pledgeeBankNameDiv").show();
        $("#pledgeeDiv").show();
        $("#payeeAccNameDiv").hide();
        $("#payeeAccDiv").hide();
        $("#selPayeeBankButDiv").hide();
        $("#payeeBankCodeDiv").hide();
        $("#payeeBankDiv").hide();
        $("#pledgeeNameDiv").hide();
        $("#pledgeeBankAccDiv").hide();
        $("#pledgeeAcc").val("");
        $("#pledgeeName").val("");
        $("#pledgeeBankNo").val("");
        $("#pledgeeBankName").val("");
    } else {
        dicts.get("interiorApply").attr({"data-disabledValue": "1"});
        dicts.get("interiorApply").val(0);
        $("#bt-biz-externalCorpBankAcc").hide();
        $("#corpDiv").hide();
        $("#corpId").hide();
        $("#pledgeeAccDiv").hide();
        $("#pledgeeBankNoDiv").hide();
        $("#pledgeeBankNameDiv").hide();
        $("#pledgeeDiv").hide();
        $("#payeeAccNameDiv").show();
        $("#payeeAccDiv").show();
        $("#selPayeeBankButDiv").hide();
        $("#payeeBankCodeDiv").show();
        $("#payeeBankDiv").show();
        $("#pledgeeNameDiv").show();
        $("#pledgeeBankAccDiv").show();
    }


}

//监听改变内部质押单选按钮时改变质权人为内部账号或者客商信息
function selectInteriorApplyType() {
    var $ = bt.jquery, form = bt.form();
    var interiorApplyType = dicts.get("interiorApply").val();
    if (interiorApplyType == '1') {
        //内部质押时显示内部账号
        $("#corpDiv").show();
        $("#pledgeeCorpid").show();
        $("#corp1").hide();
        $("#corp2").hide();
        $("#pledgeeAccDiv").show();
        $("#pledgeeBankNoDiv").show();
        $("#pledgeeBankNameDiv").show();
        $("#pledgeeDiv").show();

        //外部质押时显示供应商
        $("#payeeAccNameDiv").hide();
        $("#payeeAccDiv").hide();
        $("#selPayeeBankButDiv").hide();
        $("#payeeBankCodeDiv").hide();
        $("#payeeBankDiv").hide();
        $("#pledgeeNameDiv").hide();
        $("#pledgeeBankAccDiv").hide();
    } else {
        $("#corpDiv").hide();
        $("#pledgeeCorpid").hide();
        $("#corp1").show();
        $("#corp2").show();
        $("#pledgeeAccDiv").hide();
        $("#pledgeeBankNoDiv").hide();
        $("#pledgeeBankNameDiv").hide();
        $("#pledgeeDiv").hide();
        //外部质押时显示供应商
        $("#payeeAccNameDiv").show();
        $("#payeeAccDiv").show();
        $("#selPayeeBankButDiv").show();
        $("#payeeBankCodeDiv").show();
        $("#selPayeeBankButDiv").show();
        $("#payeeBankDiv").show();
        $("#pledgeeNameDiv").show();
        $("#pledgeeBankAccDiv").show();

        var enterStock = dicts.get("enterStock").val();
        if (enterStock == '12') {
            $("#bt-biz-externalCorpBankAcc").hide();
            $("#selPayeeBankButDiv").hide();
        } else {
            $("#bt-biz-externalCorpBankAcc").show();
        }

    }
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vouId + "&viceId=" + data.viceId + "&accId=" + data.accId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vouId + "&viceId=" + data.viceId + "&accId=" + data.accId
    });
}

/**
 * 进行批量提交操作
 * @param data 选择的数据
 */
function submitBatch(data) {

    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    if (!fromObj.verifyFun('#saveForm')) {
        return;
    }
    var $ = bt.jquery, form = bt.form();
    var interiorApply = dicts.get("interiorApply").val();
    var applyDate = $("[name=applyDate]").val();
    var businessDate = $("[name=businessDate]").val();
    var rmk = $("#rmk").val();
    var pledgeeCorpid = $("#pledgeeCorpid").val();
    var pledgeeName, pledgeeAcc, pledgeeBankAcc, pledgeeBankName, corpId;
    var DANGER = 'layui-form-danger';
    var sign = checkBillFlag();
    if (interiorApply == '1') {
        if (null == corp.get("corpId").data()[0]) {
            layer.alert(bt.lang('bill.accebill.abemitbill.billPledge.selectCorpId'), {icon: 7});
            return;
        } else {
            corpId = corp.get("corpId").data()[0].id;
        }
        if (!verify("pledgeeName")) {
            return;
        }
        if (sign == 1) {//电票处理才判断下面三个条件
            if (!verify("pledgeeBankName") || !verify("pledgeeBankNo") || !verify("pledgeeAcc")) {
                return;
            }
        }
        pledgeeAcc = $("#pledgeeAcc").val();
        pledgeeBankAcc = $("#pledgeeBankNo").val();
        pledgeeBankName = $("#pledgeeBankName").val();
        pledgeeName = $("#pledgeeName").val();
    } else {
        if (!verify("payeeAccName")) {
            return;
        }
        if (sign == 1) {
            if (!verify("payeeAcc") || !verify("payeeBankCode") || !verify("payeeBank")) {
                return;
            }
        }
        pledgeeAcc = $("#payeeAcc").val();
        pledgeeBankAcc = $("#payeeBankCode").val();
        pledgeeBankName = $("#payeeBank").val();
        pledgeeName = $("#payeeAccName").val();
    }

    var rowData = data.splice(0, len);
    // 新系统出票且分包时，交易金额重新赋值
    for (var i = 0; i < rowData.length; i++) {
        if (rowData[i].newStmIssuofpprInd === '1' && rowData[i].blgrdbagcrclPrmtInd === '1') {
            // 判断交易金额是否为空
            var transMoney = $("#transMoney_" + rowData[i].accId).val()
            if (!transMoney) {
                layer.alert(bt.lang('bill.accebill.endorse.transMoney.not.null'), {icon: 2})
                return;
            } else {
                rowData[i].transMoney = transMoney;
            }
        }
    }
    var vouIds, viceIds, accIds, transMoneys;
    var appCorpId;//存储申请人的单位id
    for (var i = 0; i < len; i++) {
        if (bt.isnotResource('bill:accebill:abbillpledgeapply:apply', rowData[i].corpId)) {
            layer.alert(bt.lang('common.message.dataResourceCheck', [(i + 1)]), {icon: 7});
            return;
        }
        if (vouIds) {
            vouIds = vouIds + "," + rowData[i].vouId;
        } else {
            vouIds = rowData[i].vouId;
        }
        if (viceIds) {
            viceIds = viceIds + "," + rowData[i].viceId;
        } else {
            viceIds = rowData[i].viceId
        }
        if (accIds) {
            accIds = accIds + "," + rowData[i].accId;
        } else {
            accIds = rowData[i].accId;
        }
        if (appCorpId) {
            appCorpId += "," + rowData[i].corpId;
        } else {
            appCorpId = rowData[i].corpId;
        }
        if (transMoneys){
            transMoneys = transMoneys +"," + rowData[i].transMoney;
        }else {
            transMoneys =  rowData[i].transMoney;
        }
        //内部质押申请时质权人不能跟选择的数据是同一单位
        if (interiorApply == '1' && corp.get("corpId").data()[0].id == rowData[i].corpId) {
            layer.alert(bt.lang('ab.bill.pledge.message.interiorApply.corp.notEqual'), {icon: 7});
            return;
        }
        //中心代管票据只能内部质押
        if (11 == rowData[i].enterStock) {
            if (interiorApply != '1') {
                layer.alert(bt.lang('ab.bill.pledge.message.enterStock.netFalseInteriorApply'), {icon: 7});
                return;
            }
        }
        if (12 == rowData[i].enterStock) {
            if (interiorApply != '0') {
                layer.alert(bt.lang('ab.bill.pledge.message.enterStock.bankTrueInteriorApply'), {icon: 7});
                return;
            }
        }
    }
    var data = JSON.stringify({
        "vouId": vouIds,
        "viceId": viceIds,
        "accId": accIds,
        "applyDate": applyDate,
        "businessDate": businessDate,
        "rmk": rmk,
        "pledgeeName": pledgeeName,
        "pledgeeAcc": pledgeeAcc,
        "pledgeeBankAcc": pledgeeBankAcc,
        "pledgeeBankName": pledgeeBankName,
        "interiorApply": interiorApply,
        "corpId": corpId,
        "pledgeeCorpid": pledgeeCorpid,
        "transMoneys": transMoneys
    });

    //对黑白名单进行验证
    var reData = externalCorpCheck({"name": pledgeeName, "corpId": appCorpId});
    if (reData.reCode == '3') {
        parent.layer ? parent.layer.alert(reData.msg, {icon: 2}) : layer.alert(reData.msg, {icon: 2});

    } else if (reData.reCode == '2') {
        parent.layer.confirm(reData.msg, function () {
            billPledgeSubmit(data);
        });
    } else {
        billPledgeSubmit(data);
    }
}

function billPledgeSubmit(data) {
    //证书签名
    var signDataMsg = signData("质押申请提交", 9);
    if (signDataMsg != "") {
        bt.loading.hide();
        layer.alert(bt.lang(signDataMsg), {icon: 2});
        return false;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.pledgeapply'), function () {
        bt.jquery.ajax({
            type: "POST",
            url: basePath + "/bill/accebill/abbillpledgeapply/batchApply",
            data: data,
            dataType: "json",
            async: false,
            contentType: 'application/json;charset=utf-8', //设置请求头信息
            success: function (result) {
                parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
                if (result.success) {
                    bt.datatable.refreshCurrentPage();
                }
            },
            error: function (res) {
            }
        });
    });
}

function verify(id) {
    var DANGER = 'layui-form-danger';
    if (null == $("#" + id).val() || "" == $("#" + id).val()) {
        $("#" + id).focus();
        $("#" + id).addClass(DANGER);
        parent.layer.msg(bt.lang("bill.bvVoucherType.fieldRequiredMsg"), {icon: 5, anim: 6});
        return false;
    } else {
        return true;
    }
}

/**
 * 判断是纸票流程还是电票流程
 * @returns {Number} 0:纸票 1:电票
 */
function checkBillFlag() {
    var data = bt.datatable.getSelectData("#appTable");
    var len = data.length;
    var rowData = data.splice(0, len);
    var sign = 0;//处理标志 0:纸票处理标志 1:电票处理标志
    for (i = 0; i < len; i++) {
        if ("11" == rowData[i].billFlag) {//billFlag=11.则设置sign=1
            sign = 1;
            break;
        }
    }
    if (len == 0) {//若没选择数据，则按电票处理（即选择单位不赋值给质权人名称）
        sign = 1;
    }
    return sign;
}

//开票单位回调函数
function corpCallback(data) {
    if ($) {
        $("#pledgeeCorpid").val("");
    }
    if (data.length == 0) return;
    var $ = bt.jquery, form = bt.form();
    var sign = checkBillFlag();
    if (sign == 0) {//按纸票处理
        $("#pledgeeName").val(data.corpName);
    } else {//按电票处理
        $("#pledgeeName").val("");
    }
    $("#pledgeeCorpid").val(data.id);
    $("#pledgeeAcc").val("");
    $("#pledgeeBankNo").val("");
    $("#pledgeeBankName").val("");
    bt.accountTable.get("pledgeeAcc").reload({
        urldata: {
            corpId: data.id,
            electricBill: "1",
            permission: "",
            ignorePermission: "true"
        }
    });


    /*bt.externalCorpBankAcc.set({
        corpId:data
    });*/
    //根据选择的质权人单位加载质权人账号
    /*bt.selectTable.init({
      	 elem: "#pledgeeAccTable",
      	 url: basePath+"/bill/accebill/abbillpledgeapply/getBankDataByCorpId?corpId="+data.id,
      	 scrollX: true,
      	 scrollY:"200px",
      	 searchInput:"#pledgeeAcc",
      	 columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curName",}
      	 ],
      	 clickCallback:function(data){
      		 if(data){
      			 $('#pledgeeAcc').val(data.bankAcc);
      			 $('#pledgeeName').val(data.accName);
      			 $('#pledgeeBankNo').val(data.bankCode);
      			 $('#pledgeeBankName').val(data.bankName);
      			 //$('#accId').val(data.id);
      			 //$('#bifCode').val(data.bifCode);
      		 }else{
      			// $('input.text').val('');
      		 }
      	 }
      });*/
}

function attachmentView(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var isNotOp = data.status != 11 ? '0' : '1';
    var params = {
        "id": data.accId,
        "attachmentId": (data.attachmentId || null),
        "vvid": (data.viceId || null),
        "briefId": '018',
        "isNotOp": isNotOp
    };
    parent.layer.open({
        type: 2,
        area: ['820px', '400px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbillattachment/view?" + $.param(params),
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

/**根据选择的业务设置必填标志
 * 选择数据包含电票则必填，不包含电票则不必填**/
function setVerify() {
    var billFlag = checkBillFlag();
    if (billFlag == 1) {
        $("#spanAcc").show();
        $("#spanBankNo").show();
        $("#spanBankName").show();
    } else {
        $("#spanAcc").hide();
        $("#spanBankNo").hide();
        $("#spanBankName").hide();
    }
}

//设置客商的普通客商所属单位信息
function setExternalCorpBankAccCorpId() {
    var params = {"type": "4"};
    var data = bt.datatable.getSelectData("#appTable");
    if (data.length > 0) {
        var corpId = ""
        bt.each(data, function (i, v) {
            if (corpId.indexOf(v.corpId) < 0) {
                if (corpId.length > 0) {
                    corpId += ","
                }
                corpId += v.corpId;
            }
        });
        params.corpId = corpId;
    }
    externalCorpBankAcc.set(params);
}

// 计算总条数
var opIdsValArray = function (datas) {
    if (datas && datas.length > 0) {
        for (i = 0; i < datas.length; i++) {
            if (datas[i].isSelect) {
                if (idsValArray.indexOf(datas[i].vouId) < 0) {
                    idsValArray.push(datas[i].vouId);
                }
            } else {
                var index = idsValArray.indexOf(datas[i].vouId);
                if (index > -1) {
                    objArrayRemove(idsValArray, index);
                }
            }
        }
        setSumMoney();
    }
};

var setSumMoney = function () {
    var showHTML = bt.lang("sys.common.isSelectSize", [idsValArray.length]);
    if ($("#sumMoneyBillSpan").text() != '') {
        $("#sumMoneyBillSpan").text(showHTML);
    } else {
        $('#appTable_info').append('<span id="sumMoneyBillSpan" style="color: red">' + showHTML + '</span>');
    }
}

var objArrayRemove = function (obj, index) {
    if (index > -1) {
        obj.splice(index, 1);
    }
};
//申请单位回调函数
function applyCorpCallback(data){
    if(data&&data.length==0)return;
    //设置票据单位
    //bt.corp.init().get("corpIdQuery").setUrl2("accCorp",data);
}
