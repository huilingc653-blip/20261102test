var form = {};
var dicts = {};
var appTable = {};
bt.use(['form', 'jquery', 'datatable', 'select', 'element', 'dicts'], function () {
    $ = bt.jquery;
    form = bt.form()

    //生成列表
    appTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,

        checkbox: true, order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebillClaim/config/accountConfigDatatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            }, {
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    deleteBatch(data);
                }
            }, {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
            {data: "corpName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "bankTypeName", bSortable: true},
            {data: "busType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("AB_CLAIM_ACCOUNTCONFIG", full.busType);
                }
            },
          //  {data: "busTypeName", bSortable: true},
            {
                data: "validSign", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("sf_yn", full.validSign);
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>\
                    <a class="" href="#" data-row="' + meta.row + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>\
                    <a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });

    //监听提交
    form.on('submit(configSubmit)', function (data) {
        data.field['accebill.accountByDiscount'] = data.field['accebill.accountByDiscount'] === '' ? 0 : 1
        data.field['accebill.accountByCollection'] = data.field['accebill.accountByCollection'] === '' ? 0 : 1
        data.field['accebill.accountByCashing'] = data.field['accebill.accountByCashing'] === '' ? 0 : 1
        data.field['accebill.accountByCashing2'] = data.field['accebill.accountByCashing2'] === '' ? 0 : 1
        $.post(basePath + "/sys/config/save", data.field, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, "JSON");
        return false;
    });
    dicts = bt.dicts.init();
});

//跳转到新增页面
function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/bill/accebillClaim/config/accountConfigAdd");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebillClaim/config/accountConfigAdd",
        end: function () {
            appTable && appTable.draw();
        }
    });
}

//跳转到编辑页面
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/bill/accebillClaim/config/accountConfigUpdate?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bill/accebillClaim/config/accountConfigUpdate?id=" + data.id,
            end: function () {
                appTable && appTable.draw();
            }
        });
    }
}

//单笔删除数据
function del(el) {
//    console.log(bt.lang('common.message.confirm.delete'));
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    console.log(bt.lang('common.message.confirm.delete'));
    console.log(data);
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/bill/accebillClaim/config/accountConfigDelete/" + data.id, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                appTable && appTable.draw();
            }
        }, 'JSON');
    });
}

//批量删除数据
function deleteBatch(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        var ids;
        for (var i = 0; i < len; i++) {
            if (ids) {
                ids = ids + "," + rowData[i].id;
            } else {
                ids = rowData[i].id;
            }
        }
        $.post(basePath + "/bill/accebillClaim/config/accountConfigBatchDelete/" + ids, function (result) {
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                appTable && appTable.draw();
            }
        }, 'JSON');
    });
}

//查看
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebillClaim/config/accountConfigView/" + data.id
    });
}

