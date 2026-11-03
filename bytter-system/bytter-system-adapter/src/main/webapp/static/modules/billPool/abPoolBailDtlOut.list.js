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
        checkbox:false,
        order: [[4,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/billPool/bail/datatable/2"
        },
        buttons: [
            {
                text: bt.lang('common.button.distill'),
                action: function ( e, dt, node, config ) {
                    deposit();
                }
            }
        ]
        ,columns: [
            {data:"corpCodeAndName",bSortable:true},
            {data:"bankAcc",bSortable:true},
            {data:"money",bSortable:true,className:'textRight',
                render: function ( data, type, full, meta ) {
                    return bt.money.format(data);
                }
            },
            {data:"oppAcc",bSortable:true},
            {data:"appDate",bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(data){
                        return bt.date.format(data, "yyyy-MM-dd");
                    }
                    return '';
                }
            },
            {data:"pbCodeAndName",bSortable:true},
            {data:"statusName",bSortable:true},
            {data:"optStatusName",bSortable:true},
            {data:"returnMsg",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html='';
                    //交易失败的可重新发送或中止
                    if(data.optStatus==-1){
                        html = html+'<a class="" href="#" data-row="'+meta.row+'" onclick="operation(this,16)">'+bt.lang("common.button.resend")+'</a>';
                        html = html+' | <a class="" href="#" data-row="'+meta.row+'" onclick="operation(this,-2)">'+bt.lang("common.button.tovoid")+'</a>  | ';
                    }
                    html=html + '<a class="layui-btn" href="#" data-row="'+meta.row+'" onclick="dtlView(this)">'+bt.lang("common.button.view")+'</a>';
                    return html;
                }
            }
        ],
        summary:{
            pageSum:true,
            selectSum:true,
            sumList:[
                {
                    colNum:'3',
                    dataName:'money'
                }
            ]
        }
    });
});

// 提取界面跳转
function deposit(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.distill'),basePath + "/billPool/bail/extractAmount");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/billPool/bail/extractAmount",
        end:function(){
//        	billTable.draw();
            bt.datatable.refreshCurrentPage();
        }
    });
}

//保证金详情页面
function dtlView(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    var param;
    if(data.id){
        param = "?id="+data.id;
    }
    parent.layer.open({
        type: 2,
        area: ['1080px', '720px'],
        maxmin: true,
        content: basePath + "/billPool/bail/viewBail/2"+param,
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
}

//操作   重发、中止
function operation(el,status){
    var data = datatable.row(layui.jquery(el).data("row")).data();
    var msg;
    if(status==16){
        msg = "common.message.confirm.bisquerycmd.resend";
    }else if(status==-2){
        msg = "common.message.confirm.tovoid";
    }
    parent.layer.confirm(bt.lang(msg),function(){
        bt.loading.show();
        $.post(basePath + "/bill/pool/abbillInstructionManage/operation",{"id":data.id,"type":"poolBail","status":status}, function(result){
            bt.loading.hide();
            if(result.success){
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
                bt.datatable.refreshCurrentPage();
            }else{
                parent.layer.alert(bt.lang(result.msg), {icon: result.success?1:2});
            }
        },'JSON');
    });
}
