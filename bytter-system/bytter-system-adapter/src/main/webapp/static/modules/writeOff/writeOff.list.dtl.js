/** 流水相关逻辑 **/

//获取流水表格数据
function getDtlDate() {
    if (null == tbDtl) {
        tbDtl = bt.datatable.initMultiTables({
            elem: "#appTableDtl",
            menuName: '核销处理',
            checkbox: true,
            mulselect: true,
            selectCss: true,
            jumpPageSelect: false,//开启跨页选择
            searchForm: {
                formElem: "#searchFormDtl",
                formFilter: "dtlSearch"
            },
            order: [[1, 'desc']],
            ajax: {
                'url': basePath + "/accountReceivable/writeOff/dtlDatatable"
            },
            buttons: [
                {
                    extend: "colvis",
                    text: bt.lang('common.button.theadHide'),
                    columns: ':not(:first):not(:last)'
                }
            ],
            columns: [
                {data: "bankSerialNo", bSortable: true,
                    render: function (data, type, full, meta) {
                        if ("1" != full.flag) {
                            var row = tbDtl.row(meta.row).node();
                            $(row).addClass("row_bg");
                        }
                    return full.bankSerialNo;
                    }

                },
                {data: "transTime", bSortable: true},
                {data: "transDateTime", bSortable: true, format: "hh:mm:ss"},
                {data: "curName", bSortable: true},
                {
                    data: "amt", bSortable: true, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.amt);
                    }
                },
                {
                    data: "writeOffBal", bSortable: true, className: 'textRight',
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.writeOffBal);
                    }
                },
                {data: "accName", bSortable: true},
                {data: "bankName", bSortable: true},
                {data: "bankAcc", bSortable: true},
                {data: "oppAccName", bSortable: true},
                {data: "oppAccNo", bSortable: true},
                {data: "uses", bSortable: true},
                {data: "postscript", bSortable: true},
                {data: "writeOffStatusName", bSortable: true,
                    render: function (data, type, full, meta) {
                        return bt.dict("writeOff.status",full.writeOffStatus);
                    }
                },
                {data: "writeOffTypeName", bSortable: true,
                    render: function (data, type, full, meta) {
                        return bt.dict("writeOff.type",full.writeOffType);
                    }
                }
            ],
            btDrawCallback: function () {
                selectType = bt.selectType.init();
                //点击应收单不自动勾选第一行流水
                //checkRow(0, true, $("#appTableDtl"));
//                autoButtonDisabled(checkDtlRule(false));
            },
            btClickRowCallback: function (data1) {
//                autoButtonDisabled(checkDtlRule(false));

                forceButtonDisabled(); //控制强制核销按钮是否禁用
            }
        });
    } else {
        bt.datatable.refreshTable("appTableDtl");
    }
}

