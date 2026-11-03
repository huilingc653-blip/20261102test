bt.use(['form', 'datatable', 'moneyType', 'btdate', 'select', 'dateType', 'dicts','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.mulselect.init();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/bankInsufficientFundsTimeConfig/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            },
            {
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchDelete(data);
                }
            },
            {
                text: bt.lang('common.button.batchDisable'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchDisable(data);
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
            {data: "bankTypeName", bSortable: false,className:"center"},
            {data: "time", bSortable: false,className:"center"},
            {data: "statusName", bSortable: false,className:"center"},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    html = html + '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a>';
                    html = html + '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a>';
                    return html;
                }
            }
        ]
    });
});

//手动新增
function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.add'), basePath + "/bill/accebill/bankInsufficientFundsTimeConfig/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['1000px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/bankInsufficientFundsTimeConfig/add",
        end: function () {
            appTable && appTable.draw();
        }
    });
}

//跳转到编辑页面
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/bill/accebill/bankInsufficientFundsTimeConfig/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bill/accebill/bankInsufficientFundsTimeConfig/update?id=" + data.id,
            end:function(){
                bt.datatable.refreshCurrentPage();
            }
        });
    }
}

//删除
function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/bill/accebill/bankInsufficientFundsTimeConfig/delete/" + data.id, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if(result.success){
                bt.navtab.refreshParent();
                bt.navtab.refreshCurrent()
            }
        }, 'JSON');
    });
}

/**
 * 批量删除
 */
function batchDelete(data) {
    if (data.length < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    var ids = "";
    var rowData = data.splice(0, data.length);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'), function () {
        $.post(basePath + "/bill/accebill/bankInsufficientFundsTimeConfig/batchDelete/", {ids: ids}, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

/**
 * 批量禁用
 */
function batchDisable(data) {
    if (data.length < 1) {
        layer.msg(bt.lang('common.message.confirm.selectData'));
        return;
    }
    var ids = "";
    var rowData = data.splice(0, data.length);
    for (var i = 0; i < rowData.length; i++) {
        if (ids == "") {
            ids += rowData[i].id;
        } else {
            ids += "," + rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchDisable'), function () {
        $.post(basePath + "/bill/accebill/bankInsufficientFundsTimeConfig/batchDisable/", {ids: ids}, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

