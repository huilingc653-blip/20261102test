var datatable,dateFun,moneyType,moneyFun,delNode;
bt.use(['form', 'datatable', 'select', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'btImport', 'bank', 'selectType','mulselect' ,'element','selectTable','accebillInfoReceive'], function () {
    $ = bt.jquery, form = bt.form();
    moneyType = bt.moneyType.init();
    moneyFun = bt.moneyType.getFun();
    bt.mulselect.init();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.selectType.init();
    dateFun = bt.dateType.getFun();
    bt.dicts.init();
    bt.bank.init({
        elem: '.bank',
    });
    var accebillInfoReceive = bt.accebillInfoReceive.init();
    // 应收票据
    bt.accebillInfoReceiveParams = function () {
        accebillInfoReceive.set({
            "corpId": $("#acceBillCorpId").val(),
            "isEmitBill": 0,
            "isPayed":'0',//是否已付款
            "ppFlg":"1",
            "batchPay":"1", //批量选票标识
            "batchPayMoney":$("#acceBillMoney").val(),//批量选票付款金额
        });
        return true;
    }
    bt.accebillInfoCallback = function (datas, rom) {
        if (datas && datas.ids) {
            var billId = datas.ids;
            //获取选择的数据
            var data = datatable.rows('.selected').data();
            if (data.length) {
                var ids = "";
                var len = data.length;
                var rowData = data.splice(0, len);
                for (var i = 0; i < rowData.length; i++) {
                    if (ids == "") {
                        ids += rowData[i].id;
                    } else {
                        ids += "," + rowData[i].id;
                    }
                }
                $.post(basePath + "/netbank/nisbillhead/batchSelectAccebill", {
                    ids: ids,
                    billId: billId
                }, function (result) {
                    //datatable.draw();
                    parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                }, 'JSON');
            }
        }
    }
    //付款账号
    var payerAccCol = [
        {data: "bankAcc",},
        {data: "accName",},
        {data: "bankName",},
        {data: "curName",}
    ];
    var payerAccInit = bt.selectTable.init({
        elem: "#payerAccTable",
        url: basePath + "/netbank/nisbillhead/getPayerAccQuery",
        urldata: {resource:'netbank:netBank:nisBillHead:query'},
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
    //根据是否挂 计划 选择不同的模板
    var importCode = "10000005";
    if ($("#budgetSel").val() === "1") {
        importCode = "100000051";
    }

    importExcel = function () {
        up = bt.btImport.init({
            code: importCode,
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            hideExportBtn:true,
            summaryHtml: function (data) {
                var successHtml = '';
                var errorHtml = '';
                var successSum = 0;
                var failSum = 0;
                if (data && data.successData.length) {
                    data.successData.forEach(function (item, index, arr) {
                        successSum = moneyFun.add(successSum, item.payMoney);
                    });
                    successHtml = '<div>金额汇总：<span>' + moneyFun.format(successSum) + '</span></div>';
                }
                if (data && data.errorData.length) {
                    data.errorData.forEach(function (item, index, arr) {
                        failSum = moneyFun.add(failSum, item.payMoney);
                    });
                    errorHtml = '<div>金额汇总：<span>' + moneyFun.format(failSum) + '</span></div>';
                }
                return {successHtml: successHtml, errorHtml: errorHtml}
            },
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/netbank/nisbillhead/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(up.getSelectData()),
                    success: function (result) {
                        bt.datatable.refreshCurrentPage();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }
                });
            }
        });
    }

    //导入初始化
    importExcelBill = function () {
        up = bt.btImport.init({
            code: "1008001Pay",
            titleRows: 0,
            width: "90%",
            height: "90%",
            headRows: 1,
            autoCloseWindown:false,
            pageLengthMenu:[10,50,100,500,2000],
            pageLen:500,
            hideExportBtn:true,
            success: function (head, data, select) {
                bt.loading.show();
                $.ajax({
                    type: "POST",
                    url: basePath + "/bill/accebill/abemitbill/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(select),
                    //重写success方法
                    success: function (result) {
                        bt.loading.hide();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                        if(result.success){
                            up.closeWindown();
                            bt.datatable.refreshCurrentPage();
                            abemitbillInfoList(select);
                        }
                    },
                    error:function(){
                        bt.loading.hide();
                    }
                });
            }
        });
    }

    //跳转开票登记列表数据
    abemitbillInfoList = function (select) {
        top.payImportOrigNo="";
        if(select){
            for(var i=0;i<select.length;i++){
                if(top.payImportOrigNo){
                    top.payImportOrigNo+=",";
                }
                top.payImportOrigNo+=select[i].origNo;
            }
        if(bt.navtab){
            bt.navtab.addNavtab('付款应付票据导入列表',basePath + "/bill/accebill/abemitbill/list?payImport=1" );
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['1120px', '800px'],
            maxmin: true,
            content: basePath + "/bill/accebill/abemitbill/list?payImport=1"
        });
        }
    }

    function createDatatableColumns(){
        var columns = [
            {
                data: "billNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if (full.status == -1) {
                        var row = datatable.row(meta.row).node();
                        $(row).addClass("row_bg");
                    }
                    return data;
                }
            },
            {data: "schedulingElementsName", bSortable: true,className: 'center',visible:$("#enablePendingPaymentPool").val() === "1"},
            {data: "schedulingDate", bSortable: true,className: 'center',visible:$("#enablePendingPaymentPool").val() === "1"},
            {data: "corpName", bSortable: true},
            {data: "voucherName", bSortable: true},
            {data: "payerAcc", bSortable: true,
                render: function (data, type, full, meta) {
                    if(!data){
                        data = '';
                    }
                    return '<div title="可用余额： ' + bt.money.format(full.availBal) + ' "> '+ data || "" +' </div>'
                }
            },
            {data: "payerAccName", bSortable: true},
            {data: "payerBank", bSortable: true},
            {data: "curName", bSortable: true},
            {
                data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    //证书信息
                    var cfcdatanfo = '<input type="hidden" id="random' + full.id + '" name="random" value=""><input type="hidden" id="encrySource' + full.id + '" name="encrySource" value=""><input type="hidden" id="caCoding' + full.id + '" name="caCoding" value="">';
                    var html = '';
                    html = cfcdatanfo + bt.money.format(full.payMoney);
                    return html;
                }
            },
            // {data:"billTypeName",bSortable:true},
            // {data:"billFlagName",bSortable:true},
            {data: "payeeAcc", bSortable: true},
            {data: "payeeAccName", bSortable: true},
            {data: "payeeBankCode", bSortable: true},
            {data: "payeeBank", bSortable: true},
            {data: "blRcvpymtpsUnnIdNo", bSortable: true},
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
            {data: "billFromName", bSortable: true, className: 'center'},
            {data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("nisBillhead.status",full.status);
                }
            },
            {
                data: "landingLimitResult", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("enterpriseLimit.landingResult", full.landingLimitResult);
                }
            },
            {data: "isForindividualName", bSortable: true, className: 'center'},
            {data: "isSelectedAcceBillName", bSortable: true, className: 'center'},
            {data: "createDate", bSortable: true, className: 'center', format: ""},
            {data: "opCode", bSortable: true, className: 'center'},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('netbank:netBank:nisBillHead:update', full.corpId) && full.status != -3) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    }
                    if (bt.isResource('netbank:netBank:nisBillHead:delete', full.corpId)) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    }
                    if ($("#isEnableSplit").val() == 1) {
                        if (bt.isResource('netbank:netBank:nisBillHead:mergeAndSplit', full.corpId)) {
                            if (full.splitId && full.splitId != "") {
                                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="splitCancel(this)">' + bt.lang("common.button.splitcancel") + '</a><span class="text-explode">|</span>';
                            }
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="split(this)">' + bt.lang("common.button.split") + '</a><span class="text-explode">|</span>';
                        }
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    return html;
                }
            }
        ]
        return columns.filter(function (col) {
            return col.visible ===undefined || col.visible
        })
    }

    //生成列表
    datatable = bt.datatable.init({
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
                hide: bt.isnotResource('netbank:netBank:nisBillHead:add'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                hide: bt.isnotResource('netbank:netBank:nisBillHead:submitBatch'),
                text: bt.lang('common.button.submit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    isBatchNoSubmit(data,false);
                    // isHasDemand(data);
                    //校验黑名单
                    // corpCheck(data);
                    // submitBatch(data);
                    // limitBankAcc(data);
                }
            }, {
                hide: bt.isnotResource('netbank:netBank:nisBillHead:submitBatch'),
                text: bt.lang('common.button.batchNoSubmit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    isBatchNoSubmit(data,true);
                    // isHasDemand(data);
                    //校验黑名单
                    // corpCheck(data);
                    // submitBatch(data);
                    // limitBankAcc(data);
                }
            },{
                hide: bt.isnotResource('netbank:netBank:nisBillHead:delete'),
                text: bt.lang('common.button.batchDelete'),
                className: 'batch-delete-btn active',
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    deleteBatch(data);
                }
            },{
                hide: bt.isnotResource('netbank:netBank:nisBillHead:submitBatch'),
                text: '暂挂',
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    pendingBatch(data);
                }
            }, {
                hide: bt.isnotResource('netbank:netBank:nisBillHead:submitBatch'),
                text: '解除暂挂',
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    relievePendingBatch(data);
                }
            },{
                hide: $("#isEnableMerge").val() != 1 ? true : bt.isnotResource('netbank:netBank:nisBillHead:mergeAndSplit'),
                text: bt.lang('common.button.merge'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    merge(data);
                }
            }, {
                hide: $("#isEnableMerge").val() != 1 ? true : bt.isnotResource('netbank:netBank:nisBillHead:mergeAndSplit'),
                text: bt.lang('common.button.mergecancel'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    mergeCancel(data);
                }
            }, {
                hide: bt.isnotResource('netbank:netBank:nisBillHead:add'),
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            }, {
                hide: bt.isnotResource('netbank:netBank:nisBillHead:add'),
                text: '导入自开票据',
                action: function (e, dt, node, config) {
                    importExcelBill();
                }
            }, {
                hide: bt.isnotResource('netbank:netBank:nisBillHead:batchSetPayerAcc'),
                text: bt.lang('common.button.batchSetPayerAcc'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchSetPayerAcc(data);
                }
            }, {
                hide: bt.isnotResource('netbank:netBank:nisBillHead:batchSetPayerAcc'),
                text: bt.lang('批量设置票据信息'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchSetOpenBillInfo(data);
                }
            }, {
                hide: bt.isnotResource('netbank:netBank:nisBillHead:batchSelectAccebill'),
                text: bt.lang('netbank.button.batchSelectAccebill'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchSelectAccebill(data);
                }
            }, {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns:createDatatableColumns(),
        insertInformation: function (settings) { //底部插入信息回调方法，返回html字符串
            var showHTML =  '已选笔数: '+bt.datatable.getSelectData('#appTable').length || 0;
            return '<div style="color: red" <span id="sumMoneyBillSpan" style="color: red">' + showHTML + '</span></div>';
        },
        btClickRowCallback:function(){
            $("#sumMoneyBillSpan").text('已选笔数: '+bt.datatable.getSelectData('#appTable').length || 0)
        },
        summary: {
            pageSum: true,
            allSum: false,
            selectSum: true,
            groupColumn:'curName',
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '8',
                    dataName: 'payMoney'
                }
            ]
        }
        //,
        // 暂时注释导出全部回调
        //customExportAll: function (params, processing, data) {
        //    var exportUrl = basePath + "/netbank/nisbillhead/exportAll";
        //    openExportExcel(exportUrl, data);
        //    processing(false);
        //}
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

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/netbank/nisbillhead/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/nisbillhead/add",
        end: function () {
            datatable && datatable.draw();
        }
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/netbank/nisbillhead/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
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

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/netbank/nisbillhead/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/netbank/nisbillhead/update?id=" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

