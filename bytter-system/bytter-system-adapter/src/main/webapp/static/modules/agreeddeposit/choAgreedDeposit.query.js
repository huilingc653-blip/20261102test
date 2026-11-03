var select;
bt.use(['form', 'datatable', 'dicts', 'btImport', 'selectType', 'corp'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.corp.init();
    select = bt.selectType.init();
    //生成列表
    var table = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: false,
        order: [[0, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/agreeddeposit/choinneragreeddeposit/datatablequery"
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
        }
        , columns: [
            {data: "corpName", bSortable: true, className: 'center'},
            {data: "blnctrAcc", bSortable: true, className: 'center'},
            {data: "accName", bSortable: true, className: 'center'},
            {data: "curName", bSortable: true, className: 'center'},
            {data: "contractNo", bSortable: true, className: 'center'},
            {data: "beginDate", bSortable: true, className: 'center'},
            {data: "oldEndDate", bSortable: true, className: 'center'},
            {
                data: "certificateWay", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("agreed.certificateWay", full.certificateWay);
                }
            },
            {
                data: "endDate", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = "-";
                    if (full.endDate != null) {
                        html = full.endDate;
                    }
                    return html;
                }
            },
            {
                data: "statusName", bSortable: false, className: 'center',
                render: function (data, type, full, meta) {
                    return bt.dict("agreed.choStatus", full.statusName);
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a>';

                html += '<span class="text-explode">|</span><a class="" href="#" data-row="' + meta.row + '" onclick="logInfo(this)">' + bt.lang("common.button.logInfo") + '</a>';
                return html;
            }
            }
        ]
    });

});


function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/agreeddeposit/choinneragreeddeposit/viewCho/" + data.id
    });
}

function logInfo(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['70%', '50%'],
        maxmin: true,
        content: basePath + "/agreeddeposit/choinneragreeddeposit/queryDtl/" + data.id
    });
}


//监听删除标志
// $('body').on('click', '.resetBtn', function () {
//     select.get("eqs_corpId").attr("data-url", "");
// });
