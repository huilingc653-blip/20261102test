bt.use(['form', 'datatable', 'selectType', 'corp', 'btImport', 'bank', 'dicts','dateType'], function () {
    $ = bt.jquery, form = bt.form();
    bt.corp.init();
    bt.dateType.init();
    bt.dicts.init();
    bt.selectType.init();

    //生成列表
    var table = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true,
        order: [[6, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/credit/evaluationAnalysis/dataTableApprove"
        },
        fixedColumns: {
            rightColumns: 1//代表冻结右边一列
        },
        columns: [
            {data:"taskNo",bSortable:false,className:"center"},
            {data:"taskName",bSortable:false,className:"center"},
            {data: "corpName", bSortable: false, className: "center",render:function (data, type, full, meta) {
                    return '<div class="table-ellipsis" title="'+data+'">'+data+'</div>'
                }},
            {data:"orgType",bSortable:false,className:"center"},
            {data: "orgName", bSortable: false, className: "center",render:function (data, type, full, meta){
                    return '<div class="table-ellipsis" title="'+data+'">'+data+'</div>'
                }},
            {data:"evaluateStartDate",bSortable:true,className:"center",
                render: function (data, type, full, meta) {
                    if(full.evaluateStartDate){
                        return bt.date.format(full.evaluateStartDate, 'yyyy-MM-dd');
                    }
                    return "";
                }
            },
            {data:"evaluateEndDate",bSortable:true,className:"center",
                render: function (data, type, full, meta) {
                    if(full.evaluateEndDate){
                        return bt.date.format(full.evaluateEndDate, 'yyyy-MM-dd');
                    }
                    return "";
                }
            },
            {data:"taskCycle",bSortable:false,className:"center",
                render: function (data, type, full, meta) {
                    if(full.taskCycle){
                        return bt.dict("credit.crTaskInfo.cycle",full.taskCycle);
                    }
                    return "";
                }
            },
            {data:"evaluator",bSortable:false,className:"center"},
            {
                data: "status", bSortable: false, className: "center", render: function (data, type, full, meta) {
                    return bt.dict("crTaskInfo.status", full.status);
                }
            },
            {data:null,bSortable:false,className:"center",
                render: function (data, type, full, meta) {
                    var html = '';
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
                    /*if(full.status  == 0){
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="evaluation(this)">' + bt.lang("evaluationAnalysis.button.evaluate") + '</a> ';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="view(this)">' + bt.lang("common.button.view") + '</a> ';
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="queryLog(this)">' + bt.lang("common.button.logInfo") + '</a>';
                    return html;*/
                }
            }
        ]
    });
});

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/credit/evaluationAnalysis/view/" + data.id + "/" + data.taskNo + "/" + data.corpId);
        return;
    }

    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/evaluationAnalysis/view/" + data.id + "/" + data.taskNo + "/" + data.corpId
    });
}

function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/credit/evaluationAnalysis/approve?id="+data.id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/credit/evaluationAnalysis/approve?id="+data.id + "&taskId=" + data.taskId
    });
}

