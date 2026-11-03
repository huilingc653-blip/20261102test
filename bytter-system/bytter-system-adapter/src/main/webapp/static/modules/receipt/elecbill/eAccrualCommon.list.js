var datatable;
bt.use(['form','datatable','date','money'], function(){
    $ = bt.jquery,form = bt.form();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,order: [1,'desc'],
        searchForm: {
            formElem: "#commonForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/receipt/eaccrualbill/datatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"billNo",bSortable:true},
            {
                data: "voucherNo", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = "";
                    if (full.voucherNo != "" && full.voucherNo != null) {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewVoucher(this)">' + full.voucherNo + '</a>';
                    }
                    return html;
                }
            },
            {data:"billType",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return full.billTypeName;
                }
            },
            {data:"accNo",bSortable:true},
            {data:"accName",bSortable:true},
            {data:"otherSubject",bSortable:true},
            {data:"homeSubject",bSortable:true},
            {data:"accNo",bSortable:true},
            {data:"accName",bSortable:true},
            {data:"otherSubject",bSortable:true},
            {data:"homeSubject",bSortable:true},
            {data:"accrual",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.accrual);
                }
            },
            {data:"startDate",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.date.format(full.startDate, "yyyy-mm-dd")
                }
            },
            {data:"endDate",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.date.format(full.endDate, "yyyy-mm-dd")
                }
            },
            {data:"tallyDate",bSortable:true},
            {data:"prints",bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(full.prints == 1){
                        return bt.lang('common.sc_yn.no');
                    }else{
                        return bt.lang('common.sc_yn.yes');
                    }
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this,0)">'+bt.lang("common.button.preview")+'</a><span class="text-explode">|</span>';
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this,1)">'+bt.lang("common.button.print")+'</a>';
                return html;
            }}
        ]
    });
});
function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/receipt/eaccrualbill/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/receipt/eaccrualbill/add",
        end:function(){
            datatable && datatable.draw();
        }
    });
}
function view(el,opType){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var zIndex = 100;
    if(opType == "1"){
        zIndex = -100;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        zIndex: zIndex,
        maxmin: true,
        content: basePath + "/receipt/eaccrualbill/view/" + data.id+"/"+opType
    });
}
function viewVoucher(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/finacial/accbook/fcaccbook/viewCommon/" + data.voucherNo
    });
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/receipt/eaccrualbill/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/receipt/eaccrualbill/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/receipt/eaccrualbill/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}