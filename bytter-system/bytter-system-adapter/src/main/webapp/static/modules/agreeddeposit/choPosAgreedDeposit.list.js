var select,table;
bt.use(['form', 'datatable', 'dicts', 'btImport','selectType','corp'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.corp.init();
    select = bt.selectType.init();
    //生成列表
    table = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [[1, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/agreeddeposit/choinneragreeddeposit/datatablecho"
        },
        buttons: [
            {
                hide:bt.isnotResource('cho:inneragreeddeposit:agreeddeposit:pos:apply'),//新增权限
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            },{
                hide:bt.isnotResource('cho:inneragreeddeposit:agreeddeposit:pos:apply'),//新增权限
                text: bt.lang('common.button.batchSubmit'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    submitBatch(data);
                }
            }, {
                hide:bt.isnotResource('cho:inneragreeddeposit:agreeddeposit:pos:apply'),//新增权限
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
        }
        , columns: [
        {data: "billNo", bSortable: true, className: 'center'},
        {data: "corpName", bSortable: true, className: 'center'},
        {data: "blnctrAcc", bSortable: true, className: 'center'},
        {data: "accName", bSortable: true, className: 'center'},
        {data: "curName", bSortable: true, className: 'center'},
        {data: "contractNo", bSortable: true, className: 'center'},
        {data: "beginDate", bSortable: true, className: 'center'},
        {data: "endDate", bSortable: true, className: 'center'},
        {
            data: "certificateWay", bSortable: true,className: 'center',
            render: function (data, type, full, meta) {
                return bt.dict("agreed.certificateWay", full.certificateWay);
            }
        },
        {
            data: "status", bSortable: true,className: 'center',
            render: function (data, type, full, meta) {
                return bt.dict("nisBillhead.status", full.status);
            }
        },
        {
            data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
            var html = '';
            // if(bt.isResource('cho:inneragreeddeposit:agreeddeposit:pos:apply',full.corpId) && (full.status == 0 || full.status == -1)){//更新和删除权限
                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
            // }
            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
            return html;
            }
        }
    ]
    });

});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/agreeddeposit/choinneragreeddeposit/addChoPos");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/agreeddeposit/choinneragreeddeposit/addChoPos",
        end: function () {
            table && table.draw();
        }
    });
}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/agreeddeposit/choinneragreeddeposit/viewCho/"+ data.id
    });
}

function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/agreeddeposit/choinneragreeddeposit/updateChoPos?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/agreeddeposit/choinneragreeddeposit/updateChoPos?id=" + data.id,
            end: function () {
                table && table.draw();
            }
        });
    }
}

function del(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/agreeddeposit/choinneragreeddeposit/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//监听删除标志
$('body').on('click','.resetBtn',function () {
    select.get("eqs_corpId").attr("data-url","");
});

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
        $.post(basePath + "/agreeddeposit/choinneragreeddeposit/batchDelete", {ids: ids}, function (result) {
            table.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

//批量提交
function submitBatch(data) {
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
    parent.layer.confirm(bt.lang('common.message.confirm.batchSubmit'), function () {
        $.post(basePath + "/agreeddeposit/choinneragreeddeposit/batchSubmit", {ids: ids}, function (result) {
            table.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}