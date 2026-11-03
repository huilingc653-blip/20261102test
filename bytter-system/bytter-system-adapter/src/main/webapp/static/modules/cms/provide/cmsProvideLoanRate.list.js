bt.use(['form', 'datatable', 'corp', 'moneyType', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();//单位
    var dicts = bt.dicts.init();

    /**判断字符串是否为空*/
    bt.empty = function (ms) {
        if (typeof (ms) == "undefined" || ms == null || ms == "" || ms == " ") {
            return true;
        }
        return false;
    }
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        checkbox: true,
        ajax: {
            'url': basePath + "/cms/provide/loaninfo/datatableLoanRate"
        },
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
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
            {data: "corpName"},
            {data: "billCode"},
            {data: "createDate"},
            {data: "bankName"},
            {data: "curName"},
            {
                data: "plMoney", className: "textRight", render: function (data, type, full, meta) {
                    return bt.money.format(full.plMoney);
                }
            },
            {data: "plDate"},
            {data: "repayDate"},
            {
                data: "repayTypeName", render: function (data, type, full, meta) {
                    return bt.empty(full.repayTypeName) ? full.repayTypeName1 : full.repayTypeName;
                }
            },
            {data: "contractTermName"},
            {
                data: "plTerm", render: function (data, type, full, meta) {
                    return full.plTerm + bt.dict('PrecativeLoanInfo.ternUnit', full.ternUnit);
                }
            },
            {
                data: "status", render: function (data, type, full, meta) {
                    return bt.dict('cms.bizStatus', full.bizStatus);
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    return '<a class="" href="#" data-row="' + meta.row + '" onclick="edit(this)">' + bt.lang("common.button.edit") + '</a>';
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
                    colNum: '6', //汇总列序号
                    dataName: 'plMoney' //汇总列借款金额
                }
            ]
        }
    });
});

function edit(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var path = basePath + "/cms/provide/cmsprovideloanrate/list?" + "id=" + data.id + "&billCode=" + data.billCode;
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.button.edit'), path);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: path,
            end: function () {
                billTable.draw();
            }
        });
    }
}

