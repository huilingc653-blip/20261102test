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
            'url': basePath + "/billpool/limitAllotCheck/dataTableApprove"
        }
        , columns: [
            {data: "corpName", bSortable: true,
                render: function ( data, type, full, meta ) {
                    return '('+full.corpCode+')'+full.corpName;
                }
            },
            {data: "createTime", bSortable: true},
            {data: "limtTypeName", bSortable: true},
            {data: "poolMoney", bSortable: true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.poolMoney);
                }},
            {data: "lockMoney", bSortable: true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.lockMoney);
                }},
            {data: "validMoney", bSortable: true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.validMoney);
                }},
            {data: "applyMoney", bSortable: true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.applyMoney);
                }},
            {data: "rmk", bSortable: true},
            {data: "parentName", bSortable: true},
            {data: "bustypeLevel", bSortable: true},
            {data: "bpName", bSortable: true},
            {data: "checkTypeName", bSortable: true},
            {data: "checkMsg", bSortable: true},
            {data: "returnMsg", bSortable: true},
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
                    dataName:'poolMoney'
                },{
                    colNum:'5',
                    dataName:'lockMoney'
                },{
                    colNum:'6',
                    dataName:'validMoney'
                },{
                    colNum:'7',
                    dataName:'applyMoney'
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
        bt.navtab.addNavtab(bt.lang('common.button.approve'), basePath + "/billpool/limitAllotCheck/approve?businessId=" + id + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/billpool/limitAllotCheck/approve?businessId=" +  data.id + "&taskId=" + data.taskId
    });
}
