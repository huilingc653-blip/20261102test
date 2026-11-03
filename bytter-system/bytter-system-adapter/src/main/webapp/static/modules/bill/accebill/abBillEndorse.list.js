var billTable = {};
var corp, isCounterpartEndorse, isRepeatEndorse, dicts;
var up;
var fromObj;
var externalCorpBankAcc = {};
var idsValArray = [];
bt.use(['form', 'radioType', 'datatable', 'selectTable', 'dicts', 'bank', 'corp', 'mulselect', 'btdate', 'jquery', 'bankInfo', 'moneyType', 'dateType', 'externalCorpBankAcc', 'element', 'accountTable', 'btImport', 'externalCorp', 'accebillInfo', 'accebillView'], function () {
    $ = bt.jquery, form = bt.form();
    fromObj = form;
    var oldIsSelectBillName = $("#isSelectBill").val();
    bt.selectBillFun = function (data) {
        if (oldIsSelectBillName != data.value) {
            // location.reload();//重载当前页面
            location.href = basePath + "/bill/accebill/abbillendorse/list?isSelectBill=" + data.value;
        }
    }
    bt.bank.init({elem: '.bank'})
    var externalCorp = bt.externalCorp.init()
    corp = bt.corp.init();
    dicts = bt.dicts.init();
    //挂授信标识
    isCounterpartEndorse = $("#isCounterpartEndorse").val() == "true" ? true : false;
    isRepeatEndorse = $("#isRepeatEndorse").val();
    bt.moneyType.init();
    bt.bankInfo.init();
    bt.dateType.init();
    bt.mulselect.init();
    externalCorpBankAcc = bt.externalCorpBankAcc.init();
    externalCorpBankAcc.set({"type": 4});
    bt.accountTable.init({urldata: {accountType: '2,3'}});
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
    //生成列表
    billTable = bt.datatable.render({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [[7, 'desc']],
        mulselect: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/abbillendorse/datatable"
        },
        buttons: [
            {
                text: bt.lang('bill.accebill.update.endorseApply'),
                hide: bt.isnotResource('bill:accebill:abbillendorse:confirm'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    billEndorse(data);
                }
            }, {
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            }/*,
		{
		    text: bt.lang('common.button.exports'),
		    action: function ( e, dt, node, config ) {
		    	exports();
		    }
		}*/
        ]
        , columns: [
            {data: "billTypeName", bSortable: true},
            {data: "billFlagName", bSortable: true},
            {data: "enterStockName", bSortable: true},
            {data: "applyCorpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.applyCorpCode + ')' + full.applyCorpName;
                }
            },
            {
                data: "corpIdName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.corpCode + ')' + full.corpIdName;
                }
            },
            {data: "busCorpName", bSortable: true},
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
            {data: "briefIdName", bSortable: true},
            {data: "sendSignName", bSortable: true},
            {data: "bankTypeName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "statusName", bSortable: true},
            {data:"newStmIssuofpprInd",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict('AB_BILL_NEW_STM_ISSUOFPPR_IND',full.newStmIssuofpprInd);
                }},
            {data:"blgrdbagcrclPrmtInd",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict('AB_BILL_BLGRDBAGCRCL_PRMT_IND',full.blgrdbagcrclPrmtInd);
                }},
            {data:"sbbllRngStrtSn",bSortable:true},
            {data:"sbbllRngEndSn",bSortable:true},
            {data:"blStdAmt",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    if(full.newStmIssuofpprInd == 1){
                        return bt.money.format(full.blStdAmt);
                    }else {
                        return "";
                    }
                }
            },
            {data: "blRmtrUnnIdNo", bSortable: true},
            {
                data: "transMoney", bSortable: true, className: 'textRight', width: 120,
                render: function (data, type, full, meta) {
                    if (full.newStmIssuofpprInd === '1' && full.blgrdbagcrclPrmtInd === '1') {
                        return '<input lay-verify="required" style="width: 100px" class="layui-input bt-input-inline bt-biz-moneyType trans-money-input" ' +
                            'value="'+bt.money.format(full.transMoney)+'" type="text" data-decimal="2" data-min="0.01" data-max="' + full.transMoney + '"' +
                            'data-id="transMoney_'+full.id+'" data-key="'+full.id+'" data-name="transMoney_'+full.id+'" data-biz-name="transMoney_'+full.id+'" />';
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
                    if ($("#isSelectBill").val() == '1') {
                        html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="deleteSelectBill(\'' + full.id + '\')">' + bt.lang("common.button.delete") + '</a>';
                    }
                    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    return html;
                }
            }
        ],
        btClickRowCallback: function (data) {
            //full.newStmIssuofpprInd === '1' && full.blgrdbagcrclPrmtInd === '1'
            //alert(data[0].transMoney);
            //alert(data[0].newStmIssuofpprInd);
            //alert(data[0].blgrdbagcrclPrmtInd);
            if (data[0].newStmIssuofpprInd === '1' && data[0].blgrdbagcrclPrmtInd === '1') {
                // 新票交易金额字段赋值
                data[0].transMoney = $("#transMoney_" + data[0].id).val();
            }


            //判断系统是否挂资金计划
            var budgetSel = $("#budgetSel").val();
            if (budgetSel == "1") {
                //判断背书挂接资金计划科目是否为空，不为空则挂资金计划
                var endorseItem = $("#endorseItem").val();
                if (endorseItem != "") {
                    var selectData = bt.datatable.getSelectData("#appTable");

                    //当选择多条数据后选择外部背书时不挂资金计划，清除资金计划列表，选一条数据时则判断选择的数据单位是否受资金计划控制，受则显示资金计划列表
                    if (selectData.length > 1) {
                        layer.alert(bt.lang('bill.accebill.abemitbill.billendorse.isselectdatabudget'), {icon: 7});
                        $("#tagItemDetail").html("");
                        //全反选，重新加载表格
                        billTable.draw();
                        $('.checkAll').prop('checked', false);
                    } else {
                        for (i = 0; i < selectData.length; i++) {
                            var enterStock = selectData[i].enterStock;
                            /* if(11==enterStock && 1==$('input:radio[name="interiorApply"]:checked').val()){
                                 $("#tagItemDetail").html("");
                             }else{*/
                            //判断挂接资金计划
                            //layer.alert(selectData[i].corpId);
                            isBudget(selectData[i].corpId, "010");
                            // }
                        }
                    }
                } else {
                    //billTable.attr({mulselect:true})
                    $("#tagItemDetail").html("");
                }
            }
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
                    colNum: '11',
                    dataName: 'billMoney'
                },
                {
                    colNum: '27',
                    dataName: 'transMoney'
                }
            ]
        }
    });
    //监听改变内部质押单选按钮时改变质权人为内部账号或者客商信息
    form.on('radio(interiorApply)', function (data) {
        isRenderBudget();
        if (data.value == 1) {
            //内部背书时显示内部账号
            $("#corpId").show();
            $("#pledgeeAccDiv1").show();
            $("#pledgeeBankNoDiv").show();
            $("#pledgeeBankNameDiv").show();
            $("#payeeAccNameDiv1").show();
            //外部质押时显示供应商
            $("#innerCorpTr").show();
            $("#payeeAccNameDiv0").hide();
            $("#payeeAccDiv").hide();
            $("#selPayeeAccButDiv").hide();
            $("#payeeBankCodeDiv").hide();
            $("#selPayeeBankButDiv").hide();
            $("#payeeBankDiv").hide();
            $("#pledgeeNameDiv").hide();
            $("#pledgeeBankAccDiv").hide();
            $("#endorseeBankNameTd").show();
        } else {
            //内部背书时显示内部账号
            $("#corpId").hide();
            $("#pledgeeAccDiv1").hide();
            $("#pledgeeBankNoDiv").hide();
            $("#pledgeeBankNameDiv").hide();
            $("#innerCorpTr").hide();
            $("#payeeAccNameDiv1").hide();
            //外部质押时显示供应商
            $("#payeeAccNameDiv0").show();
            $("#payeeAccDiv").show();
            $("#selPayeeAccButDiv").show();
            $("#payeeBankCodeDiv").show();
            $("#selPayeeBankButDiv").show();
            $("#payeeBankDiv").show();
            $("#pledgeeNameDiv").show();
            $("#pledgeeBankAccDiv").show();
            $("#endorseeBankNameTd").hide();
        }
    });
    //供应商选择
    bt.externalCorpCallback = function (data, dom) {
        $("#payeeAcc").val(data.externalAcc);
        $("#payeeAccName0").val(data.name);
        $("#payeeBankCode").val(data.bankSourceCode);
        $("#payeeBank").val(data.bank);
    }

    //单位选择
    bt.bailCorpCallback = function (data, dom) {
        $("#payeeAccName0").val(data.name);
        $("#payeeAcc").val(data.externalAcc);
        $("#payeeBankCode").val(data.bankSourceCode);
        $("#payeeBank").val(data.bank);
        $("#pledgeeBankTypeId").val(data.bankTypeId);
        $("#recSocNo").val(data.socCode);
    }

    //银行选择
    bt.bailBankInfoCallback = function (data, dom) {
        $("#payeeAccName0").val(data.bankName);
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
    mulselect();
    //监听提交
    form.on('submit(submit)', function () {
        submitBatch();
        return false;
    });
    //导入初始化
    importExcel = function () {
        //证书签名
        var signDataMsg = signData("背书申请导入", 9);
        if (signDataMsg != "") {
            bt.loading.hide();
            layer.alert(bt.lang(signDataMsg), {icon: 2});
            return false;
        }
        up = bt.btImport.init({
            code: "512880455c5dcd90015c5dcdda790L01",
            titleRows: 0,
            width: "90%",
            height: "90%",
            headRows: 1,
            autoCloseWindown:false,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                bt.loading.show();
                $.ajax({
                    type: "POST",
                    url: basePath + "/bill/accebill/abbillendorse/saveImport",
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

    var externalCorpInfo = {"corpId": "402880425b8abcfb015b8ac16e980000", "corpIdVal": "", "externalIdVal": ""};
    bt.externalCorpCallback = function (data, dom) {
        externalCorpInfo.corpIdVal = data.corpIdVal;
        externalCorpInfo.externalIdVal = data.externalIdVal;
    }

    bt.externalCorpParams = function () {
        externalCorpInfo.isView = "0";
        externalCorp.set(externalCorpInfo);
        return true;
    }

    bt.externalCorpViewParams = function () {
        externalCorpInfo.isView = "1";
        externalCorp.set(externalCorpInfo);
        return true;
    }


    var accebillInfo = bt.accebillInfo.init();
    var accebillView = bt.accebillView.init();
    bt.accebillInfoParams = function () {
        accebillInfo.set({
            "resource": 'bill:accebill:abbillendorse:query',
            "ids": $("#billAccids").val(),
            "oldIds": ''
        });
        return true;
    }
    bt.accebillViewParams = function () {
        accebillView.set({"id": $("#billAccids").val()});
        return true;
    }
    bt.accebillInfoCallback = function (datas, rom) {
        if (datas && datas.data && datas.data.length > 0) {
            for (var i = 0; i < datas.data.length; i++) {
                opAccBillIdsValArray(datas.data[i].id, false);
            }
            $("#billAccids").val(accBillIdsValArray.join(","));
            billTable.draw();
        }
        // $("#sumMoney").val(datas.sumMoney);
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
                if(tableData[i].id === key){
                    tableData[i].transMoney = $("#transMoney_"+key).val();
                    break
                }
            }
        }
        bt.datatable.updateSummary("#appTable");
    },50)

})

//选择出票人账户回调方法
function accountPayeeCallBack(data) {
    var $ = bt.jquery;
    $("#payeeAccName1").val(data.accName);//【出票人以户名为准】
    $("#pledgeeAcc1").val(data.bankAcc);
    $("#pledgeeBankName1").val(data.bankName);
    $("#pledgeeBankNo").val(data.bankCode);
    $("#pledgeeBankTypeId").val(data.bankTypeId);
}

function verify(id) {
    var DANGER = 'layui-form-danger';
    if (null == $("#" + id).val() || "" == $("#" + id).val()) {
        $("#" + id).focus();
        $("#" + id).addClass(DANGER);
        return false;
    } else {
        return true;
    }
}

//是否挂资金计划，挂则列表只能单选，否则可以多选
function mulselect() {
    var budgetSel = $("#budgetSel").val();
    if (budgetSel == "1") {
        //判断背书挂接资金计划科目是否为空，不为空则挂资金计划
        var endorseItem = $("#endorseItem").val();
        if (endorseItem != "") {
            billTable.attr({mulselect: false});
        } else {
            billTable.attr({mulselect: true});
        }
    }
}

/**根据选择的业务设置必填标志
 * 选择数据包含电票则必填，不包含电票则不必填**/
function setVerify() {
    var billFlag = checkBillFlag();
    if (billFlag == 1) {
        $("#spanCorpId").show();
        $("#spanAcc").show();
        $("#spanBankNo").show();
        $("#spanBankName").show();
    } else {
        $("#spanCorpId").hide();
        $("#spanAcc").hide();
        $("#spanBankNo").hide();
        $("#spanBankName").hide();
    }
}


function resetFristCarryOver() {
    $("#budgetDiv").hide();
    $("#tagItemDetail").html("");
}


function billEndorse(data) {
    var date = document.getElementById("date");
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    if (!bt.form().verifyFun('#saveForm')) {
        return;
    }
    var rowData = data.splice(0, len);
    // 新系统出票且分包时，交易金额重新赋值
    for (var i = 0; i < rowData.length; i++) {
        if (rowData[i].newStmIssuofpprInd === '1' && rowData[i].blgrdbagcrclPrmtInd === '1') {
            // 判断交易金额是否为空
            var transMoney = $("#transMoney_" + rowData[i].id).val()
            if (!transMoney) {
                layer.alert(bt.lang('bill.accebill.endorse.transMoney.not.null'), {icon: 2})
                return;
            } else {
                rowData[i].transMoney = transMoney;
            }
        }
    }
    var interiorApply = $('input:radio[name="interiorApply"]:checked').val();
    var bankendrSmtmk = $('input:radio[name="bankendrSmtmk"]:checked').val();
    var businessDate = $("[name=businessDate]").val();
    var rmk = $("#rmk").val();
    var pledgeeName, pledgeeAcc, pledgeeBankAcc, pledgeeBankName, bankendrsmtmk, corpId, pledgeeBankTypeId;
    var sign = checkBillFlag();
    if (interiorApply == "1") {
        if (null == corp.get("corpId").data()[0]) {
            layer.alert(bt.lang('bill.accebill.abemitbill.billEndorse.selectCorpId'), {icon: 7});
            return;
        } else {
            corpId = corp.get("corpId").data()[0].id;
        }
        if (verify("payeeAccName1")) {
            pledgeeName = $("#payeeAccName1").val();
        } else {
            return;
        }
//    	if(sign==1){////电票处理才判断下面三个条件
        if (verify("pledgeeAcc1") || sign != 1) {
            pledgeeAcc = $("#pledgeeAcc1").val();
        } else {
            return;
        }
        if (verify("pledgeeBankNo") || sign != 1) {
            pledgeeBankAcc = $("#pledgeeBankNo").val();
        } else {
            return;
        }
        if (verify("pledgeeBankName1") || sign != 1) {
            pledgeeBankName = $("#pledgeeBankName1").val();
        } else {
            return;
        }
//    	}

    } else {
        if (verify("payeeAccName0")) {
            pledgeeName = $("#payeeAccName0").val();
        } else {
            return;
        }
//    	if(sign==1){//电票处理才判断下面三个条件
        if (verify("payeeAcc") || sign != 1) {
            pledgeeAcc = $("#payeeAcc").val();
        } else {
            return;
        }
        if (verify("payeeBankCode") || sign != 1) {
            pledgeeBankAcc = $("#payeeBankCode").val();
        } else {
            return;
        }
        if (verify("payeeBank") || sign != 1) {
            pledgeeBankName = $("#payeeBank").val();
        } else {
            return;
        }
//    	}
    }


    //判断是否挂资金计划
    var budgetSel = $("#budgetSel").val();
    if (budgetSel == "1") {
        if ($('#budgetDiv').css('display') != 'none') { 	//处理任何条件下不管有没有上报金额都会提示校验金额与资金计划总金额一致
            //判断背书挂接资金计划科目是否为空，不为空则挂资金计划
            var endorseItem = $("#endorseItem").val();
            if (endorseItem != "") {
                //当选择多条数据后选择外部背书时不挂资金计划，清除资金计划列表，选一条数据时则判断选择的数据单位是否受资金计划控制，受则显示资金计划列表
                if (len == "1") {
                    var enterStock = rowData[0].enterStock;
                    if (11 == enterStock && 1 == $('input:radio[name="interiorApply"]:checked').val()) {

                    } else {
                        var sumMoney = bt.moneyType.get("sumMoney").val();
                        //获取选择的票据金额
                        var money = rowData[0].transMoney;
                        // 校验金额与资金计划总金额一致
                        if (sumMoney != money) {
                            layer.alert(bt.lang("accebill.message.budgetMoney"), {icon: 2});
                            return false;
                        }
                    }

                }
            }
        }
    }
    //被背书人名称
    $("#pledgeeName").val(pledgeeName);
    //被背书人账号
    $("#pledgeeAcc").val(pledgeeAcc);
    //被背书人开户行
    $("#pledgeeBankAcc").val(pledgeeBankAcc);
    //被背书人开户行名称
    $("#pledgeeBankName").val(pledgeeBankName);

    var vt = true, ids, vids, vvids, origNo, origNos, transMoneys, pledgeeBankTypeId = $("#pledgeeBankTypeId").val(),
        repartMsg;
    if (pledgeeBankTypeId == "") {
        $.ajax({
            type: 'post',
            url: basePath + "/bill/accebill/abbillendorse/getBankTypeId",
            data: {
                bankCode: pledgeeBankAcc,
                interiorApply: $('input:radio[name="interiorApply"]:checked').val(),
                pledgeeAcc: $("#pledgeeAcc").val()
            },
            dataType: 'json',
            async: false,
            success: function (result) {
                pledgeeBankTypeId = result.bankTypeId;
            }
        });
    }
    var appCorpId;//存储申请人的单位id
    for (i = 0; i < len; i++) {
        if (bt.isnotResource('bill:accebill:abbillendorse:confirm', rowData[i].applyCorpId, (i + 1))) {
            return;
        }

        if (vids) {
            vids = vids + "," + rowData[i].vid;
        } else {
            vids = rowData[i].vid;
        }

        if (vvids) {
            vvids = vvids + "," + rowData[i].vvid;
        } else {
            vvids = rowData[i].vvid;
        }

        if (ids) {
            ids = ids + "," + rowData[i].id;
        } else {
            ids = rowData[i].id;
        }
        if (appCorpId) {
            appCorpId += "," + rowData[i].corpId;
        } else {
            appCorpId = rowData[i].corpId;
        }
        if (origNo) {
            origNo += "," + rowData[i].origNo;
        } else {
            origNo = rowData[i].origNo;
        }
        if (transMoneys) {
            transMoneys = transMoneys + "," + rowData[i].transMoney;
        } else {
            transMoneys = rowData[i].transMoney;
        }

        //存储电票的中间票据号码
        if (rowData[i].origNos != "") {
            //票据号码
            if (origNos) {
                origNos = origNos + "," + rowData[i].origNo;
            } else {
                origNos = rowData[i].origNo;
            }
        }

        //同名跨行背书背书判断，是/否
        //通用判断 同名不同行参数为否 判断背书申请人所属账户和被背书人所属账户一致
        if (isCounterpartEndorse == false && pledgeeName == rowData[i].bankName) {
            layer.alert(bt.lang('bill.accebill.endorse.namesake.not.supported', [rowData[i].origNo]), {icon: 7})
            return;
        }


        //同名跨行背书背书判断，是/否
        if (isCounterpartEndorse
            && pledgeeName == rowData[i].bankName && pledgeeBankTypeId == rowData[i].bankTypeId) {
            layer.alert(bt.lang('bill.accebill.endorse.peers.not.supported', [rowData[i].origNo]), {icon: 7});
            return;
        }


        //参数为是：第一层 参数为不允许重复背书
        if (isRepeatEndorse == "03") {
            //出票人，收票人，持票人，前手
            if (pledgeeName == rowData[i].payer | pledgeeName == rowData[i].payee
                | pledgeeName == rowData[i].cnAccrecords | pledgeeName == rowData[i].bankName) {
                if (isCounterpartEndorse == false) {
                    layer.alert(bt.lang('bill.accebill.endorse.origno.repea.msg', [rowData[i].origNo]), {icon: 7})
                    return;
                }
            }
        }
        if (isCounterpartEndorse == false && isRepeatEndorse == "02") {
            //出票人，收票人，持票人，前手
            if (pledgeeName == rowData[i].payer | pledgeeName == rowData[i].payee
                | pledgeeName == rowData[i].cnAccrecords | pledgeeName == rowData[i].bankName) {
                if (repartMsg) {
                    repartMsg = repartMsg + "," + rowData[i].origNo;
                } else {
                    repartMsg = rowData[i].origNo;
                }
            }
        }
    }

    $("#ids").val(ids);
    $("#vids").val(vids);
    $("#vvids").val(vvids);
    $("#origNo").val(origNo);
    $("#transMoneys").val(transMoneys);
    //是否允许同名跨行背书 为否
    var param = {
        ids: ids,
        origNos: origNos,
        endorseeName: pledgeeName,
        endorseeBankNo: pledgeeBankTypeId,
        repartMsg: repartMsg
    };
    $.ajax({
        type: 'post',
        url: basePath + "/bill/accebill/abbillendorse/checkRepeatEndorse",
        data: param,
        async: false,
        dataType: 'json',
        success: function (result) {
            if (result.flag) {
                if (isCounterpartEndorse == false && isRepeatEndorse == "02") {
                    vt = false;
                    parent.layer.confirm(bt.lang('bill.accebill.endorse.repeat.operation.confirmation', [result.origNos]), function () {
                        billEndorseBlackCheck(pledgeeName, appCorpId);
                    });

                } else if (isRepeatEndorse == "03") {
                    vt = false;
                    layer.alert(bt.lang('bill.accebill.endorse.origno.repea.msg', [result.origNos]), {icon: 7})

                }


            }
        }
    });

    if (vt) {
        billEndorseBlackCheck(pledgeeName, appCorpId);
    }

}

function billEndorseBlackCheck(pledgeeName, appCorpId) {
    //对黑白名单进行验证
    var reData = externalCorpCheck({"name": pledgeeName, "corpId": appCorpId});
    if (reData.reCode == '3') {
        parent.layer ? parent.layer.alert(reData.msg, {icon: 2}) : layer.alert(reData.msg, {icon: 2});

    } else if (reData.reCode == '2') {
        parent.layer.confirm(reData.msg, function () {
            billEndorseSubmit();
        });
    } else {
        billEndorseSubmit();
    }
}

function billEndorseSubmit() {
    //证书签名
    var signDataMsg = signData("背书申请提交", 9);
    if (signDataMsg != "") {
        bt.loading.hide();
        layer.alert(bt.lang(signDataMsg), {icon: 2});
        return false;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.billEndorse'), function () {
        $.post(basePath + "/bill/accebill/abbillendorse/EndorsementApplication", $('#saveForm').serialize(), function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                $(":radio[name='bankendrSmtmk'][value='1']").prop("checked", "checked");
                $(":radio[name='interiorApply'][value='1']").prop("checked", "checked");
                $("#corpId").val('')
                corp.get('corpId').val('')
                dicts.get("recDghType").val('DT02');
                $("#pledgeeCorpid").val('')
                $("#pledgeeAcc1").val('')
                $("#payeeAcc").val('')
                $("#payeeAccName1").val('')
                $("#payeeAccName0").val('')
                $("#pledgeeBankNo").val('')
                $("#payeeBankCode").val('')
                $("#payeeBank").val('')
                $("#pledgeeBankName1").val('')
                $("#rmk").val('')
                $("#trader").val('')
                //内部背书时显示内部账号
                $("#corpId").show();
                $("#pledgeeAccDiv1").show();
                $("#pledgeeBankNoDiv").show();
                $("#pledgeeBankNameDiv").show();
                $("#payeeAccNameDiv1").show();
                //外部质押时显示供应商
                $("#innerCorpTr").show();
                $("#payeeAccNameDiv0").hide();
                $("#payeeAccDiv").hide();
                $("#selPayeeAccButDiv").hide();
                $("#payeeBankCodeDiv").hide();
                $("#selPayeeBankButDiv").hide();
                $("#payeeBankDiv").hide();
                $("#pledgeeNameDiv").hide();
                $("#pledgeeBankAccDiv").hide();
                $("#endorseeBankNameTd").show();
                $(function () {
                    var now = new Date();
                    var year = now.getFullYear(); //得到年份
                    var month = now.getMonth();//得到月份
                    var date = now.getDate();//得到日期
                    month = month + 1;
                    if (month < 10) month = "0" + month;
                    if (date < 10) date = "0" + date;
                    var time = "";
                    time = year + "-" + month + "-" + date;
                    //当前日期赋值给当前日期输入框中（jQuery easyUI）
                    $("input[name='businessDate']").val(time);
                });
                bt.datatable.refreshCurrentPage();
//				location.reload();
            }
        }, 'JSON');
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vid + "&viceId=" + data.vvid + "&accId=" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vid + "&viceId=" + data.vvid + "&accId=" + data.id
    });
}


