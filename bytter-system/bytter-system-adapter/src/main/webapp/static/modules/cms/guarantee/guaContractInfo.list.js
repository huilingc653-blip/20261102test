var datatable, mulselects;
bt.use(['form', 'datatable', 'dateType', 'select', 'corp', 'money', 'moneyType', 'bank', 'btImport', 'upload', 'dicts', 'mulselect','btImport'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    var dicts = bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();

    // 初始化下拉选择
    mulselects = bt.mulselect.init();

    // 生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        order: [[11, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/cms/guarantee/guaContractInfo/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('gua:contract:info:apply'),//新增权限
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            },
            {
                hide: bt.isnotResource('gua:contract:info:apply'),//应用权限
                text: bt.lang('common.button.imports'),
                action: function ( e, dt, node, config ) {
                    importExcel();
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
            {data: "corpName", className: 'center', bSortable: true},   // 融资主体
            {data: "guaNo", className: 'center', bSortable: true},      // 担保编号
            {data: "guaTypeName", className: 'center', bSortable: true},    // 担保方式
            {data: "guaPerson", className: 'center', bSortable: true},      // 担保单位
            {data: "debtorPerson", className: 'center', bSortable: true},   // 债务人
            {data: "creditorPerson", className: 'center', bSortable: true}, // 债权人
            {data: "curName", className: 'center', bSortable: true},    // 币别
            {
                // 担保额度
                data: "guaMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.guaMoney);
                }
            },
            {data: "contractCode", className: 'center', bSortable: true},   // 合同编号
            {data: "startDate", className: 'center', bSortable: true},      // 担保开始日期
            {data: "endDate", className: 'center', bSortable: true},        // 担保结束日期
            {data: "operator", className: 'center', bSortable: true},       // 操作人
            {
                data: "status", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("guaContract.status", full.status);
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('gua:contract:info:apply', full.corpId) && full.status != 11) {//更新和删除权限
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode"></span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode"></span>';
                    }
                    if (bt.isResource('gua:contract:info:query', full.corpId)) {//查看和日志权限权限
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode"></span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="listMsg(this)">' + bt.lang("common.button.logInfo") + '</a>';
                    }
                    return html;
                }
            }
        ],
        summary: {
            pageSum: true,//小计功能开启
            selectSum: true,//已选汇总功能开启
            //groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '8', //汇总列序号
                    dataName: 'guaMoney' //汇总列担保额度
                }
            ]
        }
    });
});

function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/cms/guarantee/guaContractInfo/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/guarantee/guaContractInfo/add",
        end: function () {
            datatable && datatable.draw();
        }
    });
}

//查看担保合同信息
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '500px'],
        maxmin: true,
        content: basePath + "/cms/guarantee/guaContractInfo/viewInfo/" + data.id
    });
}

//编辑
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/cms/guarantee/guaContractInfo/editContractInfo/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/guarantee/pledgeInfo/editContractInfo/" + data.id,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

//日志列表
function listMsg(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'), basePath + "/cms/guarantee/gualog/listlog?guaId=" + data.id);
            return;
        }
    }
}

//删除行
function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/cms/guarantee/guaContractInfo/delete/" + data.id, function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

/**
 * 导入功能
 */
importExcel = function () {
    up = bt.btImport.init({
        code: "70000060",
        titleRows: 0,
        width: "100%",
        height: "100%",
        headRows: 1,
        success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
            $.ajax({
                type: "POST",
                url: basePath + "/cms/guarantee/guaContractInfo/saveImport",
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
