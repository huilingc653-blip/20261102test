var $, form, datatable;
bt.use(['form', 'datatable', 'bank', 'corp', 'mulselect', 'btImport'], function () {
    $ = bt.jquery;
    form = bt.form();
    bt.corp.init();
    bt.mulselect.init();
    bt.bank.init({
        elem: '.bank',
    });

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        order: [[1, 'desc']],
        ajax: {
            'url': basePath + "/bankacc/btbankaccplanfreeze/datatable"
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        buttons: [
            {
                hide: bt.isnotResource('netbank:bankAcc:btBankAccPlanFreeze:apply'),//应用权限
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    planFreezeImport();
                }
            }
        ],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "curName", bSortable: true},
            {
                data: "accTypeName", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("btBankAcc.accType", full.accType);
                }
            },
            {data: "bankName", bSortable: true},
            {data: "bankTypeName", bSortable: true},
            {data: "corpName", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('netbank:bankAcc:btBankAccPlanFreeze:apply', full.corpId)) {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="planFreeze(this)">' + bt.lang("common.button.freeze") + '</a>';
                    }
                    return html;
                }
            }
        ]
    });


});

function planFreeze(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/bankacc/btbankaccplanfreeze/add?bankAccId=" + data.id + "&curId=" + data.curId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankaccfreeze/add?bankAccId=" + data.id + "&curId=" + data.curId,
        end: function () {
            bt.datatable.refreshCurrentPage();
        }
    });
}

// 导入
function planFreezeImport() {
    up = bt.btImport.init({
        code: "10000089",
        titleRows: 0,
        width: "100%",
        height: "100%",
        headRows: 1,
        success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
            $.ajax({
                type: "POST",
                url: basePath + "/bankacc/btbankaccplanfreeze/saveImport",
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
