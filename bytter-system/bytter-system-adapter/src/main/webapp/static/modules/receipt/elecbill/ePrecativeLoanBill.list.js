var datatable;
bt.use(['form','datatable','rateType','money'], function(){
    $ = bt.jquery,form = bt.form();

    var batchPrint0 = {
        text: bt.lang('receipt.button.batchPrint0'),
        className: 'printAll active',
        exportOptions: {
            columns: ':visible'
        },
        action:function (e, dt, node, config) {
            //综合打印
            batchPrint(e, dt, node, config, '0');
        }
    }
    var batchPrint1 = {
        text: bt.lang('receipt.button.batchPrint1'),
        className: 'printAll active',
        exportOptions: {
            columns: ':visible'
        },
        action:function (e, dt, node, config) {
            //A4一页一份（横向）
            batchPrint(e, dt, node, config, '1');
        }
    }
    var batchPrint2 = {
        text: bt.lang('receipt.button.batchPrint2'),
        className: 'printAll active',
        exportOptions: {
            columns: ':visible'
        },
        action:function (e, dt, node, config) {
            //A4一页两份（纵向）
            batchPrint(e, dt, node, config, '2');
        }
    }
    var batchPrint3 = {
        text: bt.lang('receipt.button.batchPrint3'),
        className: 'printAll active',
        exportOptions: {
            columns: ':visible'
        },
        action:function (e, dt, node, config) {
            //A5一页一份（横向）
            batchPrint(e, dt, node, config, '3');
        }
    }
    var batchPrintAll = {
        extend: 'collection',
        text: bt.lang('common.button.batchPrint'),//按钮名称,
        buttons: [
            batchPrint0,
            batchPrint1,
            batchPrint2,
            batchPrint3
        ],
        fade: true
    };
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
            'url': basePath+"/receipt/eprecativeloanbill/datatable"
        },
        buttons: [
            batchPrintAll,
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
            {
                data: "oldVoucher", bSortable: true, className: 'center',
                render: function (data, type, full, meta) {
                    var html = "";
                    if (full.oldVoucher != "" && full.oldVoucher != null) {
                        html = '<a class="" href="#" data-row="' + meta.row + '" onclick="viewOldVoucher(this)">' + full.oldVoucher + '</a>';
                    }
                    return html;
                }
            },
            {data:"billType",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return full.billTypeName;
                }
            },
            {data:"precativeCorp",bSortable:true},
            {data:"precativeAcc",bSortable:true},
            {data:"loanCorp",bSortable:true},
            {data:"loanAcc",bSortable:true},
            {data:"loanRate",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.rateType.getFun().format(full.loanRate);
                }
            },
            {data:"loanDate",bSortable:true},
            {data:"curId",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return full.curName;
                }
            },
            {data:"contractCode",bSortable:true},
            {data:"corpId",bSortable:true,
                render: function ( data, type, full, meta ) {
                    return full.corpName;
                }
            },
            {data:"repaymentDate",bSortable:true},
            {data:"loanMoney",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(full.loanMoney);
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
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/receipt/eprecativeloanbill/add");
        return;
    }
    // parent.layer.open({
    //     type: 2,
    //     area: ['850px', '530px'],
    //     maxmin: true,
    //     content: basePath + "/receipt/eprecativeloanbill/add",
    //     end:function(){
    //         datatable && datatable.draw();
    //     }
    // });
    var content = basePath + "/receipt/eprecativeloanbill/add";
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
    // layer.open({
    //     type: 2,
    //     area: ['850px', '530px'],
    //     zIndex: zIndex,
    //     maxmin: true,
    //     content: basePath + "/receipt/eprecativeloanbill/view/" + data.id+"/"+opType
    // });

    var content = basePath + "/receipt/eprecativeloanbill/view/" + data.id+"/"+opType;
    var shade = opType == "1"?0:0.3;
    openLayer && openLayer(content,zIndex,shade)
}
function viewVoucher(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    // parent.layer.open({
    //     type: 2,
    //     area: ['850px', '530px'],
    //     maxmin: true,
    //     content: basePath + "/finacial/accbook/fcaccbook/viewCommon/" + data.voucherNo
    // });

    var content = basePath + "/finacial/accbook/fcaccbook/viewCommon/" + data.voucherNo;
    openLayer && openLayer(content)
}
function viewOldVoucher(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var content =  '';
    if(data.billType==21) {
        //委托贷款
        // parent.layer.open({
        //     type: 2,
        //     area: ['850px', '530px'],
        //     maxmin: true,
        //     content: basePath + "/cms/precative/loansend/viewbybillcode?billCode=" + data.oldVoucher
        // });
        content = basePath + "/cms/precative/loansend/viewbybillcode?billCode=" + data.oldVoucher
    }else{
        //委托贷款还款
        // parent.layer.open({
        //     type: 2,
        //     area: ['850px', '530px'],
        //     maxmin: true,
        //     content: basePath + "/cms/precative/loanrepay/view?sendCode=''&type=view"+"&billCode=" + data.oldVoucher
        // });

        content = basePath + "/cms/precative/loanrepay/view?sendCode=''&type=view"+"&billCode=" + data.oldVoucher
    }

    if(content){
        openLayer && openLayer(content)
    }
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/receipt/eprecativeloanbill/update?id=" + data.id);
            return;
        }
        // parent.layer.open({
        //     type: 2,
        //     area: ['850px', '530px'],
        //     maxmin: true,
        //     content: basePath + "/receipt/eprecativeloanbill/update?id=" + data.id,
        //     end:function(){
        //         datatable && datatable.draw();
        //     }
        // });
        var content = basePath + "/receipt/eprecativeloanbill/update?id=" + data.id;
        openLayer && openLayer(content,undefined,undefined,function () {
            datatable && datatable.draw();
        })
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/receipt/eprecativeloanbill/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
