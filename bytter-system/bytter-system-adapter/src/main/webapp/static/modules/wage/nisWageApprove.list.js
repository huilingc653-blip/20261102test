var datatable;
bt.use(['form','datatable','select','dateType','corp','money','moneyType','dicts','btImport','bank'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.moneyType.init();
    bt.dateType.init();
    bt.dicts.init();
    bt.bank.init({
        elem:'.bank',
    });
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/wage/niswage/dataTableApprove"
        },
        buttons: [{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data:"wageNo",bSortable:true,className:'center'},
            {data:"wageTitle",bSortable:true},
            {data:"corpName",bSortable:true},
            {data: "payerAcc", bSortable: true,
                render: function (data, type, full, meta) {
                    return '<div title="可用余额： ' + bt.money.format(full.availBal) + ' "> '+ data || "" +' </div>'
                }
            },
            {data:"payerAccName",bSortable:true},
            {data:"curName",bSortable:true},
            {data:"wages",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                	return bt.money.format(full.wages);
                }
            },
            {data:"wageDay",bSortable:true,className:'center'},
            {data:"wishPayday",bSortable:true,className:'center'},
            {data:"addWord",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
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
            }}
        ],
        summary:{
            pageSum:false,
            allSum:true,
            selectSum:true,
            format:{
            	decimal:2
            },
            sumList:[
                {
                    colNum:'7',
                    dataName:'wages'
                }
            ]
        }
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

function view(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.view.data'), basePath + "/wage/niswage/view/" + data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['950px', '530px'],
        maxmin: true,
        content: basePath + "/wage/niswage/view/" + data.id
    });
}

/*审核*/
function approve(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve'), basePath + "/wage/niswage/approve?id=" + data.id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/wage/niswage/approve?id=" + data.id + "&taskId=" + data.taskId
    });
}