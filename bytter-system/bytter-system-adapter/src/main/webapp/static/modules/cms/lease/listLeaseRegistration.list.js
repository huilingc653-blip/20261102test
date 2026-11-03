bt.use(['form','datatable', 'dateType','moneyType','btdate','corp','dicts','moneyType', 'btImport'], function(){
    $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    bt.corp.init();
    bt.dicts.init();
    moneyType=bt.moneyType.init();
    bt.dateType.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [1,'desc'],
        ajax: {
            'url': basePath+"/cms/LeaseObjectInfo/datatable?leaseId="+$("#leaseId").val()
        },
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        buttons: [
            {
                hide: bt.isnotResource('gua:contract:info:apply'),//新增权限
                text: bt.lang('common.button.add'),
                action: function (e, dt, node, config) {
                    add();
                }
            },
            {
                hide: bt.isnotResource('netbank:bankAcc:bisAccBal:apply'),
                text: bt.lang('common.button.imports'),
                action: function (e, dt, node, config) {
                    importExcel();
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
            {data:"name",className:'center',bSortable:true},
            {data:"leaseItemCode",className:'center',bSortable:true},
            {data:"leaseItemName",className:'center',bSortable:true},
            {data:"brand",className:'center',bSortable:true,},
            {data:"speModel",className:'center',bSortable:true,},
            {data:"numb",className:'center',bSortable:true},
            {data:"curName",className:'center',bSortable:true,},
            {data:"oldMoney",className:'center',bSortable:true,
                render: function ( data, type, full, meta ) {
                    return moneyFun.format(full.oldMoney);
                }
            },
            {data:"status",className:'center',bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict("inl.info.status",full.status);
                }
            },{data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '';
                    if(bt.isResource('cms:LeaseObjectInfo:edit',full.corpId)&&full.status!='11'){
                            html += ' <a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>|';
                    }
                    if(bt.isResource('cms:LeaseObjectInfo:del',full.corpId)&&full.status!='11'){
                        html += ' <a class="" href="#" data-row="'+meta.row+'" onclick="del(this)">'+bt.lang("common.button.delete")+'</a>|';
                    }
                    if(bt.isResource('cms:LeaseObjectInfo:view',full.corpId)){
                        html += ' <a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
                    }
                    return html;
                }}
        ],
        summary:{
            pageSum: true,//小计功能开启
            selectSum: false,//已选汇总功能开启
            outerData: true,
            //groupColumn: 'curName',//分类汇总列参数
            format: {
                decimal: 2
            },
            sumList: [
                {
                    colNum: '9', //汇总列序号
                    dataName: 'oldMoney' //汇总列数据
                }
            ]
        }
    });
    /**
     * 导入功能
     */
    importExcel = function () {
        var leaseId =$("#leaseId").val();
        up = bt.btImport.init({
            code: "70000005",
            titleRows: 0,
            width: "100%",
            height: "100%",
            headRows: 1,
            success: function (head, data, select) { //点击确认后回调方法，这里导入组件写了success方法这里可以不用此方法
                $.ajax({
                    type: "POST",
                    url: basePath + "/cms/LeaseObjectInfo/saveImport?leaseId="+leaseId,
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
    };
});

//新增
function add(){
    var leaseId =$("#leaseId").val();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/cms/LeaseObjectInfo/add/"+ leaseId);
        bt.navtab.refreshCurrentTable();
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/LeaseObjectInfo/add/"+ leaseId,
        end:function(){
            bt.navtab.refreshCurrentTable();
        }
    });
}
//查看
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/LeaseObjectInfo/view/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/loan/cmsloaninfo/view/" + data.id
        });
    }
}
//删除
function del(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/LeaseObjectInfo/del/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
//修改
function update(el){
    var leaseId =$("#leaseId").val();
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/cms/LeaseObjectInfo/update/" + data.id+"/"+leaseId);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/LeaseObjectInfo/update/" + data.id+"/"+leaseId
        });
    }
}

//打印
function printContract(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('cms.loanContract.print'),basePath + "/cms/loan/cmscontract/print?liCode=" + data.billCode);
            return;
        }
    }
}

