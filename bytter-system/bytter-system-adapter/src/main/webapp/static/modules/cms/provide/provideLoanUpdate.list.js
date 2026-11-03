bt.use(['form','datatable','dateType','dicts','corp'], function(){
    $ = bt.jquery,form = bt.form();
    //时间
    bt.dateType.init();
   
    bt.corp.init();
    
    bt.dicts.init(); 
   
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        ordering:false,
        buttons: [
		  {
		      extend: "colvis",
		      text: bt.lang('common.button.theadHide'),
		      columns: ':not(:first):not(:last)'
		      }
		  ],
          fixedColumns: {
              rightColumns:1//代表冻结右边一列
          },
        ajax: {
            'url': basePath+"/cms/provide/provideloandeferred/tableUpadate?pageType="+$("#pageType").val()
        },
        searchForm:{
            formFilter:"search",
            formElem:"#searchForm"
        },
        
        columns: [
            {data:"corpName",bSortable:true},
            {data:"billCode",bSortable:true},
            {data:"deferredCode",bSortable:true},
            {data:"createDate",bSortable:true},
            {data:"provideCode",bSortable:true},
            {data:"repayDate",bSortable:true},
            {data:"deferredRepayDate",bSortable:true},
            {data:"bizStatus",bSortable:true,
            	render : function(data,type,full,meta){
            		return bt.dict("cms.bizStatus",full.bizStatus);
            	}},
           
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html = '';
                if(bt.isResource('cms:provide:deferred:update:edit')){
                	html +='<a class="" href="#" data-row="'+meta.row+'" onclick=" Application(this)">'+bt.lang("table.Freeze.modify")+'</a>';//修改   	
                }
                if(bt.isResource('cms:provide:deferred:delete')){
                	html +=  '\<span class="text-explode">|</span>\<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';//删除
                }
                html += '\<span class="text-explode">|</span>\<a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';
                // if(full.deferredId != null && full.deferredId != ""){
             	// 	 html += '\<span class="text-explode">|</span>\ <a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.deferredId+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
             	//   }
                return html;
            }}
        ]
    });
 
});
function Application(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){//展期修改
            bt.navtab.addNavtab(bt.lang('common.button.deferred.update'),basePath + "/cms/provide/provideloandeferred/defupdate?provideCode=" + data.provideCode+"&deferredId="+data.deferredId);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/provide/provideloandeferred/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
	
   
}

function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/provide/provideloandeferred/delete/" + data.deferredId, function(result){
        	bt.navtab.refreshCurrentTable();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

//日志列表
function listMsg(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/basedata/cmsprocessmsg/list?billCode=" + data.deferredCode);
            return;
        }
	}
}