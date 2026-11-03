var moneyType;
bt.use(['form', 'datatable', 'select', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'bank', 'selectType', 'element','mulselect','selectTable'], function () {
    $ = bt.jquery, form = bt.form();
    var mulselect = bt.mulselect.init();
    //付款账号
    var payerAccCol = [
        {data: "bankAcc",},
        {data: "accName",},
        {data: "bankName",},
        {data: "curName",}
    ];
    var opType = $("#opType").val();
    var resource = 'netbank:netBankQuery:nisBillHead:query';
    if (opType == "queryDel") {
        resource = 'netbank:netBankQueryDel:nisBillHead:query';
    }
    var payerAccInit = bt.selectTable.init({
        elem: "#payerAccTable",
        url: basePath + "/netbank/nisbillhead/getPayerAccQuery",
        urldata: {resource:resource},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#eqs_payerAcc",
        paging: true,
        columns: payerAccCol,
        clickCallback: function (data) {
            if (data) {
                $('#eqs_payerAcc').val(data.bankAcc);
            } else {
                $('input.text').val('');
            }
        }
    });
    //单位回调函数
    bt.corpCallback = function (id) {
        //获取单位业务类别
        // $.ajax({
        //     url: basePath + "/netbank/niscorpbus?getNisCorpBus",
        //     method: 'POST',
        //     data: {corpId: id, cdSign: '1'},
        //     dataType: 'json',
        //     success: function (data) {
        //         $("#busType").empty();
        //         $("#busType").append("<option value=' '>" + bt.lang("common.placeholder.select") + "</option>");
        //         if (data && data.obj && data.obj.length > 0) {
        //             $.each(data.obj, function (index, item) {
        //                 $("#busType").append("<option value=" + item.busId + ">" + item.busTypeName + "</option>");
        //             });
        //         } else {
        //             $("#busType").append("<option value=''></option>");
        //         }
        //         form.render('select');
        //     }
        // });
        //获取有权限的用户
        var createBy = mulselect.get("createBy");
        if(createBy){
            var url = basePath+"/netbank/nisbillhead/getUserByCorpId?corpId="+id;
            createBy.attr({ "data-url":url });
            createBy.render();
        }
    }
    // //获取单位业务类别
    // $.ajax({
    //     url: basePath + "/netbank/niscorpbus?getNisCorpBusByCdSign",
    //     method: 'POST',
    //     data: {cdSign: '1'},
    //     dataType: 'json',
    //     success: function (data) {
    //         $("#busType").empty();
    //         $("#busType").append("<option value=' '>" + bt.lang("common.placeholder.select") + "</option>");
    //         if (data && data.obj && data.obj.length > 0) {
    //             $.each(data.obj, function (index, item) {
    //                 $("#busType").append("<option value=" + item.busId + ">" + item.busTypeName + "</option>");
    //             });
    //         } else {
    //             $("#busType").append("<option value=''></option>");
    //         }
    //         form.render('select');
    //     }
    // });
    bt.corp.init();
    moneyType = bt.moneyType.init();
    bt.dateType.init();
    bt.dicts.init();
    bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [[1, 'asc'], [13, 'desc']],
        pageSizeOptions:[10, 25, 50, 100, 500,1000,2000,5000],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/netbank/nisbillhead/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.batchPrint'),
                action: function (e, dt, node, config) {
                    batchPrint(e, dt, node, config);
                }
            }, {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            },
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "billNo", bSortable: true, className: 'center'},
            {data: "corpName", bSortable: true},
            {data: "voucherName", bSortable: true},
            {data: "payerAcc", bSortable: true,
                render: function (data, type, full, meta) {
                    var val = data;
                    if(val == 'null' || val == null){
                        val = "";
                    }
                    return '<div title="可用余额： ' + bt.money.format(full.availBal) + ' "> '+ val || "" +' </div>'
                }
            },
            {data: "payerAccName", bSortable: true},
            {data: "payerBank", bSortable: true},
            {data: "curName", bSortable: true},
            {
                data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.payMoney);
                }
            },
            // {data:"billType",bSortable:true,
            //     render: function (data, type, full, meta) {
            //         return bt.dict("AB_BILL_TYPE",full.billType);
            //     }
            // },
            // {data:"billFlag",bSortable:true,
            //     render: function (data, type, full, meta) {
            //         return bt.dict("AB_BILL_FLAG",full.billFlag);
            //     }
            // },
            {data: "payeeAcc", bSortable: true},
            {data: "payeeAccName", bSortable: true},
            {data: "payeeBankCode", bSortable: true},
            {data: "payeeBank", bSortable: true},
            {data: "wishPayday", bSortable: true, className: 'center'},
            {
                data: "splitBillNo", bSortable: true, className: 'center', visible: setSplitBillNoVisible(),
                render: function (data, type, full, meta) {
                    var html = '';
                    if (full.splitBillNo != null && full.splitBillNo != "") {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewSplit(this)">' + full.splitBillNo + '</a>';
                    }
                    return html;
                }
            },
            {data: "busTypeName", bSortable: true},
            {data: "bursarBillNo", bSortable: true},
            {data: "purpose", bSortable: true},
            {data: "addWord", bSortable: true},
            {data: "isPayedName", bSortable: true, className: 'center', visible: setIsPayedVisible()},
            {data: "billFromName", bSortable: true, className: 'center',},
            {data: "statusName", bSortable: true, className: 'center',render: function ( data, type, full, meta ) {
                    return bt.dict("nisBillhead.status",full.status);
                }
            },
            {data: "landingLimitResult", bSortable: true, className: 'center',render: function ( data, type, full, meta ) {
                    return bt.dict("enterpriseLimit.landingResult",full.landingLimitResult);
                }
            },
            {data: "voucherStatName", bSortable: true, className: 'center',render: function ( data, type, full, meta ) {
                    return bt.dict("bisExc.voucherStat",full.voucherStat);
                }
            },
            {data: "nextReturnMsg", bSortable: false, className: 'center'},
            {data: "chargeBackName", bSortable: false, className: 'center'},
            {data: "isAutoPayName", bSortable: true, className: 'center'},
            {data: "isForindividualName", bSortable: true, className: 'center'},
            {data: "isSendGx", bSortable: true, className: 'center',render: function ( data, type, full, meta ) {
                    return bt.dict("nisBillhead.isSendGx",full.isSendGx);
                }
            },
            {
                data: "demandBillNo", bSortable: true, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (full.demandBillNo != null && full.demandBillNo != "") {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewDemand(this)">' + full.demandBillNo + '</a>';
                    }
                    return html;
                }
            },
            {data: "demandVoucherStat", bSortable: true, className: 'center',render: function ( data, type, full, meta ) {
                    return bt.dict("bisExc.voucherStat",full.demandVoucherStat);
                }
            },
            {data: "createBy", bSortable: true, className: 'center'},
            {data: "createDate", bSortable: true, className: 'center'},
            {data: "opCode", bSortable: true, className: 'center'},
            {data: "actualPayDate", bSortable: false, className: 'center'},
            // {data: "sharingDate", bSortable: false, className: 'center'},

            {
                data: null,
                bSortable: false,
                anCells: true,
                className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if ((full.status >= 11 && full.status < 95) && $("#loginName").val() === full.createBy) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="withdraw(this)">' + bt.lang("common.button.withdraw") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    //html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    if (full.bisExcId && full.bisExcId != "" && bt.isResource('netbank:netBankQuery:nisBillHead:bisQuery')) {
                        html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="viewBisExc(this)">' + bt.lang("common.button.viewBisExc") + '</a>';
                    }
                    if (full.tallySign && full.tallySign != "" && full.voucherNo && full.voucherNo != "") {
                        html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="viewVoucher(this)">' + bt.lang("common.button.viewVoucher") + '</a>';
                    }
                    if(full.dtlNum>0){
                        html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="viewdtl(this)">' + bt.lang("common.button.viewFlow") + '</a>';
                        if(full.receiptNum>0){
                            html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="viewreceipt(this)">' + bt.lang("common.button.viewReceipt") + '</a>';
                        }
                    }
                    return html;
                }
            }
        ],
        summary: {
            pageSum: true,
            allSum: false,
            selectSum: false,
            groupColumn:'curName',
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '7',
                    dataName: 'payMoney'
                }
            ]
        },
        // 暂时注释导出全部回调
        customExportAll: function (params, processing, data) {
            var exportUrl = basePath + "/netbank/nisbillhead/exportAll";
            var opType = $("#opType").val();
            if(opType == "queryDel"){
                exportUrl = basePath + "/netbank/nisbillhead/exportAllDel";
            }
            openExportExcel(exportUrl, data);
            processing(false);
        },
        //每行回调函数
        "fnRowCallback": function( nRow, aData, iDisplayIndex, iDisplayIndexFull ) {
            if(aData.voucherStat == "9" || aData.voucherStat == "2"){
                //动态设置class属性
                $(nRow).attr("class","col-error")
            }
        }
    });
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

    /**
     * 撤回
     * @param el
     */
    withdraw = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        if (data) {
            $.post(basePath + "/payment/bpm/task/batchRevoke", {ids: data.id}, function (result) {
                bt.datatable.refreshCurrentPage();
                var msg = result.msg;
                var obj = result.obj;
                // alert("result="+JSON.stringify(result));
                var length = obj.length;
                if (length) {
                    //对报错信息进行转化，显示每个报错信息有多少条
                    for (var a = 0; a < length; a++) {
                        if (obj[a].corpName) {
                            obj[a].errorMsg = [];
                            var messageList = obj[a].messageList.sort();
                            for (var i = 0; i < messageList.length;) {
                                var count = 0;
                                for (var j = i; j < messageList.length; j++) {
                                    if (messageList[i] == messageList[j]) {
                                        count++;
                                    }
                                }
                                obj[a].errorMsg.push([messageList[i].replace(/_/g, '.').toLowerCase(), count]);
                                i += count;
                            }
                        }
                    }
                    //拼装报错信息
                    for (var i = 0; i < length; i++) {
                        if (obj[i].corpName) {
                            msg += obj[i].corpName + '单位有' + obj[i].errorNum + '条撤回失败,原因:';
                            for (var k = 0; k < obj[i].errorMsg.length; k++) {
                                msg += obj[i].errorMsg[k][1] + '条' + bt.lang(obj[i].errorMsg[k][0])
                            }
                        }
                    }
                }
                parent.layer.alert(msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        }
    }
});

