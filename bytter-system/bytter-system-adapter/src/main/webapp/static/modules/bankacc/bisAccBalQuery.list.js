bt.use(['form', 'datatable', 'corp', 'selectType', 'dateType', 'upload', 'money', 'dicts', 'accountTable', 'mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.dateType.init();
    var selectType = bt.selectType.init();
    bt.mulselect.init();

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
    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        order: [[1, 'desc'], [3, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search",
            initQueryParams:function (params) {
                var insCorpId = params.ins_corpId;
                if(insCorpId == '' || insCorpId == null){
                    var corpId = $("#corpId").val();
                    if(corpId){
                        params.ins_corpId= corpId;
                        $("#corpId").val("");

                        accountTable.get("likes_bankAcc").setCorp(corpId);
                    }
                }
                return params
            }
        },
        ajax: {
            'url': basePath + "/bankacc/bisaccbal/getDataTablesQuery"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        , columns: [
            {data:"balDate",className:'center',bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.date.format(full.balDate, "yyyy-mm-dd");
                }
            },
            {data: "corpName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "isOnlineStr", bSortable: true},
            {data: "attributeName", bSortable: true},
            {data: "natureName", bSortable: true},
            {data:"accTypeName",bSortable:true},
            {data: "curName", bSortable: true},
            {
                data: "beginBal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.beginBal);
                }
            },
            {
                data: "bal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.bal);
                }
            },
            {
                data: "availBal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.availBal);
                }
            },
            {
                data: "frzBal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.frzBal);
                }
            },
            {
                data: "sysFrzBal", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.sysFrzBal);
                }
            },
            {
                data: "statusLang", bSortable: true,
                render: function (data, type, full, meta) {
                    return full.statusLang;
                }
            },
            {data: "getDate", bSortable: true},
            {data: "returnMsg", bSortable: false},
            {
                data: null, bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    var html = '';
                    // if ((full.status >= 11 && full.status < 95) && $("#loginName").val() === full.createBy) {
                    //     html += '<a class="" href="#" data-row="' + meta.row + '" onclick="withdraw(this)">' + bt.lang("common.button.withdraw") + '</a><span class="text-explode">|</span>';
                    // }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                    return html;
                }
            }
        ],
        // summary: {
        //     pageSum: false,
        //     allSum: true,
        //     selectSum: true,
        //     sumTextCol: 2,
        //     format: {
        //         decimal: 2
        //     },
        //     sumList: [
        //         {
        //             colNum: '8',
        //             dataName: 'bal'
        //         }
        //     ]
        // },
        summary: {
            pageSum: true,
            selectSum: true,
            sumTextCol: 2,
            format: {
                decimal: 2
            },
            groupColumn:"curName",
            sumList: [
                {
                    colNum: '10',
                    dataName: 'bal',
                    money:true
                },
                {
                    colNum: '11',
                    dataName: 'availBal',
                    money:true
                },
            ]
        }
        // ,
        // customExportAll: function (params, processing, data) {
        //     var exportUrl = basePath + "/bankacc/bisaccbal/exportAll";
        //     openExportExcel(exportUrl, data);
        //     processing(false);
        // }
    });

    /**
     * 撤回
     * @param el
     */
    withdraw = function (el) {
        var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
        if (data) {
            $.post(basePath + "/payment/bpm/task/batchRevoke", {ids: data.id}, function (result) {
                bt.datatable.refreshCurrentPage();
                parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
            }, 'JSON');
        }
    }
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/bisaccbal/view/" + data.id
    });
}
