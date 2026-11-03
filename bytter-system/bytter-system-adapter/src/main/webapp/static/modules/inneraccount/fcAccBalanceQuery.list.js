bt.use(['form', 'datatable', 'corp', 'money', 'bank', 'selectType', 'rateType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();

    /**
     * 科目加载自定义组装
     * @param text
     * @param data
     * @returns {*}
     */
    beforeInit = function (text, data) {
        if (data.subjectCode) {
            return "(" + data.subjectCode + ")" + data.subjectName;
        }
        return text;
    }
    bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });

    //生成列表
    bt.datatable.init({
        elem: "#appTable",
        order: [[1, 'desc'], [0, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/inneraccount/fcaccbalancequery/datatable"
        },
        buttonsLayout: true,
        columns: [
            {
                data: "account", bSortable: true,
                render: function (data, type, full, meta) {
                    var html = full.account;
                    if (full.showLink) {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewDtl(this)">' + full.account + '</a>';
                    }
                    return html;
                }
            },
            {data: "accName", bSortable: true},
            {data: "subjectName", bSortable: true},
            {data: "curName", bSortable: true},
            {
                data: "checkBalance", bSortable: true, className: "textRight",
                render: function (data, type, row, meta) {
                    return bt.money.format(data);
                }
            },
            {
                data: "availableBalance", bSortable: true, className: "textRight",
                render: function (data, type, row, meta) {
                    return bt.money.format(data);
                }
            },
            {
                data: "overdraftMoney", bSortable: true, className: "textRight",
                render: function (data, type, row, meta) {
                    return bt.money.format(data);
                }
            },
            {data: "accOpenTime", bSortable: true},
            {data: "deadlineDate", bSortable: true},
            {
                data: "interestRate", bSortable: true, className: "textRight",
                render: function (data, type, full, meta) {
                    return bt.rateType.getFun().format(full.interestRate);
                }
            }
        ],
        // 暂时注释导出全部回调
        customExportAll: function (params, processing, data) {
            var exportUrl = basePath + "/inneraccount/fcaccbalancequery/exportAll";
            openExportExcel(exportUrl, data);
            processing(false);
        }
    });
});

function viewDtl(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/inneraccount/fcaccbalancequery/viewdtl/" + data.account
    });
}