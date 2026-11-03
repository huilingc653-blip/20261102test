var dict, selectType, accountTable,moneyType,moneyFun,dateType,dateFun,corp;

//业务种类初始化前回调
batchBusTypeBefore = function (data,arguments) {
    var show = "("+arguments.busType+")" + arguments.busTypeName;
    return show;
}

// 选择单位回调
docCorpCallback = function (id) {
    // 重新根据单位ID加载账户表格
    //accountTable.get("receipt_payAccNo").setCorp(id);
    //accountTable.get("receipt_payeeAccNo").setCorp(id);
    var busTypeUrl = basePath + "/netbank/niscorpbus/listNisCorpBusVOByCorpIdAndCdSign?cdSign=0&corpId=" + id;
    bt.selectType.get("doc_eqs_batchBusType").attr("data-url", busTypeUrl);
}

bt.externalCorpBankAccParams = function (data, dom) {
    externalCorpBankAcc.set({
        "corpId": corp.get("doc_eqs_corpId").val() ? corp.get("doc_eqs_corpId").val() : "0",
        type: "2"
    });
    return true;
}



bt.use(['form', 'datatable', 'dateType', 'corp', 'dicts', 'money', 'selectType', 'accountTable', 'bank', 'moneyType', 'mulselect', 'externalCorpBankAcc'], function () {
    var laydate = layui.laydate;
    $ = bt.jquery, form = bt.form();
    dateType = bt.dateType.init();
    dateFun = bt.dateType.getFun();
    moneyType = bt.moneyType.init();
    moneyFun = moneyType.getFun();
    dict = bt.dicts.init();
    bt.mulselect.init();
    bt.bank.init({
        elem: '.bank',
    });
    externalCorpBankAcc = bt.externalCorpBankAcc.init();
    selectType = bt.selectType.init();
    accountTable = bt.accountTable.init();

    // 选择单位回调
    dtlCorpCallback = function (id) {
        // 重新根据单位ID加载账户表格
        accountTable.get("dtl_likes_bankAcc").setCorp(id);
        accountTable.get("dtl_likes_oppAccNo").setCorp(id);
    }
    corp=bt.corp.init();

    //下拉框
    $.each($("div[type='list']"), function (i, item) {
        bt.select.init({
            elem: "[field='" + $(item).attr("field") + "']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            disabled: false,
            url: $(item).attr("fieldhref")
        });
    });

    var appTableDoc = bt.datatable.init({
        elem: "#appTableDoc",
        selectCss: true,
        isInitData: true,
        checkbox: true,
        searchForm: {
            formElem: "#docSearchForm",
            formFilter: "docSearch",
            callback:function(data){ //点击查询按钮的回调方法

            }
        },
        order: [[1, 'desc']],
        ajax: {
            'url': basePath + "/accountReceivable/writeOff/writeOffApproveDatatable"
        },
        buttons: [
           {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "batchNo", className: 'center', bSortable: true},
            {data: "code", className: 'center', bSortable: false,render:
                function (data, type, full, meta) {
                     if(full.code.indexOf(',')>0){
                          return "-";
                     }else{
                        return full.code;
                     }
                }
            },
            {data: "corpName", className: 'center',bSortable: false,render:
               function (data, type, full, meta) {
                    if(full.code.indexOf(',')>0){
                         return "-";
                    }else{
                       return full.corpName;
                    }
               }
            },
            {data: "batchBusTypeName",  className: 'center',bSortable: true,render:
               function (data, type, full, meta) {
                    if(full.code.indexOf(',')>0){
                         return "-";
                    }else{
                       return full.batchBusTypeName;
                    }
               }
            },

            {data: "sourceName",className: 'center', bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("writeOff.docSource", full.source);
                }
            },
            {
                data: "receivableMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                   return bt.money.format(full.receivableMoney);
                }
            },
            {
                data: "writeOffBal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.writeOffBal);
                }
            },
             {data: "executeMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.executeMoney);
                }
            },

            {data: "curName", className: 'center', bSortable: true},
            {data: "exchangeRate", className: 'center', bSortable: false,render:
                    function (data, type, full, meta) {
                         if(full.exchangeRate!=null){
                             return full.exchangeRate;
                         }else{
                            return 1;
                         }
                    }
            },
            {data: "externalName", className: 'center',  bSortable: true,render:
                function (data, type, full, meta) {
                     if(full.code.indexOf(',')>0){
                          return "-";
                     }else{
                        return full.externalName;
                     }
                }
            },
            {data: "writeOffTypeName", className: 'center', bSortable: false,
                render: function (data, type, full, meta) {
                    return bt.dict("writeOff.type", full.writeOffType);
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                     html += '<a class="" href="#" data-row="' + meta.row + '" onclick="detailsInfo(this)">详情</a>&nbsp;|&nbsp;';
                    /*if ($("#flowStatus").val() == 'todo' && (full.writeOffType=='1'||full.writeOffType=='2')) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="approve(this)">' + bt.lang("common.button.approve") + '</a>&nbsp;|&nbsp;';
                    }*/
                    if($("#flowStatus").val() == 'pass'){
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    }else {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.workFlow.trackFlow(\'' + full.processInstanceId + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    }
                    return html;
                }
            }
        ]
    });

});

/*审核*/
function approve(el) {
    var data = layui.jquery("#appTableDoc").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.approve'), basePath + "/accountReceivable/writeOff/approve?businessId=" + data.batchNo + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/accountReceivable/writeOff/approve?businessId=" + data.batchNo + "&taskId=" + data.taskId
    });
}
function detailsInfo(el){
    var data = layui.jquery("#appTableDoc").DataTable().row(layui.jquery(el).data("row")).data();
    var rate=0;
    if(data['exchangeRate']!=null){rate=data['exchangeRate']}
    parent.layer.open({
        type: 2,
        area: ['1400px', '800px'],
        maxmin: true,
        content: basePath + "/accountReceivable/writeOff/infoApproveDetail?writeOffType="+data['writeOffType']+"&docId="+data['docId']+"&batchNo="+data['batchNo']+"&rate="+rate+"&money0="+data['receivableMoney']+"&type="+$.trim(data['paymentMeans'])+"&businessId="+$.trim(data['relationId'])+"&isApprove=true"
    });
}
