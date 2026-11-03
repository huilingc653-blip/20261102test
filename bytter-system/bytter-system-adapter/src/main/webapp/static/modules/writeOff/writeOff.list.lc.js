/** 信用证相关逻辑 **/
var tbLc;
function getLcData() {
    if (null == tbLc) {
        tbLc = bt.datatable.initMultiTables({
            elem: "#appTableLc",
            menuName: '核销处理',
            isInitData: false,
            checkbox: true,
            mulselect: true,
            selectCss: true,
            jumpPageSelect: false,//开启跨页选择
            searchForm: {
                formElem: "#searchFormLc",
                formFilter: "lcSearch"
            },
            order: [[1, 'desc']],
            ajax: {
                'url': basePath + "/accountReceivable/writeOff/lcDatatable"
            },
            buttons: [
                {
                    extend: "colvis",
                    text: bt.lang('common.button.theadHide'),
                    columns: ':not(:first):not(:last)'
                }
            ],
            columns: [
                {data: "arrivalCode", bSortable: true,
                    render: function (data, type, full, meta) {
                        if ("1" != full.flag) {
                            var row = tbLc.row(meta.row).node();
                            $(row).addClass("row_bg");
                        }
                    return full.arrivalCode;
                    }
                },
                {data: "corpName", bSortable: true},
                {data: "creditNo", bSortable: true},
                {data: "lcInter", bSortable: true,
                    render: function (data, type, full, meta) {
                        return bt.dict("letter_of_credit_inter", full.lcInter);
                    }
                },
                {data: "handDocBank", bSortable: true},
                //{data: "beneficiary", bSortable: true},
                {data: "corpName", bSortable: true, className: "center"},
                {
                    data: "billMoney", bSortable: true, className: "textRight",
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.billMoney);
                    }
                },
                {
                    data: "payMoney", bSortable: true, className: "textRight",
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.payMoney);
                    }
                },
                {
                    data: "incomingMoney", bSortable: true, className: "textRight",
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.incomingMoney);
                    }
                },
                {
                    data: "writeOffBal", bSortable: true, className: "textRight",
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.writeOffBal);
                    }
                },
                {
                    data: "mortgageMoney", bSortable: true, className: "textRight",
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.mortgageMoney);
                    }
                },
                {
                    data: "negotiationMoney", bSortable: true, className: "textRight",
                    render: function (data, type, full, meta) {
                        return bt.money.format(full.negotiationMoney);
                    }
                },
                {data: "curName", bSortable: true},
                {data: "paymentDate", bSortable: true, className: "center"},
                {data: "corpName", bSortable: true},
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
//                checkRow(0, true, $("#appTableLc"));
            },
            btClickRowCallback: function (data1) {
                forceButtonDisabled(); //控制强制核销按钮是否禁用
            }
        });
    } else {
        bt.datatable.refreshTable("appTableLc");
    }
}

function clearLcForm(){
    corp.get("lc_eqs_corpId").val("");
    dict.get('lc_eqs_lcInter').val("");
    $("#lc_likes_creditNo").val("");
    dateType.get("lc_ged_paymentDate").val("");
    dateType.get("lc_led_paymentDate").val("");
    selectType.get("lc_eqs_curId").val("");
    dict.get('lc_ins_writeOffStatus').attr({'value':'0,1'});
    dict.get('lc_eqs_writeOffType').val("");
}


function tableInitLc(data){
    if(data){
        var writeOffStatus = data.writeOffStatus;
        if (writeOffStatus == '2' && data.writeOffId) {//已核销
            $("#eqs_lc_id").val(data.writeOffId);
        }else {
            $("#eqs_lc_id").val("");
        }
        getLcData();
    }
}

function lcCorpCallback(id){

}


//获取手工核销流水数据
function getManualLcData(){
    var manualLcData = {
        flag: true,
        msg: ""
    }
    if (tbLc) {
        var table = $('#appTableLc').DataTable();
        var lcSelData = table.rows('.selected').data();
        if (lcSelData.length == 0) {
            manualLcData.flag = false;
            manualLcData.msg = "请选择信用证数据！";
        }else {
            var lcSelId = "";
            var lcSelMoney = 0;
            var lcSelCurName = lcSelData[0].curName;
            var lcSelWriteOffStatus="";
            for (var j = 0; j < lcSelData.length; j++) {
                if(lcSelData.writeOffStatus == '2'){
                    manualLcData.flag = false;
                    manualLcData.msg = "请选择未核销或部分核销的信用证数据！";
                    break;
                }
                if(lcSelData.length>1 && lcSelData[j].curName!=lcSelCurName){
                    manualLcData.flag = false;
                    manualLcData.msg = "多条信用证必须同一币种！";
                    break;
                }
                //带确认逻辑
                /*if(lcSelData.length>1 && lcSelData[j].writeOffStatus=='1'){
                    manualLcData.flag = false;
                    manualLcData.msg = "手工核销不支持多对多！";
                    break;
                }*/
                lcSelMoney = bt.money.add(lcSelMoney, lcSelData[j].writeOffBal);
                lcSelWriteOffStatus = lcSelData[j].writeOffStatus;
                if(j === 0){
                    lcSelId += lcSelData[j].id;
                }else {
                    lcSelId += "," + lcSelData[j].id;
                }
            }
            if(manualLcData.flag){
                manualLcData.relationId = lcSelId;
                manualLcData.relationMoney = lcSelMoney;
                manualLcData.length = lcSelData.length;
                manualLcData.relationCurName = lcSelCurName;
                manualLcData.relationWriteOffStatus = lcSelWriteOffStatus;
            }
        }
    }else {
        manualLcData.flag = false;
        manualLcData.msg = "请选择信用证数据！";
    }
    return manualLcData;
}