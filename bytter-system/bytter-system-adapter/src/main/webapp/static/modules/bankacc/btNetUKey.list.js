bt.use(['form', 'datatable', 'selectType','dateType', 'corp', 'btImport', 'bank', 'dicts','selectTable'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.selectType.init();
    bt.dateType.init();
    bt.bank.init({
        elem: '.bank',
    });

    importExcel = function () {
        up = bt.btImport.init({
            code: "10000083",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/bankacc/btnetukey/saveImport",
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
    bt.selectTable.init({
        elem: "#custodianId",
        url: basePath + "/sys/user/listAllUserByUnDelete",
        searchInput: ".custodianId",
        scrollX: "900px",
        scrollY: "300px",
        columns: [
            {data: "loginName",},
            {data: "username",},
        ],
        clickCallback: function (data, $tr) {
            $(".custodianId").val(data.username);
            $("input[name='eqs_custodianId']").val(data.id);
        },
    })

    bt.selectTable.init({
        elem: "#uKeyUserId",
        url: basePath + "/sys/user/listAllUserByUnDelete",
        searchInput: ".uKeyUserId",
        scrollX: "900px",
        scrollY: "300px",
        columns: [
            {data: "loginName",},
            {data: "username",},
        ],
        clickCallback: function (data, $tr) {
            $(".uKeyUserId").val(data.username);
            $("input[name='eqs_ukeyUserId']").val(data.id);
        },
    })

    bt.selectTable.init({
        elem: "#supervisorId",
        url: basePath + "/sys/user/listAllUserByUnDelete",
        searchInput: ".supervisorId",
        scrollX: "900px",
        scrollY: "300px",
        columns: [
            {data: "loginName",},
            {data: "username",},
        ],
        clickCallback: function (data, $tr) {
            $(".supervisorId").val(data.username);
            $("input[name='eqs_supervisorId']").val(data.id);
        },
    })
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bankacc/btnetukey/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:bankAcc:btnetukey:update'),
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            },
            {
                hide: bt.isnotResource('netbank:bankAcc:btnetukey:update'),
                text: bt.lang('common.button.batchDelete'),
                action: function (e, dt, node, config) {
                    batchDelete(e, dt, node, config);
                }
            },{
                hide: bt.isnotResource('netbank:bankAcc:btnetukey:update'),
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
            {data: "ukeyNo", bSortable: true,className: 'center',},
            {data: "corpName", bSortable: true,className: 'center',},
            {data: "bankLimit", bSortable: true,className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("bank.permissions", data);
                }
            },
            {data: "personalLimit", bSortable: false,className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("personal.permissions", data);
                }},
            {data: "ukeyUserName", bSortable: true,className: 'center',},
            {data: "ebankLoginName", bSortable: true,className: 'center',},
            {data: "custodianName",bSortable:true,className: 'center',},
            {data: "supervisorName", bSortable: true,className: 'center',},
            {data: "openDate", bSortable: true,className: 'center',},
            {data: "expireDate", bSortable: true,className: 'center',},
            {data: "bankAcc", bSortable: true,className: 'center',},
            {data: "accName", bSortable: true,className: 'center',},
            {data: "bankName", bSortable: true,className: 'center',},
            {data: "isHeadCustodian", bSortable: true,className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("sf_yn", data);
                }},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    if(full.custodianName === $("input[name='username']").val()){
                        if (bt.isResource('netbank:bankAcc:btnetukey:update',full.corpId)) {
                            html += '</a><span class="text-explode">|</span>'+'<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit");
                            html += '</a><span class="text-explode">|</span>' + '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") ;
                        }
                    }
                    return html;
                }
            }
        ]
    });

    flush = function()
    {
        $("input[name='eqs_ukeyUserId']").val("");
        $("input[name='eqs_custodianId']").val("");
        $("input[name='eqs_supervisorId']").val("");
    }
});

/**
 * 新增
 */
function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/bankacc/btnetukey/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btnetukey/add",
        end: function () {
            datatable && datatable.draw();
        }
    });
}

/**
 * 查看
 * @param el
 */
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btnetukey/view/" + data.id
    });
}

/**
 * 编辑
 * @param el
 */
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/bankacc/btnetukey/update/" + data.id);
            return;
        }
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btnetukey/update/" + data.id,
        end: function () {
            location.reload();
        }
    });
}

/**
 * 删除
 * @param el
 */
function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/bankacc/btnetukey/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

/**
 * 批量删除
 */
function batchDelete(e, dt, node, config) {
    var data = dt.rows('.selected').data();
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
        $.post(basePath + "/bankacc/btnetukey/batchDelete/", {ids: ids}, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}