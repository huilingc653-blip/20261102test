bt.use(['form', 'datatable', 'dateType', 'moneyType', 'dicts', 'accountTable', 'selectType','bank'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    bt.dicts.init();
    bt.bank.init({
        elem:".bank"
    })
    var selectType = bt.selectType.init();
    bt.moneyType.init();
    /**
     * 选择账户回调
     * @param data
     */
    accountCallBack = function (data) {
        $("#likes_bankAcc").val(data.bankAcc);
        $("#likes_accName").val(data.accName);
        $("#likes_bankName").val(data.bankName);
        selectType.get("eqs_curId").val(data.curId);
    };
    bt.accountTable.init();
    var moneyFun = bt.moneyType.getFun();
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        checkbox: true,
        order: [[4, 'desc']],
        ajax: {
            'url': basePath + "/bankacc/bisaccdtl/dataTableMakeup"
        },
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "transTime", bSortable: true},
            {
                data: "amt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.amt);
                }
            },
            {
                data: "bal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return moneyFun.format(full.bal);
                }
            },
            {
                data: "cdSign", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("btBankAccDtl.cdSign", full.cdSign);
                }
            },
            {data: "curName", bSortable: true},
            {data: "bankSerialNo", bSortable: true},
            {data: "oppAccNo", bSortable: true},
            {data: "oppAccName", bSortable: true},
            {data: "oppAccBank", bSortable: true},
            {data: "projectRemark", bSortable: true},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    if (bt.isResource('netbank:bankAcc:bisAccDtl:makeupApply',full.corpId)) {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="makeup(this)">' + bt.lang("common.button.makeup") + '</a>';
                    }
                    return html;
                }
            }
        ],
        summary: {
            pageSum: false,
            allSum: true,
            selectSum: true,
            sumTextCol: 2,
            format: {
                deciaml: 2
            },
            sumList: [
                {
                    colNum: '5',
                    dataName: 'amt'
                },
                {
                    colNum: '6',
                    dataName: 'bal'
                }
            ]
        },
        // 暂时注释导出全部回调
        customExportAll: function (params, processing, data) {
            var exportUrl = basePath + "/bankacc/bisaccdtl/makeupExportAll";
            openExportExcel(exportUrl, data);
            processing(false);
        }
    });

});

function ledTransTimeCallback(){
    var startDate = $("#ged_transTime").val()
    var endDate = $("#led_transTime").val()
    if(!endDate){
        bt.dateType.get('ged_transTime').attr('data-max','2099-12-31')
        $("#ged_transTime").val(startDate)
    }
}

/**
 * 补录
 * @param el
 */
function makeup(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('table.bisaccdtl.btn.makeup'), basePath + "/bankacc/bisaccdtl/makeup?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/bankacc/bisaccdtl/makeup?id=" + data.id,
            end: function () {
                bt.datatable.refreshCurrentPage();
            }
        });
    }
}