function exports(el) {
    parent.layer.alert(bt.lang("common.message.indevelopment"));
}

function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/bill/accebill/abemitbillback/delete/" + data.id, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            bt.datatable.refreshCurrentPage();
        }, 'JSON');
    });
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
    var $ = bt.jquery, form = bt.form();
    var sign = checkBillFlag();
    if (sign == 0) {//按纸票处理
        $('#payeeAccName1').val(data.corpName);
    } else {//按电票处理
        $('#payeeAccName1').val("");
    }
    $('#pledgeeCorpid').val(data.id);
    $('#pledgeeAcc1').val("");
    $('#pledgeeBankNo').val("");
    $('#pledgeeBankName1').val("");
//	bt.accountTable.get("pledgeeAcc1").initCorp(data.id,"1");
    bt.accountTable.get("pledgeeAcc1").reload({
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
      	 searchInput:"#pledgeeAcc1",
      	 columns: [
            {data:"bankAcc",},
            {data:"accName",},
            {data:"bankName",},
            {data:"curName",}
      	 ],
      	 clickCallback:function(data){
      		 if(data){
      			 $('#pledgeeAcc1').val(data.bankAcc);
      			 $('#payeeAccName1').val(data.accName);
      			 $('#pledgeeBankNo').val(data.bankCode);
      			 $('#pledgeeBankName1').val(data.bankName);
      			 //$('#accId').val(data.id);
      			 //$('#bifCode').val(data.bifCode);
      		 }else{
      			// $('input.text').val('');
      		 }
      	 }
      });*/

}

