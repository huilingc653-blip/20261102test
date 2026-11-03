var billTable={},corp,busTypeJson={};
bt.use(['form','billPool', 'datatable', 'btdate', 'select', 'dateType','mulselect', 'dicts','billPoolCorp','moneyType'],function(){
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.dateType.init();
    bt.billPoolCorp.init();
    bt.mulselect.init();
    billPool = bt.billPool.init();
    billPoolCorp = bt.billPoolCorp.init();
    moneyType = bt.moneyType.init();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[1, 'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/billPool/abSign/dataTableApprove"
        }
        , columns: [
            {data:"corpName",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return '('+full.corpCode+')'+full.corpName;
                }
            },
            {data:"origNo",bSortable:true},
            {data:"billTypeName",bSortable:true},
            {data:"billMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.billMoney);
                }
            },
            {data:"startDate",bSortable:true},
            {data:"endDate",bSortable:true},
            {data:"payer",bSortable:true},
            {data:"payee",bSortable:true},
            {data:"acceptor",bSortable:true},
            {data:"abPoolName",bSortable:true},
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
        fixedColumns: {
            rightColumns: 2
        },
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
    var id = data.id;
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.approve'), basePath + "/billPool/abSign/approve?businessId=" + id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/billPool/abSign/approve?businessId=" +  data.id + "&taskId=" + data.taskId
    });
}
