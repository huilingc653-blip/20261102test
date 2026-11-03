var datatable;
bt.use(['form','datatable','money'], function(){
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
            'url': basePath+"/receipt/eexchangebill/datatable"
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
            {data:"voucherNo",bSortable:true},
            {data:"payerName",bSortable:true},
            {data:"payeeName",bSortable:true},
            {data:"payMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.payMoney);
                }
            },
            {data:"curId",bSortable:true},
            {data:"tradeRate",bSortable:true},
            {data:"payType",bSortable:true},
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
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/receipt/eexchangebill/add");
        return;
    }
    // parent.layer.open({
    //     type: 2,
    //     area: ['850px', '530px'],
    //     maxmin: true,
    //     content: basePath + "/receipt/eexchangebill/add",
    //     end:function(){
    //         datatable && datatable.draw();
    //     }
    // });
    var content = basePath + "/receipt/eexchangebill/add";
    openLayer && openLayer(content,undefined,undefined,function () {
        datatable && datatable.draw();
    })
}
function view(el,opType){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var zIndex = 100;
    if(opType == "1"){
        zIndex = -100;
    }
    // parent.layer.open({
    //     type: 2,
    //     area: ['850px', '530px'],
    //     zIndex: zIndex,
    //     maxmin: true,
    //     content: basePath + "/receipt/eexchangebill/view/" + data.id+"/"+opType
    // });

    var content = basePath + "/receipt/eexchangebill/view/" + data.id+"/"+opType;
    var shade = opType == "1"?0:0.3;
    openLayer && openLayer(content,zIndex,shade)
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/receipt/eexchangebill/update?id=" + data.id);
            return;
        }
        // parent.layer.open({
        //     type: 2,
        //     area: ['850px', '530px'],
        //     maxmin: true,
        //     content: basePath + "/receipt/eexchangebill/update?id=" + data.id,
        //     end:function(){
        //         datatable && datatable.draw();
        //     }
        // });


        var content = basePath + "/receipt/eexchangebill/update?id=" + data.id;

        openLayer && openLayer(content,undefined,undefined,function () {
            datatable && datatable.draw();
        })
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/receipt/eexchangebill/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
