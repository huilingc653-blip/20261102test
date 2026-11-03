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
            'url': basePath + "/inneraccount/fcaccbalancequery/details/" + $('#account').val()
        },
        columns: [
            {data: "account", bSortable: true},
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
        summary: {
            pageSum: false,
            allSum: true,
            selectSum: true,
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '5',
                    dataName: 'checkBalance'
                },
                {
                    colNum: '6',
                    dataName: 'availableBalance'
                },
                {
                    colNum: '7',
                    dataName: 'overdraftMoney'
                }
            ]
        }
    });
});