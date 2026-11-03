/** 票据相关逻辑 **/
//获取流水表格数据
var tbBill;
function getBillData() {
    if (null == tbBill) {
        tbBill = bt.datatable.initMultiTables({
            elem: "#appTableBill",
            menuName: '核销处理',
            checkbox: true,
            mulselect: true,
            selectCss: true,
            isInitData: false,
            jumpPageSelect: false,//开启跨页选择
            searchForm: {
                formElem: "#searchFormBill",
                formFilter: "billSearch"
            },
            order: [[1, 'desc']],
            ajax: {
                'url': basePath + "/accountReceivable/writeOff/billDatatable"
            },
            buttons: [
                {
                    extend: "colvis",
                    text: bt.lang('common.button.theadHide'),
                    columns: ':not(:first):not(:last)'
                }
            ],
            columns: [
                {data: "billTypeName", bSortable: true,
                    render: function (data, type, full, meta) {
                        if ("1" != full.flag) {
                            var row = tbBill.row(meta.row).node();
                            $(row).addClass("row_bg");
                        }
                    return bt.dict("AB_BILL_TYPE", full.billType);
                    }
                },
                {data: "billFlagName", bSortable: true,
                    render: function (data, type, full, meta) {
                        return bt.dict("AB_BILL_FLAG", full.billFlag);
                    }
                },
                {data: "corpCodeAndName", bSortable: true},
                {data: "blRmtrUnnIdNo", bSortable: true},
                {data: "payer", bSortable: true},
                {data: "acceptor", bSortable: true},
                {data: "origNo", bSortable: true},
                {data: "billMoney", bSortable: true,
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.billMoney);
                    }
                },
                {data: "realMoney", bSortable: true,
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.realMoney);
                    }
                },
                {data: "writeOffBal", bSortable: true,
                     render: function (data, type, full, meta) {
                         return bt.money.format(full.writeOffBal);
                     }
                 },
                {data: "startDate", bSortable: true},
                {data: "endDate", bSortable: true},
                {data: "receDate", bSortable: true},
                {data: "briefIdName", bSortable: true,
                    render: function (data, type, full, meta) {
                        return bt.dict("AB_BRIEF_STATUS",full.briefId);
                    }
                },
                {data: "enterStockName", bSortable: true,
                    render: function (data, type, full, meta) {
                        return bt.dict("AB_ENTER_STOCK",full.enterStock);
                    }
                },
                {data: "newStmIssuofpprIndName", bSortable: true,
                    render: function (data, type, full, meta) {
                        return bt.dict("AB_BILL_NEW_STM_ISSUOFPPR_IND",full.newStmIssuofpprInd);
                    }
                },
                {data: "blgrdbagcrclPrmtIndName", bSortable: true,
                    render: function (data, type, full, meta) {
                        return bt.dict("AB_BILL_BLGRDBAGCRCL_PRMT_IND",full.blgrdbagcrclPrmtInd);
                    }
                },
                {data: "sbbllRngStrtSn", bSortable: true},
                {data: "sbbllRngEndSn", bSortable: true},
                {data: "blStdAmt", bSortable: true,
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.blStdAmt);
                    }
                },
                {data: "transferWayName", bSortable: true,
                    render: function (data, type, full, meta) {
                        if (full.briefId == '8018' && full.transferWay == '0') {
                            return data = '票据池质押';
                        }else{
                            return bt.dict("AB_BILL_TRANSFERWAY", full.transferWay);
                        }
                    }
                },
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
//                checkRow(0, true, $("#appTableBill"));
            },
            btClickRowCallback: function (data1) {
                forceButtonDisabled(); //控制强制核销按钮是否禁用
            }
        });
    } else {
        bt.datatable.refreshTable("appTableBill");
    }
}

function clearBillForm(){
    $("#eqs_bill_id").val("");
    corp.get("bill_eqs_corpId").val("");
    dict.get('bill_eqi_billFlag').val("");
    dict.get('bill_eqi_billType').val("");
    dateType.get("bill_ged_startDate").val("");
    dateType.get("bill_led_startDate").val("");
    $("#bill_likes_origNo").val("");
    dict.get('bill_eqs_enterStock').val("");
    dateType.get("bill_ged_endDate").val("");
    dateType.get("bill_led_endDate").val("");
    $("#bill_likes_payer").val("");
    $("#bill_likes_acceptor").val("");
    moneyType.get("bill_geg_billMoney").val("");
    moneyType.get("bill_leg_billMoney").val("");
    dateType.get("bill_ged_businessDate").val("");
    dateType.get("bill_led_businessDate").val("");
    dict.get("bill_ins_writeOffStatus").attr({'value':'0,1'})
    dict.get("bill_eqs_writeOffType").val("");
}

function tableInitBill(data){
    if(data){
        var writeOffStatus = data.writeOffStatus;
        if (writeOffStatus == '2' && data.writeOffId) {//已核销
            $("#eqs_bill_id").val(data.writeOffId);
        }else {
            $("#eqs_bill_id").val("");
        }
        getBillData();
    }
}

function billCorpCallback(id){

}


//获取手工核销流水数据
function getManualBillData(){

    var manualBillData = {
        flag: true,
        msg: ""
    }
    if (tbBill) {
        var table = $('#appTableBill').DataTable();
        var billSelData = table.rows('.selected').data();
        if (billSelData.length == 0) {
            manualBillData.flag = false;
            manualBillData.msg = "请选择票据数据！";
        }else {
            var billSelId = "";
            var billSelMoney = 0;
            var billSelCurName = billSelData[0].curName;
            var billSelWriteOffStatus="";
            for (var j = 0; j < billSelData.length; j++) {
                if(billSelData.writeOffStatus == '2'){
                    manualBillData.flag = false;
                    manualBillData.msg = "请选择未核销或部分核销的票据数据！";
                    break;
                }
                if(billSelData.length>1 && billSelData[j].curName!=billSelCurName){
                    manualBillData.flag = false;
                    manualBillData.msg = "多条票据必须同一币种！";
                    break;
                }
                //带确认逻辑
                /*if(billSelData.length>1 && billSelData[j].writeOffStatus=='1'){
                    manualBillData.flag = false;
                    manualBillData.msg = "手工核销不支持多对多！";
                    break;
                }*/
                billSelMoney = bt.money.add(billSelMoney, billSelData[j].writeOffBal);
                billSelWriteOffStatus = billSelData[j].writeOffStatus;
                if(j === 0){
                    billSelId += billSelData[j].id;
                }else {
                    billSelId += "," + billSelData[j].id;
                }
            }
            if(manualBillData.flag){
                manualBillData.relationId = billSelId;
                manualBillData.relationMoney = billSelMoney;
                manualBillData.length = billSelData.length;
                manualBillData.relationCurName = billSelCurName;
                manualBillData.relationWriteOffStatus = billSelWriteOffStatus;
            }
        }
    }else {
        manualBillData.flag = false;
        manualBillData.msg = "请选择票据数据！";
    }
    return manualBillData;
}