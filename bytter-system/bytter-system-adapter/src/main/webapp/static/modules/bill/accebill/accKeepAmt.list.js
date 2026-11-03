var datatable;
bt.use(['form','datatable','dicts','dateType','select','corp','btImport','mulselect','moneyType'], function(){
    $ = bt.jquery,form = bt.form();
    var moneyType = bt.moneyType.init();
    var moneyFun = bt.moneyType.getFun();
    bt.dicts.init();
    bt.corp.init();
    bt.mulselect.init();

    importExcel = function () {
        up = bt.btImport.init({
            code: "20000006",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) {
                $.ajax({
                    type: "POST",
                    url: basePath + "/bill/accebill/accKeepAmt/saveImport",
                    dataType: "json",
                    contentType: "application/json",
                    data: JSON.stringify(up.getSelectData()),
                    success: function (result) {
                        bt.datatable.refreshCurrentPage();
                        parent.layer.alert(result.msg, {icon: result.success ? 1 : 2});
                    }
                });
            }
        });
    }

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/accebill/accKeepAmt/datatable"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            },{
                text: bt.lang('批量禁用'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    batchForbidden(data);
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data:"corpName",bSortable:false},
            {data:"bankAcc",bSortable:false},
            {data:"bankTypeName",bSortable:false},
            {data:"bankName",bSortable:false},
            {data:"keepAmt",bSortable:false,className:'textRight',render: function (data, type, full, meta) {
                if(null != full.keepAmt && "" != full.keepAmt){
                    return moneyFun.format(full.keepAmt);
                }else{
                    return '';
                }
                }},
            {data:"status",bSortable:false,className:'center',
                render: function ( data, type, full, meta ) {
                    return bt.dict("status_yn",full.status);
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html ='';
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>';
                    if (full.status == '0'){
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
                    }
                    return html;
                }}
        ]
    });

});
function add(){
    parent.layer.open({
        type: 2,
        area: ['1080px', '550px'],
        maxmin: true,
        content: basePath + "/bill/accebill/accKeepAmt/add",
        end:function(){
            bt.navtab.refreshCurrentTable();
        }
    });
}

function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        parent.layer.open({
            type: 2,
            area: ['1080px', '550px'],
            maxmin: true,
            content: basePath + "/bill/accebill/accKeepAmt/update?id=" + data.id,
            end:function(){
                bt.navtab.refreshCurrentTable();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/bill/accebill/accKeepAmt/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}


function batchForbidden(data) {
    var date = document.getElementById("date");
    var len = data.length;
    if (!len) {
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0, len);
    var ids;
    for (i = 0; i < len; i++) {
        if (ids) {
            ids = ids + "," + rowData[i].id;
        } else {
            ids = rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('您确认要批量禁用吗'), function () {
        $.post(basePath + "/bill/accebill/accKeepAmt/batchForbidden", {
            "ids": ids
        }, function (result) {
            parent.layer ? parent.layer.alert(result.msg, {icon: result.success ? 1 : 2}) : layer.alert(result.msg, {icon: result.success ? 1 : 2});
            if (result.success) {
                bt.datatable.refreshCurrentPage();
            }

        }, 'JSON');
    });
}
