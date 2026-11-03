bt.use(['form', 'datatable', 'selectType', 'bank', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.bank.init({
        elem: '.bank',
    });
    bt.selectType.init();
    bt.dicts.init();
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
            'url': basePath + "/bankacc/btbankaccrevhandle/datatable"
        },
        buttons: [
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
            {data: "accNo", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "curName", bSortable: false},
            {data: "isDomesticBankName",bSortable:true},
            {data: "accTypeName", bSortable: true},
            {data: "attributeName", bSortable: true},
            {data: "natureName", bSortable: true},
            {data: "bankTypeName", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "provName", bSortable: true},
            {data: "cityName", bSortable: true},
            {data: "electricBillName", bSortable: true},
            {data: "opRession", bSortable: true},
            {data: "accUseName", bSortable: false},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('netbank:bankAcc:btBankAccRevHandle:apply',full.corpId)) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="update(this)">' + bt.lang("common.button.changeaccount") + '</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="deleteColumn(this)">' + bt.lang("common.button.repulse") + '</a><span class="text-explode">|</span>';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
                    return html;
                }
            }
        ]
    });
});

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
        content: basePath + "/bankacc/btbankaccrev/view/" + data.id
    });
}

/**
 * 变更办理
 * @param el
 */
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/bankacc/btbankaccrevhandle/update?id=" + data.id + "&&bankAcc=" + data.bankAcc);
            return;
        }
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankaccrevhandle/update?id=" + data.id + "&&bankAcc=" + data.bankAcc,
        end: function () {
            location.reload();
        }
    });
}

/**
 * 打回
 * @param el
 */
function deleteColumn(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.repulse'), function () {
        $.post(basePath + "/bankacc/btbankaccrevhandle/delete/" + data.id, function (result) {
            location.reload();
            parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
        }, 'JSON');
    });
}