//判断是否符合匹配规则
function checkDtlRule(needAlert){
    //符合规则且一对一才可自动核销成功
    var docTableSel = $('#appTableDoc').DataTable().rows('.selected').data();
    var dtlTableSel = $('#appTableDtl').DataTable().rows('.selected').data();
    var bankAccTypeStr = '';
    $('input[name="eqs_matchingRule"]:checked').each(function () {
        bankAccTypeStr += $(this).val() + ",";
    });
    var checkDtlRuleMsg = "";
    var checkDtlRuleFlag = true;
    if("1"!=paymentMeansSel){
        checkDtlRuleMsg = "自动核销仅支持结算方式为银行流水！";
        checkDtlRuleFlag = false;
//    }else if(!bankAccTypeStr || bankAccTypeStr.indexOf("1")==-1 || bankAccTypeStr.indexOf("5")==-1 ){
//        checkDtlRuleMsg = "自动匹配规则必须选择应收余额和应收币种！";
//        checkDtlRuleFlag = false;
    }else if(docTableSel.length!=1 || dtlTableSel.length!=1){
        checkDtlRuleMsg = "自动核销仅支持一对一！";
        checkDtlRuleFlag = false;
    }else {
        if(checkDtlRuleFlag && bankAccTypeStr.indexOf("1") != -1){
            checkDtlRuleFlag = Number(docTableSel[0].writeOffBal)-Number(dtlTableSel[0].amt) == 0;
        }
        if(checkDtlRuleFlag && bankAccTypeStr.indexOf("2") != -1){
            checkDtlRuleFlag = docTableSel[0].payerAccNo == dtlTableSel[0].oppAccNo;
        }
        if(checkDtlRuleFlag && bankAccTypeStr.indexOf("3") != -1){
            checkDtlRuleFlag = docTableSel[0].payeeAcc == dtlTableSel[0].bankAcc;
        }
        if(checkDtlRuleFlag && bankAccTypeStr.indexOf("4") != -1){
            let transTimeRule = dict.get("transTimeRule").val();
            var endDate = docTableSel[0].endDate;
            var transTime = dtlTableSel[0].transTime.substr(0,10);
            if(transTimeRule == "0"){
                //相等
                checkDtlRuleFlag = dateFun.getBetweenDay(endDate, transTime) == 0;
            }else if(transTimeRule == "-1"){
                //流水日期小于到期日期
                checkDtlRuleFlag = dateFun.getBetweenDay(endDate, transTime) < 0;
            }else if(transTimeRule == "1"){
                //流水日期大于到期日期
                checkDtlRuleFlag = dateFun.getBetweenDay(endDate, transTime) > 0;
            }
        }
        if(checkDtlRuleFlag && bankAccTypeStr.indexOf("5") != -1){
            checkDtlRuleFlag = docTableSel[0].curId == dtlTableSel[0].curId;
        }
    }
    if(needAlert && !checkDtlRuleMsg){
        parent.layer.alert(bt.lang(checkDtlRuleMsg), {icon: 2});
    }
    return checkDtlRuleFlag;
}

function clearDtlForm(){


    $("#dtl_likes_oppAccName").val("");
    selectType.get("dtl_eqs_bankType").val("");

    corp.get("dtl_eqs_corpId").val("");
    selectType.get("dtl_eqs_curId").val("");
    bankType.get("dtl_eqs_bankType").val("");
    $("#dtl_likes_bankAcc").val("");
    $("#dtl_likes_oppAccNo").val("");
    $("#dtl_likes_payeeAccName").val("");
    moneyType.get("dtl_geg_amt").val("");
    moneyType.get("dtl_leg_amt").val("");
    dateType.get("dtl_ged_transTime").val("");
    dateType.get("dtl_led_transTime").val("");
    $("#dtl_likes_bankSerialNo").val("");
    $("#dtl_likes_uses").val("");
    $("#dtl_likes_postscript").val("");
    dict.get("dtl_ins_writeOffStatus").attr({'value':'0,1'});
    dict.get("dtl_eqs_writeOffType").val("");
}

function tableInitDtl(data){
    //清除原先表单查询
//    clearDtlForm();

    if(data){
        //获取自动核销匹配规则
        var bankAccTypeStr = '';
        $('input[name="eqs_matchingRule"]:checked').each(function () {
            bankAccTypeStr += $(this).val() + ",";
        });

        var writeOffStatus = data.writeOffStatus;
        if (writeOffStatus == '2' && data.writeOffId) {//已核销
            $("#eqs_dtl_id").val(data.writeOffId);
        }else {
            $("#eqs_dtl_id").val("");
            //部分核销和未核销按照条件查询
            if (bankAccTypeStr.indexOf("1") != -1) {
                moneyType.get("dtl_geg_amt").val(data.writeOffBal);
                moneyType.get("dtl_leg_amt").val(data.writeOffBal);
            }
            if (bankAccTypeStr.indexOf("2") != -1) {
                $("#dtl_likes_oppAccNo").val(data.payerAccNo);
            }
            if (bankAccTypeStr.indexOf("3") != -1) {
                $("#dtl_likes_bankAcc").val(data.payeeAcc);
            }
            if (bankAccTypeStr.indexOf("4") != -1) {
                let time = data.endDate;
                let transTimeRule = dict.get("transTimeRule").val();
                if(transTimeRule == "0"){
                    //相等
                    dateType.get("dtl_ged_transTime").val(time);
                    dateType.get("dtl_led_transTime").val(time);
                }else if(transTimeRule == "-1"){
                    //流水日期小于到期日期
                    dateType.get("dtl_ged_transTime").val("");
                    dateType.get("dtl_led_transTime").val(dateFun.format(dateFun.addDay(time, -1)));
                }else if(transTimeRule == "1"){
                    //流水日期大于到期日期
                    dateType.get("dtl_ged_transTime").val(dateFun.format(dateFun.addDay(time, 1)));
                    dateType.get("dtl_led_transTime").val("");
                }
            }else {
                 var time = data.endDate;
                 if(time.length>10){
                     time = time.substr(0,10);
                 }
                 var date= new Date().format('yyyy-MM-dd');
                 var beginDate = dateFun.format(dateFun.addDay(time, -7));//交易时间往前7天
                 var endDate = dateFun.format(dateFun.addDay(time, 7));//交易时间往后7天
                 /*if(dateFun.getBetweenDay(endDate, date) > 0){
                     endDate = date;
                 }*/
                 dateType.get("dtl_ged_transTime").val(beginDate);
                 dateType.get("dtl_led_transTime").val(endDate);
             }
            if (bankAccTypeStr.indexOf("5") != -1) {
                selectType.get("dtl_eqs_curId").val(data.curId);
            }
        }
    }
    getDtlDate();
}