//获取选中的所有数据
function getSelectData() {
    var selectData;
    bt.use('datatable', function () {
        selectData = bt.datatable.getSelectData("#appTable");
    })
    return selectData;
}

/**
 * 判断选择数据是否挂资金计划
 */
function isRenderBudget() {
    var selectData = bt.datatable.getSelectData("#appTable");
    if (selectData.length) {
        //判断票据系统是否挂接资金计划
        var budgetSel = $("#budgetSel").val();
        if (budgetSel == "1") {
            //判断背书挂接资金计划科目是否为空，不为空则挂资金计划
            var endorseItem = $("#endorseItem").val();
            if (endorseItem != "") {
                //当选择多条数据后选择外部背书时不挂资金计划，清除资金计划列表，选一条数据时则判断选择的数据单位是否受资金计划控制，受则显示资金计划列表
                if (selectData.length > 1) {
                    layer.alert(bt.lang('bill.accebill.abemitbill.billendorse.isselectdatabudget'), {icon: 7});
                    $("#tagItemDetail").html("");
                    return false;
                } else {
                    for (i = 0; i < selectData.length; i++) {
                        var enterStock = selectData[i].enterStock;
                        if (11 == enterStock && 1 == $('input:radio[name="interiorApply"]:checked').val()) {
                            $("#tagItemDetail").html("");
                            return false;
                        } else {
                            //判断挂接资金计划
                            isBudget(selectData[i].corpId, "010");
                            return true;
                        }
                    }

                }
            }
        }
    }
    return true;
}


