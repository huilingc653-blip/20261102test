bt.use(['form','datatable','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    // 生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'rtlip',
        colReorder: true,
        checkbox:false,order: [0,'desc'],
        ajax: {
            'url': basePath+"/pricing/depositRateType/interestRateType/datatable"
        },
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
        buttons: [
            {
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            }
        ],
        columns: [
            {data:"code",className:'center',bSortable:true},
            {data:"name",className:'center',bSortable:true},
            {data:"depositClass",className:'center',bSortable:true,
                render: function ( data, type, full, meta ) {
                    return bt.dict("pricing.deposit.type",full.depositClass);
                }
            },
            {data:"period",className:'center',bSortable:true,
            	render: function ( data, type, full, meta ) {
                   if(full.periodCycle==null || full.periodCycle==""){
                       if(full.period==null){
                           return "";
                       }else{
                           return bt.dict("pricing.noticeType",full.period);
                       }
                   }else{
                	   return full.period+bt.dict("pricing.fixedTerm",full.periodCycle);
                   }
                }	
            },
            {data:"depositAccSign",className:'center',bSortable:true,
                render: function ( data, type, full, meta ) {
                    if(full.depositAccSign==null){
                        return bt.dict("pricing.depAccSign",2);//存量数据，默认为资金池内部账户
                    }else{
                        return bt.dict("pricing.depAccSign",full.depositAccSign);
                    }
                }
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            		var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>\
                    <a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>\
                    <a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
                return html;
            }}
        ],
        createdRow:function(row,data,index){
            if (data.isSysvalue ==1) {
                $('td',row).eq(5).css('color','blue');
              }
            }
    });
});
function add(){
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/pricing/depositRateType/interestRateType/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/pricing/depositRateType/interestRateType/add",
        end:function(){
        	bt.navtab.refreshCurrentTable();
        }
    });
}
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '350px'],
        maxmin: true,
        content: basePath + "/pricing/depositRateType/interestRateType/view/" + data.id
    });
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/pricing/depositRateType/interestRateType/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/pricing/depositRateType/interestRateType/update?id=" + data.id,
            end:function(){
            	bt.navtab.refreshCurrentTable();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/pricing/depositRateType/interestRateType/delete/" + data.id, function(result){
        	bt.navtab.refreshCurrentTable();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}