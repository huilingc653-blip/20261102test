
bt.use(['form', 'datatable', 'corp', 'selectType', 'dateType', 'dicts', 'btImport', 'mulselect'], function () {
    $ = bt.jquery;
    form = bt.form();
    bt.dicts.init();
    bt.dateType.init(); //日期
    bt.corp.init();
    bt.selectType.init();
    bt.mulselect.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [4, 'desc'],
        ajax: {
            'url': basePath + "/cms/bank/credit/plan/datatable"
        },
        searchForm:{
            formFilter:"search",
            formElem:"#searchForm"
        },
        buttons: [
            {
                hide: bt.isnotResource('cms:bank:credit:plan:add'), //新增权限
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            },
            {
                hide: bt.isnotResource('cms:bank:credit:plan:delete'), //删除权限
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    batchDelete(e, dt, node, config);
                }
            },
            {
                hide: bt.isnotResource('cms:bank:credit:plan:add'), //新增权限
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importCreditBank();
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            { data: "corpName", bSortable: false, className: 'center' },
            { data: "bankName", bSortable: false, className: 'center' },
            {
                data: "creditType", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("cms.bank.credit.plan", data);
                }
            },
            { data: "year", bSortable: true, className: 'center' },
            { data: "curName", bSortable: false, className: 'center' },
            { data: "creditAmount", sortable: true, className: "textRight",
                render: function(data, type, full, meta) { return bt.money.format(full.creditAmount); }
            },
            { data: "status", sortable: false,
                render: function(data, type, full, meta) { return full.status == '1' ? "是" : "否" }
            },
            { data: null, bSortable: false, className: 'center', render: function(data, type, full, meta) {
                    const elements = [];
                    // 拥有编辑权限
                    if (bt.isResource('cms:bank:credit:plan:edit')) {
                        elements.push('<a class="" href="#" data-row="' + meta.row + '" onclick="edit(this)">' + bt.lang("common.button.edit") + '</a>')
                    }
                    // 拥有删除权限
                    if (bt.isResource('cms:bank:credit:plan:delete')) {
                        elements.push('<a class="" href="#" data-row="' + meta.row + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a>')
                    }
                    return elements.join(" ");
                }
            }
        ]
    });
});

// 新增
function add(el){
    bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/cms/bank/credit/plan/addEdit");
}

// 编辑
function edit(el){
    const data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    bt.navtab.addNavtab(bt.lang('修改数据'), basePath + "/cms/bank/credit/plan/addEdit?id=" + data.id);
}

function del(el){
    const data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    doDelete([data.id])
}

function batchDelete(e, dt, node, config) {
    const data = dt.rows('.selected').data();
    if (data.length < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    const ids = [];
    const rowData = data.splice(0, data.length);
    for (let i = 0; i < rowData.length; i++) {
        ids.push(rowData[i].id);
    }
    doDelete(ids);
}

function doDelete(ids) {
    if (!ids || ids.length === 0) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function(){
        bt.loading.show();
        $.post(basePath + "/cms/bank/credit/plan/deleteByIds?ids=" + ids.join(","), function(result){
            bt.loading.hide();
            bt.navtab.refreshCurrentTable();
            parent.layer.msg(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

// 导入
function importCreditBank() {
    up = bt.btImport.init({
        code: "70000063",
        titleRows: 0,
        width: "100%",
        height: "100%",
        headRows: 1,
        success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
            $.ajax({
                type: "POST",
                url: basePath + "/cms/rat/creditCooperationBank/saveImport",
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

// 查看
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/rat/creditCooperationBank/view/" + data.id
    });
}
