var table;
bt.use(['form', 'datatable', 'selectType', 'corp', 'btImport', 'bank', 'dicts', 'dateType','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.selectType.init();
    var mulselectType=bt.mulselect.init();
    bt.bank.init({
        elem: '.bank',
    });
    bt.dateType.init();
    importExcel = function () {
        up = bt.btImport.init({
            code: "10000008",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/bankacc/btbankacchandle/saveImport",
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

    //生成列表
    table = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bankacc/btbankacchandle/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:bankAcc:btBankAcc:create'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                hide: bt.isnotResource('netbank:bankAcc:btBankAcc:apply'),
                text: bt.lang('table.btBankAcc.batchSetBillInfo'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchSetBillInfo(data);
                }
            }, {
                hide: bt.isnotResource('netbank:bankAcc:btBankAcc:apply'),
                text: bt.lang('设置非直联'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchSetOnline(data);
                }
            }, {
                hide: bt.isnotResource('netbank:bankAcc:btBankAcc:apply'),
                text: bt.lang("common.button.imports"),
                action: function (e, dt, node, config) {
                    importExcel();
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
        columns: [
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "curName", bSortable: false},
            {
                data: "rates", bSortable: true,
                render: function (data, type, full, meta) {
                    if (full.rates) {
                        return bt.money.mul(full.rates,100,4)+"%";
                    }
                    return "";
                }
            },
            {data: "electricBillName", bSortable: true,
                render: function(data, type, full, meta){
                    return bt.dict("sf_yn", full.electricBill)
                }
            },
            {data: "circulationModeName", bSortable: true,
                render: function(data, type, full, meta){
                    return bt.dict("bankAcc.circulationMode", full.circulationMode)
                }
            },
            {data: "isDomesticBankName",bSortable:true,
                render: function(data, type, full, meta){
                    return bt.dict("btBankAcc.isDomesticBank", full.isDomesticBank)
                }
            },
            {data: "accTypeName", bSortable: true,
                render: function(data, type, full, meta){
                    return bt.dict("btBankAcc.accType", full.accType)
                }
            },
            {data: "attributeName", bSortable: true},
            {data: "natureName", bSortable: true},
            {data: "bankTypeName", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "regDate", bSortable: true},
            {
                data: "isFrom", bSortable: true, render: function (data, type, full, meta) {
                    if ("0" == full.isFrom) {
                        return bt.lang("table.btBankAcc.app");
                    } else {
                        return bt.lang("table.btBankAcc.autonomy");
                    }
                }
            },
            {data: "isOnlineStr", bSortable: true,
                render: function(data, type, full, meta){
                    return bt.dict("sf_yn", full.isOnline)
                }
            },
            {data: "validSignStr", bSortable: true,
                render: function(data, type, full, meta){
                    return bt.dict("bankAcc.validSign", full.validSign)
                }
            },
            {data: "statusStr", bSortable: true,
                render: function(data, type, full, meta){
                    return bt.dict("bankAcc.handleStatu", full.status)
                }
            },
            {data: "accUseName", bSortable: false},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if ("1" == full.isFrom) {
                        if (bt.isResource('netbank:bankAcc:btBankAcc:update',full.corpId) && full.isClose != "1") {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                        }
                        if (bt.isResource('netbank:bankAcc:btBankAcc:delete',full.corpId) && full.isClose != "1") {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                        }
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    } else {
                        if (bt.isResource('netbank:bankAcc:btBankAcc:apply',full.corpId)) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update2(this)">' + bt.lang("common.button.openaccount") + '</a><span class="text-explode">|</span>';
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn1(this)">' + bt.lang("common.button.repulse") + '</a><span class="text-explode">|</span>';
                        }
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view1(this)">' + bt.lang("common.button.view") + '</a>';
                        // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    }
                    return html;
                }
            }
        ]
    });
});

/**
 * 新增
 */
function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/bankacc/btbankacchandle/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankacchandle/add",
        end: function () {
            location.reload();
        }
    });
}

/**
 * 账户查看
 * @param el
 */
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankacchandle/view/" + data.id
    });
}

/**
 * 账户申请查询
 * @param el
 */
function view1(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankaccapp/view/" + data.id
    });
}

/**
 * 开户办理
 * @param el
 */
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/bankacc/btbankacchandle/update?id=" + data.id + "&isFrom=" + data.isFrom);
            return;
        }
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankacchandle/update?id=" + data.id,
        end: function () {
            location.reload();
        }
    });
}


/**
 * 开户办理2
 * @param el
 */
function update2(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/bankacc/btbankacchandle/update?id=" + data.id + "&isFrom=" + data.isFrom+"&flag=kh");
            return;
        }
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankacchandle/update?id=" + data.id +"&flag=kh",
        end: function () {
            location.reload();
        }
    });
}

/**
 * 账户删除
 * @param el
 */
function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/bankacc/btbankacchandle/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

/**
 * 打回
 * @param el
 */
function deleteColumn1(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.repulse'), function () {
        $.post(basePath + "/bankacc/btbankacc/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//票据账户设置
function batchSetBillInfo(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var accId = "";
    for (var i = 0; i < rowData.length; i++) {
        if(bt.empty(rowData[i].bankAcc)){
            layer.alert(bt.lang("basic.business.message.bankacc.billinfo.batchset"), {icon: 7});
            return;
        }
        if(!bt.empty(rowData[i].bankAcc) && "95" != rowData[i].status && "100" != rowData[i].status){
            layer.alert(bt.lang("basic.business.message.bankacc.billinfo.batchset"), {icon: 7});
            return;
        }
        if(!bt.empty(rowData[i].accType) && "10" == rowData[i].accType){
            layer.alert(bt.lang("table.btBankAcc.electric.isNotSet"), {icon: 7});
            return;
        }
       if(i == 0){
           accId = rowData[i].id;
       }else{
           accId += "," + rowData[i].id;
       }
    }
    layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankacchandle/toSetBillInfo/" + accId
    });
}

// 设置非直联
function batchSetOnline(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var accId = "";
    for (var i = 0; i < rowData.length; i++) {
        if ('0' == rowData[i].isClose){
            layer.alert(bt.lang(rowData[i].bankAcc+"为非销户流程中的账号，请直接编辑。"), {icon: 7});
            return;
        }
        if(i == 0){
            accId = rowData[i].id;
        }else{
            accId += "," + rowData[i].id;
        }
    }
    var msg = bt.lang('您确认设置非直联吗？');
    parent.layer.confirm(msg, function () {
        $.post(basePath + "/bankacc/btbankacchandle/batchUnOnline", {
            ids: accId
        }, function (result) {
            table.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });

}

/**判断字符串是否为空*/
bt.empty = function(ms){
    if(typeof(ms) == "undefined" || ms == null || ms == "" || ms == " "){
        return true;
    }
    return false;
}

function validateAccUseCode(element) {
    var $element = $(element);

    // 获取隐藏的选中值字段（class 为 txtSel 的 hidden input）
    var $hiddenInput = $element.find('input[type="hidden"].txtSel[name="accUseCode"]');
    var selectedValue = $hiddenInput.val();

    // 获取显示的输入框（通常在隐藏域下方）
    var $textInput = $element.find('input[type="text"]:not(.layui-input-disabled)');

    console.log('选中 Code:', selectedValue);
    console.log('输入值:', $textInput.val());

    // 如果隐藏域的 value 为空，清空下方的 input
    if (!selectedValue || selectedValue === '') {
        $textInput.val('');
        console.log('已清空输入框');
    }
}