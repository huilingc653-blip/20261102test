var billTable = {};
bt.use(['form', 'datatable', 'selectTable', 'dicts', 'corp', 'moneyType', 'mulselect', 'btdate', 'jquery', 'dateType', 'element'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.dicts.init();
    bt.mulselect.init();
    bt.btdate.init({
        elem: "#startDate",
        startname: "ged_startDate",
        endname: "led_startDate"
    });
    bt.btdate.init({
        elem: "#endDate",
        startname: "ged_endDate",
        endname: "led_endDate"

    });
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[1, 'desc']],
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bill/accebill/nonassignableCancel/dataTableApprove"
        },
        columns: [
            {
                data: "applyCorpName", bSortable: true,
                render: function (data, type, full, meta) {
                    return '(' + full.applyCorpCode + ')' + full.applyCorpName;
                }
            },
            {data: "corpName", bSortable: true},//1
            {data: "busCorpName", bSortable: true},
            {data: "busPersonnel", bSortable: true},//2
            {data: "billFlagName", bSortable: true},
            {data: "billTypeName", bSortable: true},//3
            {data: "origNo", bSortable: true},
            {data: "sbbllRngStrtSn", bSortable: true},//4
            {data: "sbbllRngEndSn", bSortable: true},
            {
                data: "blStdAmt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    if (data) {
                        return bt.money.format(data);
                    }
                    return '';
                }
            },//5
            {
                data: "billMoney", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    if (data) {
                        return bt.money.format(data);
                    }
                    return '';
                }
            },
            {data: "payer", bSortable: true},//6
            {data: "acceptor", bSortable: true},
            {data: "startDate", bSortable: true},//7
            {data: "endDate", bSortable: true},
            {data: "cnAccrecords", bSortable: true},//背书人（前手）8
            {data: "sendSignName", bSortable: true},
            {data: "blgrdbagcrclPrmtIndName", bSortable: true},//9
            {data: "blHoldUnnIdNo", bSortable: true},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if ($("#flowStatus").val() == 'todo') {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="approve(this)">' + bt.lang("common.button.approve") + '</a>&nbsp;|&nbsp;';
                    }
                    if ($("#flowStatus").val() == 'pass') {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    } else {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.workFlow.trackFlow(\'' + full.processInstanceId + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    }
                    return html;
                }
            }
        ],
        fixedColumns: {
            rightColumns: 1
        },
        summary: {
            pageSum: true,
            selectSum: true,
            sumList: [
                {
                    colNum: '10',
                    dataName: 'blStdAmt'

                },
                {
                    colNum: '11',
                    dataName: 'billMoney'
                }
            ]
        }
    });
});


/*审核*/
function approve(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.approve'), basePath + "/bill/accebill/nonassignableCancel/approve?businessId=" + data.id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/nonassignableCancel/approve?businessId=" + data.id + "&taskId=" + data.taskId
    });
}

//申请单位回调函数
function applyCorpCallback(data) {
    if (data && data.length == 0) return;
    //设置票据单位
    //bt.corp.init().get("corpId").setUrl2("accCorp", data);

}

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.view'),
            basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vid + "&viceId=" + data.vvid + "&accId=" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abreceivebill/view?vouId=" + data.vid + "&viceId=" + data.vvid + "&accId=" + data.id
    });
}