//根据所选应收单获取匹配的银行流水信息
function getDtl() {
    var data = bt.datatable.getSelectData("#appTableDoc");
    var bankAccTypeStr = '';
    var flag = false;
    $('input[name="eqs_matchingRule"]:checked').each(function () {
        bankAccTypeStr += $(this).val() + ",";
        if ($(this).val() === '4') {
            flag = true;
        }
    });
    if(flag){
        $("#transTimeRuleTr").show();
    }else{
        $("#transTimeRuleTr").hide();
    }
    if (data.length > 0) {
        tableInitByTableType(data[0]);
    }
//    autoButtonDisabled();
}

//获取手工核销流水数据
function getManualDtlData(){
    var manualDtlData = {
        flag: true,
        msg: ""
    }
    if (tbDtl) {
        var table = $('#appTableDtl').DataTable();
        var dtlSelData = table.rows('.selected').data();
        if (dtlSelData.length == 0) {
            manualDtlData.flag = false;
            manualDtlData.msg = "请选择银行流水数据！";
        }else {
            var dtlSelId = "";
            var dtlSelMoney = 0;
            var dtlSelCurName = dtlSelData[0].curName;
            var dtlSelWriteOffStatus="";
            for (var j = 0; j < dtlSelData.length; j++) {
                if(dtlSelData.writeOffStatus == '2'){
                    manualDtlData.flag = false;
                    manualDtlData.msg = "请选择未核销或部分核销的银行流水数据！";
                    break;
                }
                if(dtlSelData.length>1 && dtlSelData[j].curName!=dtlSelCurName){
                    manualDtlData.flag = false;
//                    manualDtlData.msg = "多条流水必须同一币种！";
                    manualDtlData.msg = "请一对一勾选后手工核销！";
                    break;
                }
                //带确认逻辑
                /*if(dtlSelData.length>1 && dtlSelData[j].writeOffStatus=='1'){
                    manualDtlData.flag = false;
                    manualDtlData.msg = "手工核销不支持多对多！";
                    break;
                }*/
                dtlSelMoney = bt.money.add(dtlSelMoney, dtlSelData[j].writeOffBal);
                dtlSelWriteOffStatus = dtlSelData[j].writeOffStatus;
                if(j === 0){
                    dtlSelId += dtlSelData[j].id;
                }else {
                    dtlSelId += "," + dtlSelData[j].id;
                }
            }
            if(manualDtlData.flag){
                manualDtlData.relationId = dtlSelId;
                manualDtlData.relationMoney = dtlSelMoney;
                manualDtlData.length = dtlSelData.length;
                manualDtlData.relationCurName = dtlSelCurName;
                manualDtlData.relationWriteOffStatus = dtlSelWriteOffStatus;
            }
        }
    }else {
        manualDtlData.flag = false;
        manualDtlData.msg = "请选择银行流水数据！";
    }
    return manualDtlData;
}