function deleteColumn(el) {
    delNode = el;
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var chargeBackType = $("#chargeBackType").val();
    if (data.billFrom == "6" && chargeBackType.trim() == "") {
        parent.layer.open({
            type: 2,
            area: ['500px','250px'],
            maxmin: true,
            content: basePath + "/netbank/nisbillhead/chargeBackType"
        });
        return;
    } else {
        if (chargeBackType.trim() == "") {
            //默认不允许为空
            chargeBackType = "-1";
        }
    }
    //删除明细单据校验
    var result = validSplitDel(data);
    var msg = bt.lang('common.message.confirm.delete');
    if(result){
        msg = bt.lang('nis.message.splitdtl.confirm.delete', [data.billNo]);
    }
    var status = data.status;
    var billNo = data.billNo;
    if(status == -3){
        parent.layer?parent.layer.closeAll():layer.closeAll();
        layer.alert("单号"+billNo+"状态为暂挂不能删除！", {icon: 7});
        return false;
    }
    parent.layer.confirm(msg, function () {
        $.ajax({
            type: 'DELETE',
            dataType: 'json',
            url: basePath + "/netbank/nisbillhead/delete/" + data.id + "/" + chargeBackType,
            success: function(result){
                datatable && datatable.draw();
                parent.layer?parent.layer.closeAll():layer.closeAll();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }
        });
    });
}

