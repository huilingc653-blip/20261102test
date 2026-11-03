bt.use(['form', 'datatable', 'dateType', 'moneyType', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dateType.init();
    bt.dicts.init();

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
        clickRowSelected:false,
        scrollY:'auto',
        order:[[1, 'desc']],
        ajax: {
            'url': basePath + "/report/allocationsettlement/datatable"
        },
        buttons: [
             {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns: [
            {data: "transTime", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "attributeName", bSortable: true},
            {
                data: "cdSign", bSortable: true,
                render: function (data, type, full, meta) {
                    return bt.dict("btBankAccDtl.cdSign", full.cdSign);
                }
            },
            {data: "curName", bSortable: true},
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

            {data: "oppAccNo", bSortable: true},
            {data: "oppAccName", bSortable: true},
            {data: "oppAccBank", bSortable: true},
            {data: "remark", bSortable: true},
            {data: "projectRemark", bSortable: true}
            /*{
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                var html = '';
                html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';
                return html;
            }
            }*/
        ],
        summary: {
            pageSum: false,
            allSum: true,
            selectSum: true,
            sumTextCol:2,
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '9',
                    dataName: 'amt'
                },{
                    colNum: '10',
                    dataName: 'bal'
                }
            ]
        }
    });

});


function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankaccdtl/view/" + data.id
    });
}
