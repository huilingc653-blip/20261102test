bt.use(['form', 'datatable', 'selectType', 'corp', 'btImport', 'bank', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.selectType.init();
    bt.bank.init({
        elem: '.bank',
    });


    //生成列表
    var table = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/bankacc/btbankacchandle/dataTableApprove"
        },
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data: "bankAcc", bSortable: true},
            {data: "accName", bSortable: true},
            {data: "corpName", bSortable: true},
            {data: "curName", bSortable: false},
            {data: "electricBillName", bSortable: true,
                render: function(data, type, full, meta){
                    return bt.dict("sf_yn", full.electricBill)
                }
            },
            {data: "circulationModeName", bSortable: true,
                render: function(data, type, full, meta){
                    return bt.dict("bankAcc.circulationMode", full.circulationMode)
                }
            },
            {data: "isDomesticBankName",bSortable:true,
                render: function(data, type, full, meta){
                    return bt.dict("btBankAcc.isDomesticBank", full.isDomesticBank)
                }
            },
            {data: "accTypeName", bSortable: true,
                render: function(data, type, full, meta){
                    return bt.dict("btBankAcc.accType", full.accType)
                }
            },
            {data: "attributeName", bSortable: true},
            {data: "natureName", bSortable: true},
            {data: "bankTypeName", bSortable: true},
            {data: "bankName", bSortable: true},
            {
                data: "isFrom", bSortable: true, render: function (data, type, full, meta) {
                    if ("0" == full.isFrom) {
                        return bt.lang("table.btBankAcc.app");
                    } else {
                        return bt.lang("table.btBankAcc.autonomy");
                    }
                }
            },
            {data: "isOnlineStr", bSortable: true,
                render: function(data, type, full, meta){
                    return bt.dict("sf_yn", full.isOnline)
                }
            },
            {data: "validSignStr", bSortable: true,
                render: function(data, type, full, meta){
                    return bt.dict("bankAcc.validSign", full.validSign)
                }
            },
            {data: "statusStr", bSortable: true,
                render: function(data, type, full, meta){
                    return bt.dict("bankAcc.handleStatu.query", full.status)
                }
            },
            {data: "accUseName", bSortable: false},
            {
                data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html='';
                    if($("#flowStatus").val()=='todo'){
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="approve(this)">'+bt.lang("common.button.approve")+'</a>&nbsp;|&nbsp;';
                    }
                    if($("#flowStatus").val() == 'pass'){
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a><span class="text-explode">|</span>';
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.trackFlowByBusinessId(\'' + full.id + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    }else {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.workFlow.trackFlow(\'' + full.processInstanceId + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    }
                    return html;
                }
            }
        ]
    });

    bt.workFlow.beforeSubmit = function(submitData){
        var data = bt.datatable.getSelectData("#appTable");
        if(data.length < 1){
            layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 2});
            return;
        }
        var ids = [];
        var taskIds = [];
        // 遍历行数据
        $.each(data,function(i, tr) {
            taskIds.push(tr.taskId);
        });
        bt.workFlow.taskList = taskIds;
        return true;
    }

});

/**
 * 账户查看
 * @param el
 */
function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bankacc/btbankacchandle/view/" + data.id
    });
}

/*审核*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve'), basePath + "/bankacc/btbankacchandle/approve?id=" + data.id + "&taskId=" + data.taskId);
        return;
    }
}