//账务校验
function validSplitDel(data) {
    var result = "";
    if(data.splitId) {
        $.ajax({
            url: basePath + "/netbank/nisbillhead?checkSplitDtl",
            method: 'POST',
            async: false,
            data: {
                splitId: data.splitId
            },
            dataType: 'json',
            success: function (data) {
                if (data.obj) {
                    result = data.obj;
                }
            }
        });
    }
    return result;
}

function split(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if(data.busBillType == "YF" || data.busBillType == "FK" || data.busBillType == "BJ"){
            layer.alert("共享结算单据不能拆分！", {icon: 7});
            return;
        }
        //系统结算方式为50-应收票据，不可进行拆分
        /*if (data.sysVoucherCode == "50") {
            layer.alert(bt.lang('nis.pay.message.bill.split'), {icon: 7});
            return;
        }*/
        //单据来源为承兑汇票，不可进行拆分
        if (data.billFrom == "3") {
            layer.alert(bt.lang('nis.pay.message.billfrom.bill.split'), {icon: 7});
            return;
        }
        $.ajax({
            url: basePath + "/netbank/nisbillhead?isMergeBill",
            method: 'POST',
            async: false,
            data: {
                id: data.id
            },
            dataType: 'json',
            success: function (result) {
                if (result.success) {
                    layer.alert(result.msg, {icon: 2});
                    return;
                }
                if (bt.navtab) {
                    bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/netbank/nisbillhead/split?id=" + data.id);
                    return;
                }
                parent.layer.open({
                    type: 2,
                    area: ['850px', '530px'],
                    maxmin: true,
                    content: basePath + "/netbank/nisbillhead/split?id=" + data.id,
                    end: function () {
                        datatable && datatable.draw();
                    }
                });
            }
        });
    }
}

