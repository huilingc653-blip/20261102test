var dataTable;
bt.use(['form', 'datatable', 'corp', 'btImport', 'dateType', 'money', 'dicts', 'selectType', 'accountTable','bank'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    bt.dicts.init();
    bt.bank.init({
        elem:".bank",
    });
    var selectType = bt.selectType.init();

    /**
     * 选择账户回调
     * @param data
     */
    accountCallBack = function (data) {
        $("#likes_bankAcc").val(data.bankAcc);
        selectType.get("eqs_curId").val(data.curId);
    };
    var accountTable = bt.accountTable.init();

    /**
     * 选择单位回调
     * @param id
     */
    corpCallback = function (id) {
        // 重新根据单位ID加载账户表格
        accountTable.get("likes_bankAcc").setCorp(id);
    };
    bt.corp.init();

    /**
     * 导入功能
     */
    importExcel = function () {
        up = bt.btImport.init({
            code: "10000006",
            hideExportBtn: true,
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/bankacc/bisaccbal/saveImport",
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
    };

    /**
     * 生成列表
     */
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        order: [[6, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bankacc/bisaccbal/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:bankAcc:bisAccBal:apply'),
                text: bt.lang('bankAcc.button.balInit'),
                action: function (e, dt, node, config) {
                    initBal();
                }
            },
            {
                hide: bt.isnotResource('netbank:bankAcc:bisAccBal:apply'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            },
           {
                hide: bt.isnotResource('netbank:bankAcc:bisAccBal:apply'),
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            },
            {
                hide: bt.isnotResource('netbank:bankAcc:bisAccBal:apply'),
                text: bt.lang('common.button.batchSubmit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    submitBatch(data);
                }
            }, {
                hide: bt.isnotResource('netbank:bankAcc:bisAccBal:apply'),
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    deleteBatch(data);
                }
            },{
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
            {data: "bankName", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "curName", bSortable: true},
            {data: "balDate", bSortable: true},
            {
                data: "bal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.bal);
                }
            },
            {
                data: "availBal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.availBal);
                }
            },
            {
                data: "frzBal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.frzBal);
                }
            },
            {
                data: "status", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("cgsBill.status", full.status);
                }
            },
            {data: "getDate", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                var html = '';
                if (bt.isResource('netbank:bankAcc:bisAccBal:apply',full.corpId)) {
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("netbank.button.change") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode">|</span>';
                }
                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                return html;
            }
            }
        ],
        summary: {
            pageSum: false,
            allSum: true,
            selectSum: true,
            sumTextCol:2,
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '7',
                    dataName: 'bal'
                }
            ]
        }
    });
});

function initBal() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('bankAcc.button.balInit'), basePath + "/bankacc/bisaccbal/initBal");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/bisaccbal/balInit",
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('table.bisAccBal.btn.create'), basePath + "/bankacc/bisaccbal/add?addFlag=0");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/bisaccbal/add?addFlag=0",
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/bisaccbal/view/" + data.id
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('table.bisAccBal.btn.update'), basePath + "/bankacc/bisaccbal/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bankacc/bisaccbal/update?id=" + data.id,
            end: function () {
                bt.datatable.refreshCurrentPage();
            }
        });
    }
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
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'), function () {
        $.post(basePath + "/bankacc/bisaccbal/batchDelete", {
            ids: ids
        }, function (result) {
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}


//批量提交
function submitBatch(data) {
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids="";
    var rowData = data.splice(0,len);
    for(var i=0;i<rowData.length;i++){
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'),function(){
        $.post(basePath + "/bankacc/bisaccbal/batchSubmit",{ids:ids}, function(result){
            datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
//删除
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/bankacc/bisaccbal/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}