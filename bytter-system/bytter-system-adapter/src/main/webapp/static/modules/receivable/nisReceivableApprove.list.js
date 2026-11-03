bt.use(['form', 'dateType', 'datatable', 'moneyType', 'selectType', 'bank', 'corp','mulselect','dicts'], function () {
    $ = bt.jquery;
    bt.dateType.init();
    bt.selectType.init();
    bt.moneyType.init();
    bt.corp.init();
    bt.mulselect.init();
    bt.bank.init({
        elem: '.bank',
    });
    var dicts = bt.dicts.init();
    var selectTree;

    bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        checkbox: true,
        clickRowSelected:false,
        order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/receivable/nisreceivable/dataTableApprove"
        },
        buttons: [],
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "billNo", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "busTypeName", bSortable: true},
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "bankName", bSortable: true},
            {data: "curName", bSortable: true},
            {
                data: "amt", bSortable: true, className: 'textRight',
                render: function (data, type, full, meta) {
                    return bt.money.format(full.amt);
                }
            },
            {data: "oppAccNo", bSortable: true},
            {data: "oppAccName", bSortable: true},
            {data: "oppBankCode", bSortable: true},
            {data: "oppAccBank", bSortable: true},
            {data: "transTime", bSortable: true, className: 'center'},
            {
                data: "status", bSortable: true, className: 'center', visible: setApproveDateVisible(),
                render: function (data, type, full, meta) {
                    return bt.dict("nisReceivable.status", full.status);
                }
            },
            {data: "approveDate", bSortable: true, className: 'center', visible: setApproveDateVisible()},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                var html = '';
                if ($("#flowStatus").val() == 'todo') {
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="approve(this)">' + bt.lang("common.button.approve") + '</a>&nbsp;|&nbsp;';
                }
                if($("#flowStatus").val() == 'pass'){
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                }else {
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.workFlow.trackFlow(\'' + full.processInstanceId + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                }
                return html;
            }
            }
        ],
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
                    colNum: '8',
                    dataName: 'amt'
                }
            ]
        },
        btInitComplete: function (settings, json) {
            // 加载单位
            selectTree = bt.selectTree.init({
                url: basePath + "/receivable/nisreceivable/treeByCorpIds",
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
    });

    initCorp = function () {
        selectTree.setUrl(basePath + "/receivable/nisreceivable/treeByCorpIds");
    }

    /**
     * 审批后事件
     */
    bt.workFlow.afterSubmit = function () {
        initCorp();
    }
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/receivable/nisreceivable/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/receivable/nisreceivable/view/" + data.id
    });
}

function setApproveDateVisible() {
    var flowStatus = $("#flowStatus").val();
    if (flowStatus == "pass") {
        return true;
    } else {
        return false;
    }
}

/*审核*/
function approve(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.approve'), basePath + "/receivable/nisreceivable/approve?id=" + data.id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/receivable/nisreceivable/approve?id=" + data.id + "&taskId=" + data.taskId
    });
}


