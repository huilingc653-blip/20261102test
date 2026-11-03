var form,$,billPool,billPoolCorp,datatable,moneyType;
bt.use(['form','billPool','billPoolCorp','dateType','datatable','mulselect','dicts','moneyType'], function () {
    $ = bt.jquery; form = bt.form();
    bt.dateType.init();
    bt.dicts.init();
    bt.mulselect.init();
    billPool = bt.billPool.init();
    billPoolCorp = bt.billPoolCorp.init();
    moneyType = bt.moneyType.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/billpool/collection/approveDatatable"
        },
        buttons: []
        ,columns: [
            {data:"corpCodeAndName",bSortable:true},
            {data:"pbCodeAndName",bSortable:true},
            {data:"origNo",bSortable:true},
            {data:"billTypeName",bSortable:true},
            {data:"billFlagName",bSortable:true},
            {data:"billMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"inoutTypeName",bSortable:true},
            {data:"startDate",bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(data){
                        return bt.date.format(data, "yyyy-MM-dd");
                    }
                    return '';
                }
            },
            {data:"endDate",bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(data){
                        return bt.date.format(data, "yyyy-MM-dd");
                    }
                    return '';
                }},
            {data:"payer",bSortable:true},
            {data:"payee",bSortable:true},
            {data:"acceptor",bSortable:true},
            {data: null, bSortable: false, className: 'center', render: function (data, type, full, meta) {
                    var html = '';
                    if ($("#flowStatus").val() == 'todo') {
                        html += '<a class="" href="#" data-row="' + meta.row + '" onclick="approve(this)">' + bt.lang("common.button.approve") + '</a>&nbsp;|&nbsp;';
                    }
                    html += '<a class="" href="#" data-row="' + meta.row + '" onclick="bt.workFlow.trackFlow(\'' + full.processInstanceId + '\')">' + bt.lang("common.button.trackFlow") + '</a>';
                    return html;
                }
            }
        ],
        summary:{
            pageSum:true,
            selectSum:true,
            sumList:[
                {
                    colNum:'4',
                    dataName:'billMoney'
                }
            ]
        }
    });
});


/*审核*/
function approve(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var param = "dtlId="+data.dtlId+"&taskId=" + data.taskId;
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.approve') ,basePath + "/billpool/collection/viewApprove?"+param );
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/billpool/emitFinaApply/viewApprove?"+param
    });
}