//根据单位ID和业务类型判断是否挂预算
function isBudget(corpId, briefId) {
    $.ajax({
        type: 'POST',
        url: basePath + "/bill/accebill/abvoucherinfoVice/isBudget",
        data: {'corpId': corpId, 'briefId': briefId},
        async: false,
        dataType: 'json',
        success: function (result) {
            if (result.success) {
                if (result.attributes.isBudget) {
                    $genOtherHtml(corpId);
                } else {
                    $("#budgetDiv").hide();
                    $("#tagItemDetail").html("");
                }
            } else {
//	            	parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
                $("#budgetDiv").hide();
                $("#tagItemDetail").html("");
            }
        }
    });
}

/**
 * 生成公共的属性
 * @return
 */
$genOtherHtml = function (corpId) {
    var $ = bt.jquery, form = bt.form();
    var itemCode = $('input[name="endorseItem"]').val();
    var defItemCode = $('input[name="defItemCode"]').val();
    var processDate = $('input[name="businessDate"]').val();
    var url = basePath + "/budget/fbsBudgetProcess/getItemDetail";
    var curId = $("#curId").val();
    var param = {
        corpId: corpId,
        itemCode: itemCode,
        defItemCode: defItemCode,
        processDate: processDate,
        curId: curId,
        type: 'select',
        date: (new Date()).getTime()
    };
    $.ajax({
        type: 'post',
        url: url,
        data: param,
        async: true,
        success: function (json) {
            $("#budgetDiv").show();
            $("#tagItemDetail").html("");
            $("#tagItemDetail").html(json);
            //设置执行总金额lay-verify属性
//        bt.use('moneyType',function(){
//        	bt.moneyType.init().get("sumMoney").attr("lay-verify","checkSumMoney");
//        });

        },
    });
};

