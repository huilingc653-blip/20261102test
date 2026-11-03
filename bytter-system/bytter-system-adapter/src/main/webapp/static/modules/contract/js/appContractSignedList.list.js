var billTable = {};
bt.use(['form', 'datatable', 'moneyType', 'btdate', 'conType','corp', 'select', 'dateType', 'dicts'], function () {
    $ = bt.jquery, form = bt.form();
    bt.dicts.init();
    bt.corp.init();
    bt.dateType.init();
    bt.conType.init();
    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox: true, order: [[1, 'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath + "/contract/signed/dataTableApprove"
        }
        , columns: [
            {data:"corpName",bSortable:true,
                render: function ( data, type, full, meta ) {
                	return '('+full.corpCode+')'+full.corpName;
                }
            },
            {data:"conName",bSortable:true},
            {data:"typeName",bSortable:true},
            {data:"conNo",bSortable:true},
            {data:"signType",bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.dict("CON_SIGN_TYPE",full.signType);
            }},
            {data:"draftDate",bSortable:true},
            {data:"beginDate",bSortable:true},
            {data:"endDate",bSortable:true},
            {data:"status",bSortable:true,
                render: function (data, type, full, meta) {
                    return bt.dict("CON_DRAFT_STATUS",full.status);
            }},
            {data:"backMsg",bSortable:true},
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
            rightColumns: 1
        }
    });
});


/*审核*/
function approve(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var rid = data.rid;
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('common.button.approve'), basePath + "/contract/signed/approve?businessId=" + rid + "&taskId=" + data.taskId);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/contract/signed/approve?businessId=" +  data.rid + "&taskId=" + data.taskId
    });
}


