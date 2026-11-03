bt.use(['form','datatable','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'rtlip',
        colReorder: true,
        checkbox:false,order: [0,'desc'],
        ajax: {
            'url': basePath+"/period/tbcreditperiod/datatable"
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
            {data:"periodName",className:'center',bSortable:true},
            {data:"period",className:'center',bSortable:true,
            	render: function ( data, type, full, meta ) {
                   if(full.period==null || full.period===""){
                	   return "N"+bt.dict("cms.periodCycle",full.periodCycle);
                   }else if(parseInt(full.period)==0){
                	   return bt.lang("cms.periodCycle.seven")
                   }else{
                	   return full.period+bt.dict("cms.periodCycle",full.periodCycle);
                   }
                }	
            },
            {data:"loanClass",className:'center',bSortable:true,
            	render: function ( data, type, full, meta ) {
                    return bt.dict("cms.LoanClass",full.loanClass);
                }	
            },
            /*{data:"moneyType",className:'center',bSortable:true,
            	render: function ( data, type, full, meta ) {
                    return bt.dict("cms.creditPeriod.moneyType",full.moneyType);
                }
            },*/
            /*{data:"isSysvalue",className:'center',bSortable:true,
            	render: function ( data, type, full, meta ) {
            		if(full.isSysvalue==1){
            			return bt.lang("cms.TbCreditPeriod.issysvalue");
            		}else{
            			return bt.lang("cms.TbCreditPeriod.isnotsysvalue");
            		}
                }
            },*/
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
//            	if(full.isSysvalue==1){
//            		var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
//            	}else{
            		var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>\
                    <a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a>\
                    <a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
//            	}
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
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/period/tbcreditperiod/add");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/period/tbcreditperiod/add",
        end:function(){
        	bt.navtab.refreshCurrentTable();
        }
    });
}
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/period/tbcreditperiod/view/" + data.id
    });
}
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/period/tbcreditperiod/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/period/tbcreditperiod/update?id=" + data.id,
            end:function(){
            	bt.navtab.refreshCurrentTable();
            }
        });
    }
}
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/period/tbcreditperiod/delete/" + data.id, function(result){
        	bt.navtab.refreshCurrentTable();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}