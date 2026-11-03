var datatable;
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
    var payerAccInit = bt.selectTable.init({
        elem: "#payerAccTable",
        url: basePath + "/netbank/nisbillhead/getPayerAccQuery",
        urldata: {resource:"netbank:netBank:nisBillHead:rpa:query"},
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
        $.ajax({
            url: basePath + "/netbank/niscorpbus.do?getNisCorpBus",
            method: 'POST',
            data: {corpId: id, cdSign: '1'},
            dataType: 'json',
            success: function (data) {
                $("#busType").empty();
                $("#busType").append("<option value=' '>" + bt.lang("common.placeholder.select") + "</option>");
                if (data && data.obj && data.obj.length > 0) {
                    $.each(data.obj, function (index, item) {
                        $("#busType").append("<option value=" + item.busId + ">" + item.busTypeName + "</option>");
                    });
                } else {
                    $("#busType").append("<option value=''></option>");
                }
                form.render('select');
            }
        });
        //获取有权限的用户
        var createBy = mulselect.get("createBy");
        if(createBy){
            var url = basePath+"/netbank/nisbillhead/getUserByCorpId?corpId="+id;
            createBy.attr({ "data-url":url });
            createBy.render();
        }
    }
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.dicts.init();
    bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [[1, 'asc'], [13, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/netbank/nisbillhead/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:netBank:nisBillHead:rpa:apply'),
                text: bt.lang('common.button.batchBack'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchBack(data);
                }
            },
            {
                hide: bt.isnotResource('netbank:netBank:nisBillHead:rpa:apply'),
                text: bt.lang('netbank.button.batchUpdateVoucherStat'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchUpdate(data);
                }
            },
            {
                hide: bt.isnotResource('netbank:netBank:nisBillHead:rpa:apply'),
                text: bt.lang('netbank.button.batchUpdateRPAVoucherStat'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchUpdateExceptionStatus(data);
                }
            },
            {
                hide: bt.isnotResource('netbank:netBank:nisBillHead:rpa:apply'),
                text: bt.lang('netbank.button.batchUpdateReturnMsg'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchUpdateReturnMsg(data);
                }
            },
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
            {data: "payerAcc", bSortable: true},
            {data: "payerAccName", bSortable: true},
            {data: "payerBank", bSortable: true},
            {data: "curName", bSortable: true},
            {
                data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.payMoney);
                }
            },
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
            {
                data: "voucherStatName", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("rpa.voucherStat", full.voucherStat);
                }
            },
            {data: "transTime", bSortable: true, className: 'center'},
            {data: "returnMsg", bSortable: true, className: 'center'},
            {data: "isForindividualName", bSortable: true, className: 'center'},
            {data: "backTypeName", bSortable: true, className: 'center'},
            {data: "createName", bSortable: false, className: 'center'},
            {data: "createDate", bSortable: true, className: 'center'},
            {data: "approveUser", bSortable: false, className: 'center'},
            {data: "approveDate", bSortable: false, className: 'center'},
            {
                data: null,
                bSortable: false,
                anCells: true,
                className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if (full.voucherStat && full.voucherStat == "2" && bt.isResource('netbank:netBank:nisBillHead:rpa:apply', full.corpId)) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="back(this)">' + bt.lang("common.button.back") + '</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="updateVoucherStat(this)">' + bt.lang("netbank.button.batchUpdateVoucherStat") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
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
                    colNum: '8',
                    dataName: 'payMoney'
                }
            ]
        },
        // 暂时注释导出全部回调
        customExportAll: function (params, processing, data) {
            var exportUrl = basePath + "/netbank/nisbillhead/exportAll";
            openExportExcel(exportUrl, data);
            processing(false);
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
});
//打回
function back(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data.voucherStat != "2") {
        layer.alert(bt.lang('netbank.message.rpa.voucherStat.fail.back'), {icon: 7});
        return;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.back'), function () {
        $.post(basePath + "/netbank/nisbillhead/batchBackRPA", {
            ids: data.id
        }, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
//修改交易状态
function updateVoucherStat(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data.voucherStat != "2") {
        layer.alert(bt.lang('netbank.message.rpa.voucherStat.fail.update'), {icon: 7});
        return;
    }
    parent.layer.confirm(bt.lang('nis.message.confirm.updateVoucherStat'), function () {
        $.post(basePath + "/netbank/nisbillhead/batchUpdateVoucherStatRPA", {
            ids: data.id
        }, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
//批量退回
function batchBack(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (rowData[i].voucherStat != "2") {
            layer.alert(bt.lang('netbank.message.rpa.voucherStat.fail.back'), {icon: 7});
            return;
        }
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.back'), function () {
        $.post(basePath + "/netbank/nisbillhead/batchBackRPA", {
            ids: ids
        }, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
//批量修改交易状态
function batchUpdate(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (rowData[i].voucherStat != "2") {
            layer.alert(bt.lang('netbank.message.rpa.voucherStat.fail.update'), {icon: 7});
            return;
        }
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('nis.message.confirm.updateVoucherStat'), function () {
        $.post(basePath + "/netbank/nisbillhead/batchUpdateVoucherStatRPA", {
            ids: ids
        }, function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
function batchUpdateExceptionStatus(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids = "";
    var rowData = data.splice(0, len);
    for (var i = 0; i < rowData.length; i++) {
        if (rowData[i].voucherStat != "Y") {
            layer.alert(bt.lang('netbank.message.rpa.voucherStat.fail.update'), {icon: 7});
            return;
        }
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/nisbillhead/updateExceptionStatus?ids="+ids,
        end: function () {

        }
    });
}
function batchUpdateReturnMsg(data) {
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
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/netbank/nisbillhead/batchUpdateReturnMsg?ids="+ids,
        end: function () {

        }
    });
}
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
