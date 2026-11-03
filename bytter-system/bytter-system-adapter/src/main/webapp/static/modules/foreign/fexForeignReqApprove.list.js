var datatable;
bt.use(['form', 'datatable', 'select', 'dateType', 'corp', 'money', 'moneyType', 'dicts', 'bank', 'selectType','element','mulselect'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.mulselect.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.selectType.init();
    bt.dicts.init();
    bt.bank.init({
        elem: '.bank',
    });

    var selectTree;

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        clickRowSelected:false,
        order: [[1, 'asc'],[8, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/foreign/fexforeignreq/dataTableApprove"
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
            {data: "billNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    if(full.status == -1){
                        var row = datatable.row(meta.row).node();
                        $(row).addClass("row_bg");
                    }
                    return data;
                }
            },
            {data: "corpName", bSortable: true},
            {data: "payerAcc", bSortable: true},
            {data: "payerAccName", bSortable: true},
            {data: "payerBank", bSortable: true},
            {data: "curName", bSortable: true},
            {
                data: "payMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.payMoney);
                }
            },
            {data: "payDate", bSortable: true, className: 'center'},
            {data: "payeeAcc", bSortable: true},
            {data: "payeeAccName", bSortable: true},
            {data: "payeeSwiftCode", bSortable: true},
            {data: "payeeBank", bSortable: true},
            {data: "payeeCountryName", bSortable: true},
            {data: "addWord", bSortable: true},
            {
                data: "remitType", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {

                    return bt.dict("fexForeignReq.remitType", full.remitType);
                }
            },
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if ($("#flowStatus").val() == 'todo') {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="approve(this)">' + bt.lang("common.button.approve") + '</a>&nbsp;|&nbsp;';
                    }
                    if($("#flowStatus").val() == 'pass'){
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    }else {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.workFlow.trackFlow(\'' + full.processInstanceId + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    }
                    return html;
                }
            }
        ],
        btInitComplete: function (settings, json) {
            // 加载单位
            selectTree = bt.selectTree.init({
                url: basePath + "/foreign/fexforeignreq/treeByCorpIds",
                elem: ".demo-selectTree-normal",
                name: 'ins_corpId',
                id: 'id',
                pId: 'pId',
                checkType: "check",
                required: false,
                clickCallBack: function (id) {
                }
            });
        },
        summary: {
            pageSum: true,
            allSum: false,
            selectSum: true,
            groupColumn:'curName',
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '7',
                    dataName: 'payMoney'
                }
            ]
        }
    });

    initCorp = function () {
        selectTree.setUrl(basePath + "/netbank/nisbillhead/treeByCorpIds");
    }

    /**
     * 审批后事件
     */
    bt.workFlow.afterSubmit = function () {
        initCorp();
    }
});

/*审核*/
function approve(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.approve'), basePath + "/foreign/fexforeignreq/approve?id=" + data.id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/foreign/fexforeignreq/approve?id=" + data.id + "&taskId=" + data.taskId
    });
}
