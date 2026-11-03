bt.use(['form','datatable','selectType','dicts','corp','dateType','moneyType','btImport','externalCorpBankAcc'],function () {
    var selectType=bt.selectType.init();
    var dict = bt.dicts.init();
    var corp = bt.corp.init();
    bt.dateType.init();
    bt.moneyType.init();

    var externalCorpBankAcc = bt.externalCorpBankAcc.init();
    bt.externalCorpBankAccParams = function () {
        //供应商默认不查
        externalCorpBankAcc.set({
            "corpId": $("#corpIdHidden").val() ? $("#corpIdHidden").val() : "0",
            type: "2"
        });
        return true;
    }
    //供应商选择
    bt.externalCorpCallback = function (data, dom) {
        if (typeof data.netId === "undefined") { // 选择客商信息
            $("#eqs_payeeAccName").val(data.name);
        } else {  // 选择网点或者成员单位信息
            $("#eqs_payeeAccName").val(data.corpName);
        }
    };


    importExcel = function () {
        up = bt.btImport.init({
            code: "10000061",
            width: "100%",
            height: "100%",
            headRows: 1,
            titleRows: 0,
            success: function (head,data,select) {
                $.ajax({
                    type:"POST",
                    url: basePath + "/capital/record/saveImport",
                    dataType:"json",
                    contentType:"application/json",
                    data:JSON.stringify(up.getSelectData()),
                    success:function(result){
                        bt.datatable.refreshCurrentPage();
                        parent.layer.alert(result.msg, {icon: result.success?1:2});
                    }
                });
            }
        });
    }
    table = bt.datatable.init({
        elem: "#appTable",
        dom: "Brtlip",
        colReorder:true,
        checkbox: true,
        searchForm:{
            formElem: "#searchForm",
            formFilter: "search"
        },
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        ajax: {
            'url': basePath+"/capital/record/datatable"
        },
        buttons:[
            {
                text:bt.lang('common.button.add'),
                action:function (e, dt, node, config) {
                    add();
                }
            },{
                hide: bt.isnotResource('netbank:netBank:nisCapitalRecord:delete'),
                text:bt.lang('common.button.batchDelete'),
                action:function (e, dt, node, config)
                {
                    var data = dt.rows('.selected').data();
                    batchDelete(data);
                }
            },
            {
                text: bt.lang('common.button.imports'),
                action: function ( e, dt, node, config ) {
                    importExcel();
                }
            },
            {
                extend: "colvis",
                text: bt.lang("common.button.theadHide"),
                columns: ':not(:first):not(:last)'
            }
        ],
        columns:[
            {data:"corpName",bSortable:true,className:'center'},
            {data:"payeeAccName",bSortable:true,className:'center'},
            {data:"busType",bSortable:true,className:'center',
                render:function (data, type, full, meta) {
                    return bt.dict("capitalRecord.busType",full.busType);
                }
            },
            {data:"payMoney",bSortable:true,className:'textRight',
                render:function (data, type, full, meta) {
                    return bt.money.format(full.payMoney);
                }
            },
            {data:"wishPayDay",bSortable:true,className:'center'},
            {data:"projectName",bSortable:true,className:'center'},
            {data:"purpose",bSortable:true,className:'center'},
            {data:"voucherType",bSortable:true,className:'center',
                render:function (data, type, full, meta) {
                    return bt.dict("capitalRecord.voucerType",full.voucherType);
                }
            },
            {data:"netName",bSortable:true,className:'center'},
            {data:"contractCode",bSortable:true,className:'center'},
            {data:"contractName",bSortable:true,className:'center'},
            {data:"contractType",bSortable:true,className:'center'},
            {data:"createDate",bSortable:true,className:'center',
                render:function (data, type, full, meta) {
                    if (full.status == 1){
                        if (full.updateDate){
                            return full.updateDate;
                        } else{
                            return full.createDate;
                        }
                    }else{
                        return '';
                    }
                }
            },
            {data:"status",bSortable:true,className:'center',
                render:function (data, type, full, meta) {
                    return bt.dict("capitalRecord.status",full.status);
                }
            },
            {data:"null",bSortable:false,className:'center',render:function (data, type, full, meta) {
                    var html='';
                    if(full.status=='0'){
                        html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode">|</span>';
                        if (bt.isResource('netbank:netBank:nisCapitalRecord:delete', full.corpId)) {
                            html += '<a class="" href="#" data-row="' + meta.row + '" onclick="del(this)">' + bt.lang("common.button.delete") + '</a><span class="text-explode">|</span>';
                        }
                    }
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    return html;
                }}
        ]
    })
});

function add() {
    if (bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath+'/capital/record/add');
        return;
    }
    parent.layer.open({
        type:2,
        area:['850px','530px'],
        maxmin:true,
        content:basePath+'/capital/record/add',
        end:function () {
            table && table.draw();
        }
    });
}

function update(e1) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(e1).data("row")).data();
    if (data){
        if (bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + '/capital/record/update?id=' + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px','530px'],
            manmin: true,
            content: basePath + "/capital/record/update?id=" + data.id,
            end:function () {
                table && table.draw();
            }
        })
    }
}

function view(e1) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(e1).data("row")).data();
    if (data){
        if (bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + '/capital/record/view?id=' + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px','530px'],
            manmin: true,
            content: basePath + "/capital/record/view?id=" + data.id,
            end:function () {
                table && table.draw();
            }
        })
    }
}

function del(e1) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(e1).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function () {
        $.post(basePath + "/capital/record/delete/", {ids:data.id},function (result) {
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg,{icon: result.success?1:2});
        },'JSON');
    })
}

function batchDelete(data) {
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var ids="";
    var rowData = data.splice(0,len);
    for(var i=0;i<rowData.length;i++){
        if (rowData[i].status == 0) {
            if (ids == "") {
                ids += rowData[i].id;
            } else {
                ids += "," + rowData[i].id;
            }
        }else{
            layer.alert(bt.lang('bill.blankvou.bvvouinput.delete.check'), {icon: 7});
            return;
        }
    }
    parent.layer.confirm(bt.lang('common.button.batchDelete'),function(){
        $.post(basePath + "/capital/record/delete",{ids:ids}, function(result){
            table.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
