var datatable={},mulselect;
bt.use(['form','datatable','corp','dateType','dicts','mulselect'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    bt.dicts.init();
    bt.dateType.init();
    mulselect=bt.mulselect.init();

    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/bill/accebill/abbillsignstrategy/datatable"
        },
        buttons: [
            //新增、批量删除
            {
                text: bt.lang('common.button.add'),
                hide:bt.isnotResource('bill:accebill:sign:query'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                text: bt.lang('common.button.delete'),
                hide:bt.isnotResource('bill:accebill:sign:query'),
                action: function ( e, dt, node, config ) {
                    var data = dt.rows('.selected').data();
                    deleteBatch(data);
                }
            },
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
            {data:"corpCodeAndName",bSortable:true},
            {data:"code",bSortable:true},
            {data:"name",bSortable:true},
            {data:"billTypeName",bSortable:true},
            {data:"signTypeName",bSortable:true},
            {data:"validSignName",bSortable:true},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                    var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>';
                    html = html+'<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
                    return html;
                }}
        ]
    });

});

function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.add'),basePath + "/bill/accebill/abbillsignstrategy/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbillsignstrategy/add",
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
}

function update(el){
    var data = datatable.row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.edit'),basePath + "/bill/accebill/abbillsignstrategy/update/"+data.id);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/bill/accebill/abbillsignstrategy/update/"+data.id,
        end:function(){
            bt.datatable.refreshCurrentPage();
        }
    });
}

function deleteColumn(el){
    var data = datatable.row(layui.jquery(el).data("row")).data();
    var id = data.id;
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/bill/accebill/abbillsignstrategy/batchDel",{"ids":id}, function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    });
}

function deleteBatch(data){
    if(!bt.form().verifyFun('#dataForm')){
        return false;
    }
    var len = data.length;
    if(!len){
        layer.alert(bt.lang('common.message.confirm.selectData'), {icon: 7});
        return;
    }
    var rowData = data.splice(0,len);
    var ids;
    for(i=0;i<len;i++){
        if(ids){
            ids=ids+","+rowData[i].id;
        }else{
            ids=rowData[i].id;
        }
    }
    parent.layer.confirm(bt.lang('common.message.confirm.batchDelete'),function(){
        $.post(basePath + "/bill/accebill/abbillsignstrategy/batchDel",{"ids":ids}, function(result){
            parent.layer?parent.layer.alert(result.msg, {icon: result.success?1:2}):layer.alert(result.msg, {icon: result.success?1:2});
            if(result.success){
                bt.datatable.refreshCurrentPage();
            }
        },'JSON');
    });
}



