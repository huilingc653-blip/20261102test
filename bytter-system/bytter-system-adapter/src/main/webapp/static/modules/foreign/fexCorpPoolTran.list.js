var datatable,select;
bt.use(['form', 'datatable', 'selectType','dicts','selectTable'], function () {
    $ = bt.jquery, form = bt.form();

    bt.dicts.init();
    /**
     * 资金池回调
     * @param data
     */
    poolCallBack = function (data) {
        //重新加载主账号
        bankAccInit.get("bankAcc").rebuild({
            urldata: {poolId: data.poolid},
            columns: bankAccCol
        });
    }


    var bankAccCol = [
        {data: "bankAcc",},
        {data: "bankName",},
        {data: "curEnglishCode",},
        {data: "isOnlineStr",}
    ];
    //主账号
    var bankAccInit = bt.selectTable.init({
        elem: "#mainAccountTable",
        url: basePath + "/foreign/fexcorppool/getBankAcc",
        urldata: {poolId: ""},
        scrollX: true,
        scrollY: "200px",
        searchInput: "#bankAcc",
        paging: true,
        columns: bankAccCol,
        clickCallback: function (data) {
            if (data) {
                $('#bankAcc').val(data.bankAcc);
                $('#curId').val(data.curId);
            } else {
                $('input.text').val('');
            }
        }
    });

    select = bt.selectType.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: false,
        clickRowSelected:false,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/foreign/fexcorppooltran/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
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

            {data: "poolName", bSortable: true, className: 'center'},
            {data: "bankAcc", bSortable: true, className: 'center'},
            {data: "englishCode", bSortable: true, className: 'center'},
            {
                data: "tranSign", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fexCorpPoolTran.tranSign", full.tranSign);
                }
            },
            {
                data: "tranType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("fexCorpPoolTran.tranType", full.tranType);
                }
            },
            {data: "proName", bSortable: true},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.edit") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ]
    });

});

/**
 * 新增
 * @param el
 */
function add() {
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.add.data'), basePath + "/foreign/fexcorppooltran/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcorppooltran/add",
        end: function () {
            datatable && datatable.draw();
        }
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
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/foreign/fexcorppooltran/update?poolId=" + data.poolId +"&&bankaccId="+data.bankaccId+"&curId=" + data.curId);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/foreign/fexcorppooltran/update?poolId=" + data.poolId +"&&bankaccId="+data.bankaccId+"&curId=" + data.curId,
            end: function () {
                datatable && datatable.draw();
            }
        });
    }
}

/**
 * 删除
 * @param el
 */
function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'), function () {
        $.post(basePath + "/foreign/fexcorppooltran/delete/" + data.id , function (result) {
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}

/**
 * 查看
 * @param el
 */
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/foreign/fexcorppooltran/view/" + data.poolId +"/" + data.bankaccId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexcorppooltran/view/" + data.poolId +"/" + data.bankaccId
    });
}

/**
 * 重置
 */
function resetCode() {
    select.get("poolId").val('');
    $("#bankAcc").val('');
    $("#curId").val('');
}
