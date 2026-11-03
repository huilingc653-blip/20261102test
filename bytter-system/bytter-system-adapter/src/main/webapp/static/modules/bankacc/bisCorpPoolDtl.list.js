bt.use(['form', 'datatable', 'dateType', 'corp', 'dicts', 'money', 'selectType', 'accountTable', 'bank', 'moneyType','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    bt.moneyType.init();
    bt.dicts.init();
    bt.mulselect.init();
    bt.bank.init({
        elem: '.bank',
    });
    var selectType = bt.selectType.init();

    accountCallBack = function (data) {
        $("#likes_bankAcc").val(data.bankAcc);
        selectType.get("eqs_curId").val(data.curId);
    }
    var accountTable = bt.accountTable.init();
    // 选择单位回调
    corpCallback = function (id) {
        // 重新根据单位ID加载账户表格
        accountTable.get("likes_bankAcc").setCorp(id);
    }
    bt.corp.init();
    /**
     * 生成列表
     */
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        order: [[2, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bankacc/biscorppooldtl/datatable"
        },
        buttons: [
            {
                hide: bt.isnotResource('bankacc:biscorppooldtl:apply'),
                text: bt.lang('批量认领'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    batchClaim(data);
                }
            },{
                hide: bt.isnotResource('bankacc:biscorppooldtl:apply'),
                text: bt.lang('明细生成余额'),
                action: function (e, dt, node, config) {
                    var data = dt.rows('.selected').data();
                    genBal(data);
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
            {data: "transTime", bSortable: true, className: 'center'},
            {data: "corpName", bSortable: true},
            {data: "dtlId", bSortable: true, className: 'center'},
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "curName", bSortable: true, className: 'center'},
            {
                data: "amtIn", bSortable: false, className: 'textRight', bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.money.format(full.amtIn);
                }
            },
            {
                data: "amtOut", bSortable: false, className: 'textRight', bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.money.format(full.amtOut);
                }
            },
            {data: "bankSerialNo", bSortable: true},
            {data: "oppAccNo", bSortable: true},
            {data: "oppAccName", bSortable: true,render: function (data, type, full, meta) {
                    return '<div style="min-width: 240px;"><pre>'+(data||'')+'</pre></div>'
                }},
            {data: "oppAccBank", bSortable: true},
            {data: "remark", bSortable: true},
            {data: "postscript", bSortable: true},
            {data: "uses", bSortable: true},
            {data: "poolTypeName", bSortable: false, className: 'center'}
        ],
        summary:{
            pageSum: true,//小计功能关闭
            selectSum: true,//已选汇总功能开启
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '8',
                    dataName: 'amtIn'
                },
                {
                    colNum: '9',
                    dataName: 'amtOut'
                }
            ]
        }
    });
});

//批量认领
function batchClaim(data) {
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/biscorppooldtl/batchClaim"
    });
}

//明细生成余额
function genBal(data) {
    layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/biscorppooldtl/genBal"
    });
}