function setIsPayedVisible() {
    var showIsPayed = $("#showIsPayed").val();
    if (showIsPayed == "1") {
        return true;
    } else {
        return false;
    }
}

function setSplitBillNoVisible(){
    var isEnableSplit = $("#isEnableSplit").val();
    if (isEnableSplit == "1") {
        return true;
    } else {
        return false;
    }
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/netbank/nisbillhead/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/nisbillhead/view/" + data.id
    });
}

function viewSplit(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/netbank/nisbillhead/view/" + data.splitId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/nisbillhead/view/" + data.splitId
    });
}

function viewDemand(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/netbank/nisdemand/view/" + data.demandId);
        return;
    }
}

//指令查看
function viewBisExc(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/bdlink/bisexc/view/" + data.bisExcId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bdlink/bisexc/view/" + data.bisExcId
    });
}

//凭证查看
function viewVoucher(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/finacial/accbook/fcaccbook/viewCommon/" + data.voucherNo
    });
}

//查看关联的流水
function viewdtl(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var size=data.dtlNum;
    if(size==1){
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bankacc/bisaccdtl/view/" + data.dtlId
        });
    }else {
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/netbank/nisbillhead/viewdtls/"+data.id
        });
    }
}
//查看关联的电子回单
function viewreceipt(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var size=data.receiptNum;
    if(size===1){
        if(data.receiptFlag==='0'){
            parent.layer.open({
                title: false, //不显示标题栏
                type: 2,
                area: ['810px', '450px'],
                closeBtn :0,//不显示关闭按钮
                content: [basePath + "/netbank/nisbillhead/viewReceipt/" + data.receiptId]
                , btn: [bt.lang('common.button.download'), bt.lang('common.button.print'), '<i class="layui-icon">&#x1006;</i> ']
                , yes: function () {
                    window.location = basePath + "/receipt/bisbankreceiptdtl/download?id=" + data.receiptId;
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
                ,btn2: function () {
                    print(data.receiptId);
                }
                , btn3: function () {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            });
        }else if(data.receiptFlag==='1'){
            parent.layer.open({
                title: false, //不显示标题栏
                type: 2,
                area: ['810px', '450px'],
                closeBtn :0,//不显示关闭按钮
                content: [basePath + "/netbank/nisbillhead/viewHisReceipt/" + data.receiptId]
                , btn: [bt.lang('common.button.download'), bt.lang('common.button.print'), '<i class="layui-icon">&#x1006;</i> ']
                , yes: function () {
                    window.location = basePath + "/receipt/bisbankreceiptdtlhis/download?id=" + data.receiptId;
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
                ,btn2: function () {
                    hisprint(data.receiptId);
                }
                , btn3: function () {
                    parent.layer ? parent.layer.closeAll() : layer.closeAll();
                }
            });
        }
        /**
         * 打印电子回单
         * @param id
         */
        function print(id) {
            parent.layer.open({
                type: 2,
                zIndex: -100,
                shade: 0,
                content: basePath + "/netbank/nisbillhead/print?id=" + id
            });
        }
        /**
         * 打印历史电子回单
         * @param id
         */
        function hisprint(id) {
            parent.layer.open({
                type: 2,
                zIndex: -100,
                shade: 0,
                content: basePath + "/netbank/nisbillhead/printHis?id=" + id
            });
        }
    }else {
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/netbank/nisbillhead/viewReceipts/"+data.id
        });
    }
}
/**
 * 批量打印
 * @param e
 * @param dt
 * @param node
 * @param config
 */
function batchPrint(e, dt, node, config) {
    var data = dt.rows('.selected').data();
    if (data.length < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    var loadIndex = layer && layer.load(1, {shade: [0.3, '#fff']});
    var ids = "";
    var rowData = data.splice(0, data.length);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.open({
        type: 2,
        zIndex: -100,
        shade: 0,
        content: basePath + "/netbank/nisbillhead/batchPrintBill?ids=" + ids,
        success:function (layero, index) {
            layer && layer.close(loadIndex);
        }
    });
}

function setEndMoney(data){
    moneyType.get("leg_payMoney").val(data.value);
}
