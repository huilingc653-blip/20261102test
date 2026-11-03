bt.use(['form','datatable','corp','selectType','moneyType'], function(){
	$ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
	
	//初始化单位
    bt.corp.init();
    //初始化下拉框
    bt.selectType.init();
	
    datatable = bt.datatable.init({
        elem: "#appTable",
        colReorder: true,
        ordering:false,
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/finacial/interest/fcloanrepay/datatable"
        }
        ,columns: [
                   {data:"netName",className:'center',bSortable:true},
                   {data:"corpName",className:'center',bSortable:true},
                   {data:"busType",className:'center',bSortable:true,
                	   render: function ( data, type, full, meta ) {
                		   if(full.busType==1){
                			   return bt.lang("fcloanrepay.busType.three");
                		   }else{
                			   return full.busType;
                		   }
                       }   
                   },
                   {data:"loanAcc",className:'center',bSortable:true},
                   {data:"accName",className:'center',bSortable:true},
                   {data:"curName",className:'center',bSortable:true},
                   {data:"needInterestAcc",className:'center',bSortable:true},
                   {data:"repayAcc",className:'center',bSortable:true},
                   {data:"repayType",className:'center',bSortable:true,
                	   render: function ( data, type, full, meta ) {
                		   if(full.repayType==3){
                			   return bt.lang("fcloanrepay.repayType.three");
                		   }else{
                			   return full.repayType;
                		   }
                       }
                   },
                   {data:"interest",className:'textRight',bSortable:true,
                	   render: function ( data, type, full, meta ) {
                		   return moneyFun.format(full.interest);
                	   }
                   },
                   {data:"enterDate",className:'center',bSortable:true},
                   {data:"status",className:'center',bSortable:true,
                	   render: function ( data, type, full, meta ) {
                		   return bt.dict("common.status",full.status);
                	   }
                   },
                   {data:null,className:'center',bSortable:true,
                	   render: function ( data, type, full, meta ) {
                		   var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang('common.button.edit')+'</a>\
                           <a class="" href="#" data-row="'+meta.row+'" onclick="del(this)">'+bt.lang('common.button.delete')+'</a>';
                           return html;
                	   }
                   },
                   ],
    });
});

//新增
function add(){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.add'),basePath + "/finacial/interest/fcloanrepay/add");
        return false;
	}
}
//删除
function del(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/finacial/interest/fcloanrepay/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
//修改
function update(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.update.data'),basePath + "/finacial/interest/fcloanrepay/update?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/finacial/interest/fcloanrepay/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}