function attachmentView(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var isNotOp = data.status != 11 ? '0' : '1';
    var params = {
        "id": data.id,
        "attachmentId": (data.attachmentId || null),
        "vvid": (data.vvid || null),
        "briefId": '010',
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

var accBillIdsValArray = [];

// 查找指定的元素在数组中的位置
Array.prototype.indexOf = function (val) {
    for (var i = 0; i < this.length; i++) {
        if (this[i] == val) {
            return i;
        }
    }
    return -1;
}

var objArrayRemove = function (obj, index) {
    if (index > -1) {
        obj.splice(index, 1);
    }
};

var deleteSelectBill = function (id) {
    opAccBillIdsValArray(id, true);
    $("#billAccids").val(accBillIdsValArray.join(","));
    billTable.draw();
}

var opAccBillIdsValArray = function (accBillid, isDelete) {
    if (!isDelete) {
        if (accBillIdsValArray.indexOf(accBillid) < 0) {
            accBillIdsValArray.push(accBillid);
        }
    } else {
        var index = accBillIdsValArray.indexOf(accBillid);
        if (index > -1) {
            objArrayRemove(accBillIdsValArray, index);
        }
    }
    $("#ids").val(accBillIdsValArray.join(","));
};


// 计算总条数
var opIdsValArray = function (datas) {
    if (datas && datas.length > 0) {
        if ($("#budgetSel").val() != "1") {  // 不挂接资金计划
            for (i = 0; i < datas.length; i++) {
                if (datas[i].isSelect) {
                    if (idsValArray.indexOf(datas[i].id) < 0) {
                        idsValArray.push(datas[i].id);
                    }
                } else {
                    var index = idsValArray.indexOf(datas[i].id);
                    if (index > -1) {
                        objArrayRemove(idsValArray, index);
                    }
                }
            }
        } else {
            if (datas[0].isSelect) {
                idsValArray[0] = datas[0].id
            } else {
                objArrayRemove(idsValArray, 0);
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
function applyCorpCallback(data) {
    if (data && data.length == 0) return;
    //设置票据单位
    //bt.corp.init().get("corpIdQuery").setUrl2("accCorp",data);
}