function splitCancel(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.splitcancel'), function () {
        $.post(basePath + "/netbank/nisbillhead/splitCancel/" + data.id, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//批量删除
function deleteBatch(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    var chargeBackType = $("#chargeBackType").val();
    var billNos = "";
    for (var i = 0; i < rowData.length; i++) {
        if (rowData[i].billFrom == "6" && chargeBackType.trim() == "") {
            parent.layer.open({
                type: 2,
                area: ['500px','250px'],
                maxmin: true,
                content: basePath + "/netbank/nisbillhead/chargeBackType?type=batch"
            });
            return;
        }
        var status = rowData[i].status;
        var billNo = rowData[i].billNo;
        if(status == -3){
            layer.alert("单号"+billNo+"状态为暂挂不能删除！", {icon: 7});
            return false;
        }
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
        //删除明细单据校验
        var result = validSplitDel(rowData[i]);
        if(result){
            if (billNos == "") {
                billNos += rowData[i].billNo;
            } else {
                billNos += "," + rowData[i].billNo;
            }
        }
    }
    var msg = bt.lang('common.message.confirm.batchDelete');
    if(result){
        msg = bt.lang('nis.message.splitdtl.confirm.delete', [billNos]);
    }
    parent.layer.confirm(msg, function () {
        $.post(basePath + "/netbank/nisbillhead/batchDelete", {
            ids: ids,
            chargeBackType: chargeBackType
        }, function (result) {
            datatable.draw();
            parent.layer?parent.layer.closeAll():layer.closeAll();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//批量暂挂
function pendingBatch(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm('是否批量暂挂？', function () {
        $.post(basePath + "/netbank/nisbillhead/pendingBatch", {
            ids: ids
        }, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//批量解除暂挂
function relievePendingBatch(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm('是否批量解除暂挂？', function () {
        $.post(basePath + "/netbank/nisbillhead/relievePendingBatch", {
            ids: ids
        }, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//批量提交
function submitBatch(rowData) {
    var ids = "";
    var signDataMsg = "";
    var randoms = '';
    var sourceDatas = '';
    var caCodings = '';
    var billNos = "";
    var hasBillPayFlag = false;//票据付款标志
    //当前日期
    var curDate = new Date();
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }

        var sysVoucherCode = rowData[i].sysVoucherCode;
        if (sysVoucherCode === "49" || sysVoucherCode === "50") {
            hasBillPayFlag = true;
        }

        //判断期望支付日期（根据支付结算参数是否控制期望支付日期）
        if (dateFun.compare(rowData[i].wishPayday, curDate) > 0) {
            billNos = billNos + rowData[i].billNo + ",";
        }
        //性能考虑：批量提交证书验签第一笔
        if (i == 0) {
            var key = rowData[i].id;
            //数据源规则：付款账号+随机数（10位）+金额（14位）+期望支付日期+收款人+收款账号
            var sourceData = rowData[i].payerAcc;//付款账号
            var random = String(parseInt(Math.random() * 10000000000));//随机数
            while (random.length < 10) {
                random = random + '0';
            }
            $("#random" + key).val(random);
            var payMoney = rowData[i].payMoney + "";//金额
            while (payMoney.length < 14) {
                payMoney = '0' + payMoney;
            }
            var wishPayday = dateFun.format(rowData[i].wishPayday, 'yyyyMMdd');//期望支付日期
            sourceData += random + payMoney + wishPayday + rowData[i].payeeAccName + rowData[i].payeeAcc;
            $("#encrySource" + key).val(sourceData);
            signDataMsg = batchSignData(sourceData, "caCoding" + key, "2");
            if (randoms == "") {
                randoms += random;
            } else {
                randoms += "," + random;
            }
            if (sourceDatas == "") {
                sourceDatas += sourceData;
            } else {
                sourceDatas += "," + sourceData;
            }
            if (caCodings == "") {
                caCodings += $("#caCoding" + key).val();
            } else {
                caCodings += "," + $("#caCoding" + key).val();
            }
        }
        if (signDataMsg != "") {
            bt.loading.hide();
            layer.alert(bt.lang(signDataMsg), {icon: 2});
            return false;
        }
    }
    if (billNos != "") {
        billNos = billNos.substring(0,(billNos.length-1));
        layer.confirm(bt.lang('nis.message.wishpayday.pass',[billNos]), {icon: 3}, function () {
            // 票据付款则无需校验金额
            if (!hasBillPayFlag) {
                checkBalance(ids, randoms, sourceDatas, caCodings, rowData);
            } else {
                rePay(ids, randoms, sourceDatas, caCodings, rowData);
            }
        });
    } else {
        if (!hasBillPayFlag) {
            checkBalance(ids, randoms, sourceDatas, caCodings, rowData);
        } else {
            rePay(ids, randoms, sourceDatas, caCodings, rowData);
        }
    }
    return false;
}

/**
 * 校验账户余额
 * @param data
 * @returns {boolean}
 */
checkBalance = function (ids, randoms, sourceDatas, caCodings, rowData) {
    //账户余额校验
    var accountOver = $("#accountOver").val();//内部账户余额不足： 0-不提醒 1-只提醒不控制 2-控制
    var bankaccOver = $("#bankaccOver").val();//银行账户余额不足： 0-不提醒 1-只提醒不控制 2-控制
    //付款金额大于账户余额
    var flag = false;
    var msg = "";
    $.ajax({
        url: basePath + "/netbank/nisbillhead/checkBal",
        method: 'Post',
        async: false,
        data: {ids:ids},
        dataType: 'json',
        success: function (da) {
            msg = da.obj;
        }
    });

    if(msg !== "") {
        msg = msg.replaceAll("{","<");
        msg = msg.replaceAll("}",">");
        if ((bankaccOver === "2" || accountOver === "2")) {
            var content = "<div style='height: 30px;line-height: 30px;padding-left: 16px;' class='msg-box'><i class=\"layui-layer-ico layui-layer-ico2\"  style='position: relative;top: auto;left: auto;'></i><div style='padding-left: 10px;display: inline-block;'>" + bt.lang("netbank.message.balance") + "</div></div>";
            parent.layer.alert(content + msg, {title:"余额不足提示",area: ['850px', '530px']});
            return false;
        } else if (bankaccOver === "1" || accountOver === "1") {
            flag = true;
        }
    }
    if (flag) {
        bt.loading.hide();
        var content = "<div style='height: 30px;line-height: 30px;adding-left: 16px;' class='msg-box'><i class=\"layui-layer-ico layui-layer-ico0\"  style='position: relative;top: auto;left: auto;'></i><div style='padding-left: 10px;display: inline-block;'>" + bt.lang("netbank.message.confirm.balance") + "</div></div>";
        parent.layer.confirm(content + msg, {title:"余额不足提示",area: ['850px', '530px']}, function (index) {
            bt.loading.show();
            rePay(ids, randoms, sourceDatas, caCodings, rowData);
        });
    } else {
        rePay(ids, randoms, sourceDatas, caCodings, rowData);
    }
    return false;
}

/**
 * 校验付款方式为应收票据时，不能选背书申请的票据，需重新选票
 * @param rowData
 * @returns {boolean}
 */
checkReceiveBill = function (rowData) {
    var ids = "";
    for (var i = 0; i < rowData.length; i++) {
        if (ids === "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    var msg = "";
    $.ajax({
        url: basePath + "/netbank/nisbillhead/checkReceiveBill",
        method: 'Post',
        async: false,
        data: {ids:ids},
        dataType: 'json',
        success: function (da) {
            msg = da.obj;
        }
    });

    if(msg !== "") {
        msg = msg.replaceAll("{","<");
        msg = msg.replaceAll("}",">");
        var content = "<div style='height: 30px;line-height: 30px;padding-left: 16px;' class='msg-box'><i class=\"layui-layer-ico layui-layer-ico2\"  style='position: relative;top: auto;left: auto;'></i>" +
                        "<div style='padding-left: 10px;display: inline-block;'>以下付款单号对应的票据号已做业务或被锁定状态，请编辑重新选票</div>" +
                      "</div>";
        return content + msg
    }
    return "";
}

/**限额begin*/
//单位限额
function limitCorp(rowData,msg){
    var newArr = arrayConversionForCorp(rowData);//将所有单位数据进行合并，计算金额总和
    var noticeBankAcc1 = [];//日累计
    var noticeBankAcc2 = [];//月累计

    //依次遍历每个单位
    for(var i=0;i<newArr.length;i++){
        var result=0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/limitForCorp",
            method: 'GET',
            async: false,
            data: {curId: newArr[i].curId, payMoney: newArr[i].payMoney, corpId: newArr[i].corpId,wishPayDate: newArr[i].wishPayday},
            dataType: 'json',
            success: function (da) {
                result = da.obj;
            }
        });
        //1：日累计；2：月累计
        if (result=== 1) {
            noticeBankAcc1.push(newArr[i]);
        } else if(result === 2){
            noticeBankAcc2.push(newArr[i]);
        }
    }
    if(noticeBankAcc1.length>0){
        noticeBankAcc1 = arrayFilterForCorp(noticeBankAcc1);//单位去重
        var bankAcc1='';
        for(var i =0;i<noticeBankAcc1.length;i++){
            bankAcc1+=noticeBankAcc1[i].corpName+"，";
        }
        bankAcc1=bankAcc1.substring(0,bankAcc1.length-1)
        msg+= bt.lang("alert.over.daylimit.msg.corp")+'<br>'+bankAcc1 +'<br><br>';
    }
    if(noticeBankAcc2.length>0){
        noticeBankAcc2 = arrayFilterForCorp(noticeBankAcc2);//单位去重
        noticeBankAcc2 = arrayExcludeForCorp(noticeBankAcc2,noticeBankAcc1);//如果该单位已存在日累计，则排除该单位于月累计
        var bankAcc2='';
        for(var i =0;i<noticeBankAcc2.length;i++){
            bankAcc2+=noticeBankAcc2[i].corpName+"，";
        }
        if(bankAcc2 != null && bankAcc2 != '' && bankAcc2.length!=0){
            bankAcc2=bankAcc2.substring(0,bankAcc2.length-1)
            msg+= bt.lang("alert.over.monthlimit.msg.corp")+'<br>'+bankAcc2+'<br><br>';
        }
    }
    if(msg.length>0){
        msg+= bt.lang("billPool.isContinue")
        parent.layer.confirm(msg, {icon: 3, title:bt.lang('fbs.budget.input.message.info'),area: ['500px','300px']},function (index) {
            //原批量提交代码逻辑
            submitBatch(rowData);
        });
    }else{
        submitBatch(rowData);
    }
}

//是否批量提交应付票据判断
function isBatchNoSubmit(res,isBatchNoSubmit) {
    var resTemp = res.slice();
    $('#isBatchNoSubmit').val(isBatchNoSubmit);
    var hasEmitVoucherType = false;
    var len = res.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    //获取并组装结算方式为应付票据的结算方式名称
    var voucherName ="";
    $.ajax({
        url: basePath + "/netbank/fcvouchertype/getValidVoucherType",
        method: 'GET',
        async: false,
        dataType: 'json',
        success: function (data) {
            if(data !=null && data.length != 0){
                for(var i=0;i<data.length;i++) {
                    if(data[i].sysVoucherCode== "49"){
                        voucherName +=data[i].voucherName;
                        voucherName +=",";
                    }
                }
                voucherName = voucherName.substring(0,voucherName.length - 1);
            }else{
                voucherName ="应付票据";
            }
        }
    });

    var billPayFlag = false;// 提交时，判断是否同时存在票据和非票据的数据
    var noBillPayFlag = false;// 提交时，判断是否同时存在票据和非票据的数据
    var rowData = res.splice(0, len);
    for(var i=0;i<rowData.length;i++) {
        var sysVoucherCode = rowData[i].sysVoucherCode;
        if(isBatchNoSubmit && sysVoucherCode != "49"){
            layer.alert("此功能按钮针对付款方式为【"+voucherName+"】的付款单数据，其他付款单不能使用", {icon: 7});
            return false;
        }
        if(sysVoucherCode == "49"){
            hasEmitVoucherType = true;
        }
        if (sysVoucherCode === '49' || sysVoucherCode === '50') {
            billPayFlag = true; // 存在票据付款
        } else {
            noBillPayFlag = true; // 存在非票据付款
        }
    }
    if(hasEmitVoucherType && !isBatchNoSubmit){
        parent.layer.confirm("此提交票据走单笔开票接口",function(){
            if (!isBatchNoSubmit && billPayFlag && noBillPayFlag) {
                parent.layer.confirm("存在票据和非票据的数据，是否确认是否提交数据",function(){
                    isHasDemand(resTemp, isBatchNoSubmit);
                });
            } else {
                isHasDemand(resTemp, isBatchNoSubmit);
            }
        });
    }else{
        if (!isBatchNoSubmit && billPayFlag && noBillPayFlag) {
            parent.layer.confirm("存在票据和非票据的数据，是否确认是否提交数据",function(){
                isHasDemand(resTemp, isBatchNoSubmit);
            });
        } else {
            isHasDemand(resTemp, isBatchNoSubmit);
        }
    }
}

function isHasDemand(res, isBatchNoSubmit) {
    var len = res.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = res.splice(0, len);
    for(var i=0;i<rowData.length;i++) {
        var status = rowData[i].status;
        var billNo = rowData[i].billNo;
        if(status == -3){
            layer.alert("单号"+billNo+"状态为暂挂不能提交！", {icon: 7});
            return false;
        }
    }

    // 校验付款方式为应收票据时，不能选背书申请的票据，需重新选票
    if (!isBatchNoSubmit) {
        var msg = checkReceiveBill(rowData)
        if (msg !== "") {
            parent.layer.alert(msg, {title:"温馨提示", area: ['850px', '530px']});
            return false;
        }
    }

    var demandControl = $("#demandControl").val();
    if(demandControl == "0"){
        corpCheck(rowData);
    }else if(demandControl == "1" || demandControl == "2"){
        var result = false;
        var billNo = "";
        for(var i=0;i<rowData.length;i++) {
            //付款方式 联动支付的不受参数控制
            var  sysVoucherCode = rowData[i].sysVoucherCode;
            var payAcc = rowData[i].payerAcc;
            var curId = rowData[i].curId;
            console.log(payAcc);
            console.log(curId);
            if(sysVoucherCode == "34") {
                $.ajax({
                    url: basePath + "/netbank/nisdemand/isHasDemand",
                    method: 'GET',
                    async: false,
                    data: {id: rowData[i].id,payAcc:payAcc,curId:curId},
                    dataType: 'json',
                    success: function (da) {
                        if (!da.success) {
                            result = true;
                            billNo += rowData[i].billNo;
                        }
                    }
                });
            }
        }
        //如果没有需求单
        if(result){
            if(demandControl == "2"){
                layer.alert("单号"+billNo+"没有生成需求清单！", {icon: 7});
                return false;
            }
            if(demandControl == "1"){
                parent.layer.confirm("单号"+billNo+"没有生成需求清单！",function(){
                    corpCheck(rowData);
                });
            }
        }else{
            corpCheck(rowData);
        }
    }

}
//校验黑白名单
function corpCheck(rowData){
    var params=[];
    for (var i = 0; i < rowData.length; i++) {
        var param = {};
        param.name = rowData[i].payeeAccName;
        param.appNo = rowData[i].billNo;
        param.corpId = rowData[i].corpId;
        params.push(param);
    }
    var data = {};
    data.params = params;
    //对黑白名单进行验证
    var reData=externalCorpCheckBatch(data);
    if(reData.reCode=='3'){
        bt.loading.hide();
        parent.layer?parent.layer.alert(reData.msg, {icon: 2}):layer.alert(reData.msg, {icon: 2});
        return;
    }else if(reData.reCode=='2'){
        bt.loading.hide();
        parent.layer.confirm(reData.msg,function(){
            if($("#isOpenLimit").val()=="1") {
                limitBankAcc(rowData);
            }else{
                submitBatch(rowData);
            }
        });
    }else{
        if($("#isOpenLimit").val()=="1") {
            limitBankAcc(rowData);
        }else {
            submitBatch(rowData);
        }
    }
}
//账号限额
function limitBankAcc(rowData){
    var noticeBankAcc1 = [];//单次付款校验的账号
    for(var i=0;i<rowData.length;i++){
        var result=0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/limit",
            method: 'GET',
            async: false,
            data: {curId: rowData[i].curId, payMoney: rowData[i].payMoney, payerAcc: rowData[i].payerAcc,wishPayDate: rowData[i].wishPayday},
            dataType: 'json',
            success: function (da) {
                result = da.obj;
            }
        });
        if (result=== 1) {
            noticeBankAcc1.push(rowData[i]);
        }
    }
    noticeBankAcc1 = arrayConversion(noticeBankAcc1);
    var newArr = arrayConversion(rowData);

    var noticeBankAcc2 = [];//日累计
    var noticeBankAcc3 = [];//月累计
    for(var i=0;i<newArr.length;i++){
        var result=0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/limit",
            method: 'GET',
            async: false,
            data: {curId: newArr[i].curId, payMoney: newArr[i].payMoney, payerAcc: newArr[i].payerAcc,wishPayDate: newArr[i].wishPayday},
            dataType: 'json',
            success: function (da) {
                result = da.obj;
            }
        });
        if (result=== 2) {
            noticeBankAcc2.push(newArr[i]);
        } else if(result === 3){
            noticeBankAcc3.push(newArr[i]);
        }
    }
    var msg='' ;
    if(noticeBankAcc1.length>0){
        noticeBankAcc1 = arrayFilterForBankAcc(noticeBankAcc1);
        var bankAcc1='';
        for(var i =0;i<noticeBankAcc1.length;i++){
            bankAcc1+=noticeBankAcc1[i].payerAcc+"，";
        }
        bankAcc1=bankAcc1.substring(0,bankAcc1.length-1)
        msg+= bt.lang("alert.over.paylimit.msg")+'<br>'+bankAcc1 +'<br><br>';
    }
    if(noticeBankAcc2.length>0){
        noticeBankAcc2 = arrayFilterForBankAcc(noticeBankAcc2);
        noticeBankAcc2 = arrayExcludeForBankAcc(noticeBankAcc2,noticeBankAcc1);
        var bankAcc2='';
        for(var i =0;i<noticeBankAcc2.length;i++){
            bankAcc2+=noticeBankAcc2[i].payerAcc+"，";
        }
        if(bankAcc2 != null && bankAcc2 != '' && bankAcc2.length!=0){
            bankAcc2=bankAcc2.substring(0,bankAcc2.length-1)
            msg+= bt.lang("alert.over.daylimit.msg")+'<br>'+bankAcc2+'<br><br>';
        }
    }
    if(noticeBankAcc3.length>0){
        noticeBankAcc3 = arrayFilterForBankAcc(noticeBankAcc3);
        noticeBankAcc3 = arrayExcludeForBankAcc(noticeBankAcc3,noticeBankAcc1);
        noticeBankAcc3 = arrayExcludeForBankAcc(noticeBankAcc3,noticeBankAcc2);
        var bankAcc3='';
        for(var i =0;i<noticeBankAcc3.length;i++){
            bankAcc3+=noticeBankAcc3[i].payerAcc+"，";
        }
        if(bankAcc3 != null && bankAcc3 != '' && bankAcc3.length!=0){
            bankAcc3=bankAcc3.substring(0,bankAcc3.length-1)
            msg+= bt.lang("alert.over.monthlimit.msg")+'<br>'+bankAcc3+'<br><br>';
        }
    }
    limitCorp(rowData,msg);
}

//过滤数组与数组分类-账号
function arrayConversion(array){
    var temp= {}
    var newArr = [];
    for(var i = 0; i < array.length; i++){
        //获取单位本位币换算汇率
        var result = 0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/standardRate",
            method: 'GET',
            async: false,
            data: {curId: array[i].curId, corpId: array[i].corpId},
            dataType: 'json',
            success: function (data) {
                result = bt.money.mul(array[i].payMoney,data,2)
            }
        });
        result = parseFloat(result);

        //开始分离
        var rData = array[i];
        if(!temp[rData.payerAcc+rData.wishPayday]){
            newArr.push({
                payerAcc: rData.payerAcc,
                payMoney: result,
                wishPayday: rData.wishPayday
            });
            temp[rData.payerAcc+rData.wishPayday] = rData;
        }else{
            for(var j = 0; j < newArr.length; j++){
                var sum = newArr [j];
                if(sum.payerAcc+sum.wishPayday === rData.payerAcc+rData.wishPayday){
                    sum.payMoney=(parseFloat(sum.payMoney) + result).toString();
                    break;
                }
            }
        }
    };
    return newArr;
}
//过滤数组与数组分类-单位
function arrayConversionForCorp(array){
    var temp= {}
    var newArr = [];
    for(var i = 0; i < array.length; i++){
        //获取单位本位币换算汇率
        var result = 0;
        $.ajax({
            url: basePath + "/netbank/nisbillhead/standardRate",
            method: 'GET',
            async: false,
            data: {curId: array[i].curId, corpId: array[i].corpId},
            dataType: 'json',
            success: function (data) {
                result = bt.money.mul(array[i].payMoney,data,2)
            }
        });
        result = parseFloat(result);
        var rData = array[i];
        if(!temp[rData.corpName+rData.wishPayday]){
            newArr.push({
                corpName: rData.corpName,
                payMoney : result,
                corpId : rData.corpId,
                wishPayday: rData.wishPayday
            });
            temp[rData.corpName+rData.wishPayday] = rData;
        }else{
            for(var j = 0; j < newArr.length; j++){
                var sum = newArr [j];
                if(sum.corpName+sum.wishPayday === rData.corpName+rData.wishPayday){
                    sum.payMoney=(parseFloat(sum.payMoney) + result).toString();
                    break;
                }
            }
        }
    };
    return newArr;
}

//单位数组去重
function arrayFilterForCorp(array){
    var temp= {}
    var newArr = [];
    for(var i = 0; i < array.length; i++){
        var rData = array[i];
        if(!temp[rData.corpName]){
            newArr.push(rData);
            temp[rData.corpName] = rData.corpName;
        }
    };
    return newArr;
}
//单位数组排除
function arrayExcludeForCorp(array1,array2){
    var newArr = [];
    for(var i = 0; i < array1.length; i++){
        var flag = 0;
        for(var j = 0; j < array2.length; j++){
            if(array1[i].corpName !== array2[j].corpName){
                flag++;
            }
        }
        if(array2.length === 0 || flag === array2.length){
            newArr.push(array1[i]);
        }
    }
    return newArr;
}
//账号去重
function arrayFilterForBankAcc(array){
    var temp= {}
    var newArr = [];
    for(var i = 0; i < array.length; i++){
        var rData = array[i];
        if(!temp[rData.payerAcc]){
            newArr.push(rData);
            temp[rData.payerAcc] = rData.payerAcc;
        }
    };
    return newArr;
}
//账号数组排除
function arrayExcludeForBankAcc(array1,array2){
    var newArr = [];
    for(var i = 0; i < array1.length; i++){
        var flag = 0;
        for(var j = 0; j < array2.length; j++){
            if(array1[i].payerAcc !== array2[j].payerAcc){
                flag++;
            }
        }
        if(array2.length === 0 || flag === array2.length){
            newArr.push(array1[i]);
        }
    }
    return newArr;
}
/**限额end*/

//批量选择付款账号
function batchSetPayerAcc(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var corpId = "";
    var voucherType = "";
    var voucherTypeId = "";
    var editPayerAcc = $("#editPayerAcc").val();
    for (var i = 0; i < rowData.length; i++) {
        //校验付款单位是否一致
        if (corpId == "") {
            corpId = rowData[i].corpId;
        } else {
            if (corpId != rowData[i].corpId) {
                layer.alert(bt.lang('nis.setpayeracc.message.corp.same'), {icon: 7});
                return false;
            }
        }

        //校验付款方式是否一致--改成校验系统付款方式
        if (voucherType == "") {
            voucherType = rowData[i].sysVoucherCode;
            voucherTypeId = rowData[i].voucherTypeId;
        } else {
            if (voucherType != rowData[i].sysVoucherCode) {
                layer.alert(bt.lang('nis.setpayeracc.message.vouchertype.same'), {icon: 7});
                return false;
            }
        }

        //校验是否有erp且不允许编辑金额的数据
        if(editPayerAcc == "0"){
            billFrom = rowData[i].billFrom;
            if(billFrom == "6"){
                layer.alert(bt.lang('nis.setpayeracc.message.billfrom.erp'), {icon: 7});
                return false;
            }
        }
    }
    layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/nisbillhead/toSetPayerAcc/" + corpId + "/" + voucherTypeId
    });
}

function batchSelectAccebill(data) {
    //同单位，应收票据，是否已付款为否
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var corpId = "";
    var payMoney = 0;
    for (var i = 0; i < rowData.length; i++) {
        //校验付款单位是否一致
        if (corpId == "") {
            corpId = rowData[i].corpId;
        } else {
            if (corpId != rowData[i].corpId) {
                layer.alert(bt.lang('nis.selectaccebill.message.corp.same'), {icon: 7});
                return false;
            }
        }
        //校验结算方式是否是应收票据
        if (rowData[i].sysVoucherCode != "50") {
            layer.alert(bt.lang('nis.selectaccebill.message.vouchertype'), {icon: 7});
            return false;
        }
        //校验是否已付款必须为否
        if (rowData[i].isPayed == "1") {
            layer.alert(bt.lang('nis.selectaccebill.message.ispayed'), {icon: 7});
            return false;
        }
        payMoney = moneyFun.add(payMoney, rowData[i].payMoney);
    }
    $('#acceBillCorpId').val(corpId);
    $('#acceBillMoney').val(payMoney);
    $('#accebillInfoBut').trigger("click");
}

//重复付款校验
function rePay(ids, randoms, sourceDatas, caCodings, rowData) {
    bt.loading.show();
    var result = false;
    var msg = "";
    $.ajax({
        url: basePath + "/netbank/nisbillhead?batchRePay",
        method: 'POST',
        async: false,
        data: {
            ids: ids
        },
        dataType: 'json',
        success: function (data) {
            result = data.success;
            msg = "1" == data.obj ? "netbank.message.confirm.repay" : "netbank.message.confirm.repay.temp";
        }
    });
    if (result) {
        bt.loading.hide();
        parent.layer.confirm(bt.lang(msg), function (index) {
            parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
                if (caCodings == "") {
                    for (var i = 0; i < rowData.length; i++) {
                        var key = rowData[i].id;
                        if (caCodings == "") {
                            caCodings += $("#caCoding" + key).val();
                        } else {
                            caCodings += "," + $("#caCoding" + key).val();
                        }
                    }
                }
                bt.loading.show();
                $.post(basePath + "/netbank/nisbillhead/batchSubmit", {
                    ids: ids,
                    randoms: randoms,
                    sourceDatas: sourceDatas,
                    caCodings: caCodings,
                    isBatchNoSubmit:  $('#isBatchNoSubmit').val()
                }, function (result) {
                    bt.loading.hide();
                    var icon = result.success ? (result.attributes ? result.attributes.succIcon : 1) : 2;
                    datatable.draw();
                    showBatchSubmitResult(result, icon);
                }, 'JSON');
            });
        });
    } else {
        bt.loading.hide();
        parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
            if (caCodings == "") {
                for (var i = 0; i < rowData.length; i++) {
                    var key = rowData[i].id;
                    if (caCodings == "") {
                        caCodings += $("#caCoding" + key).val();
                    } else {
                        caCodings += "," + $("#caCoding" + key).val();
                    }
                }
            }
            bt.loading.show();
            $.post(basePath + "/netbank/nisbillhead/batchSubmit", {
                ids: ids,
                randoms: randoms,
                sourceDatas: sourceDatas,
                caCodings: caCodings,
                isBatchNoSubmit:  $('#isBatchNoSubmit').val()
            }, function (result) {
                bt.loading.hide();
                var icon = result.success ? (result.attributes ? result.attributes.succIcon : 1) : 2;
                datatable.draw();
                showBatchSubmitResult(result, icon);
            }, 'JSON');
        });
    }
    return false;
}

function showBatchSubmitResult(result, icon) {
    if (paymentLimitAlert.isBatchResult(result)) {
        parent.layer.alert(paymentLimitAlert.batchContent(result), paymentLimitAlert.batchAlertOptions());
        return;
    }
    parent.layer.alert(result.msg, {icon: icon});
}


//合并
function merge(data) {
    var len = data.length;
    if (len < 2) {
        layer.alert(bt.lang('netbank.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.merge'), function () {
        $.post(basePath + "/netbank/nisbillhead/merge", {ids: ids}, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//撤销合并
function mergeCancel(data) {
    var len = data.length;
    if (len < 1) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.mergecancel'), function () {
        $.post(basePath + "/netbank/nisbillhead/mergeCancel", {ids: ids}, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//业务种类初始化前回调
// busBefore = function (data,arguments) {
//     var show = "("+arguments.busType+")" + arguments.busTypeName;
//     return show;
// }

function openBillPage(voucherId,voucherType) {
    parent.layer.open({
        type: 2,
        area: ['1200px','600px'],
        maxmin: true,
        content: basePath + "/netbank/nisbillhead/billList?voucherId="+sendId+"&voucherTypeId="+voucherType
    });
}

function getVoucherIds(){
    var data = bt.datatable.getSelectData('#appTable')
    var rowData = data.splice(0, data.length);
    var voucherId = "";
    for (var i = 0; i < rowData.length; i++) {
        if(voucherId == ""){
            voucherId += rowData[i].id;
        }else {
            voucherId += ","+rowData[i].id;
        }
    }
    return  voucherId
}
/**
 * 开票页面弹窗回调方法
 * @param id
 */
function openBillResult(openBillInfo){

}

var sendId = "";
function batchSetOpenBillInfo(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var voucherType = "";
    var sysVoucherCode = "";
    var isPayed = '';
    var voucherId = "";
    var corpId = "";
    for (var i = 0; i < rowData.length; i++) {

        //校验单位是否一致
        if(corpId == ""){
            corpId = rowData[i].corpId;
        }else {
            if (corpId != rowData[i].corpId) {
                layer.alert('批量设置票据信息时，单位必须一致', {icon: 7});
                return false;
            }
        }

        //校验付款方式是否一致
        if (voucherType == "") {
            voucherType = rowData[i].voucherTypeId;
        } else {
            if (voucherType != rowData[i].voucherTypeId) {
                layer.alert(bt.lang('nis.setpayeracc.message.vouchertype.same'), {icon: 7});
                return false;
            }
        }

        sysVoucherCode = rowData[i].sysVoucherCode;
        isPayed = rowData[i].isPayed;
        if(voucherId == ""){
            sendId = rowData[i].id;
            voucherId = rowData[i].id;
        }else {
            voucherId = rowData[i].id+","+voucherId;
        }
        if(rowData[i].payerAcc){
            sendId = rowData[i].id;
        }else if("" == sendId){
            sendId = rowData[0].id;
        }
    }
    if(sysVoucherCode != '49' && '0' == isPayed){
        layer.alert('未付款的付款方式为应付票据的付款单才需要完善票据信息', {icon: 7});
        return false;
    }
    openBillPage(voucherId,voucherType);
}

function chargeBackTypeCallback(chargeBackType) {
    if(chargeBackType) {
        $("#chargeBackType").val(chargeBackType);
        // 模拟点击
       /// $('.batch-delete-btn').trigger('click');

        var  selected = bt.datatable.getSelectData('#appTable');
        deleteBatch(selected);
        $("#chargeBackType").val("");
    }
}
function chargeCallback(chargeBackType) {
    if(chargeBackType) {
        $("#chargeBackType").val(chargeBackType);
        deleteColumn(delNode);
        $("#chargeBackType").val("");
    }
}

function setEndMoney(data){
    moneyType.get("leg_payMoney").val(data.value);
}
