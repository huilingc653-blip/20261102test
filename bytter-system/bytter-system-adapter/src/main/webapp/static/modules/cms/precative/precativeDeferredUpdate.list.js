bt.use(['form','datatable','corp','money'], function(){
    $ = bt.jquery,form = bt.form();
    
    bt.corp.init();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
       
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/cms/precative/precativeloandeferred/dataTableUpdate?pageType="+$("#pageType").val()
        },
        columns: [
            {data:"billCode",bSortable:true,className:'center'},
            {data:"sendNo",bSortable:true,className:'center'},
            {data:"loanOppName",bSortable:false,className:'center'},
            /*{data:"expiryHandleType",bSortable:false,className:'center',
            	render : function(data,type,full,meta){
            		return bt.dict("provide.deferredHandleType",full.expiryHandleType)
            	}
            },*/
            {data:"deferredStartDate",bSortable:true,className:'center'},
            {data:"deferredRepayDate",bSortable:true,className:'center'},
            {data:"deferredLimt",bSortable:false,className:'center',
            	render : function(data,type,full,meta){
                    if (full.ternUnit == 'D') {
                        return full.deferredLimt + bt.lang("cms.loan.ternUnit.day");
                    } else if (full.ternUnit == 'M') {
                        return full.deferredLimt + bt.lang("cms.loan.ternUnit.month");
                    } else {
                        return full.deferredLimt;
                    }
            	}
            },
           /* {data:"deferredNum",className:'center',bSortable:false,className:'center',
               	render: function ( data, type, full, meta ) {
               		if(full.deferredNum>0){
               			return "<a href='javascript:void(0)' onclick='sendInfolist(\""+full.sendNo+"\")' ><u>"+full.deferredNum+"</u></a>";
               		}else{
               			return full.deferredNum;
               		}
                }
            },*/
            {data:"bizStatus",bSortable:false,className:'center',
            	render : function(data,type,full,meta){
            		return bt.dict("cms.bizStatus",full.bizStatus)
            	}
            },
            {data:null,bSortable:false,className:'center',
            	render: function (data, type, full, meta) {
	                var html = '';
	                html +='<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
	                if(bt.isResource('cms:precative:deferred:update:edit')){
		                html += '\<span class="text-explode">|</span>\<a class="" href="#" data-row="'+meta.row+'" onclick="Apply(this)">'+bt.lang("common.button.edit")+'</a>';     
	                }
	                if(bt.isResource('cms:precative:deferred:delete')){
	                	html +='\<span class="text-explode">|</span>\<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a>';
	                }
	                html +=' \<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';
	                // html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="bt.trackFlowByBusinessId(\''+full.id+'\')">'+bt.lang("common.button.trackFlow")+'</a>';
	                return html;
            	}
            }
        ]
    });
});

function Apply(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.deferred.update'),basePath + "/cms/precative/precativeloandeferred/updatePage?voucherNo="+ data.voucherNo+"&sendNo="+data.sendNo+"&billCode="+data.billCode);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/precative/cmsprecativeloandeferred/update?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}

function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.button.view'),basePath + "/cms/precative/precativeloandeferred/view?sendBillCode=" + data.sendNo +"&billCode="+data.billCode);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/cms/precative/precativeloandeferred/view?sendBillCode=" + data.sendNo+"&billCode="+data.billCode
        
    });
}

function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/cms/precative/precativeloandeferred/delete/" + data.id, function(result){
            datatable && datatable.draw();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

function listMsg(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/basedata/cmsprocessmsg/list?billCode=" + data.billCode);
            return;
        }
	}
}
//展期次数
function sendInfolist(billCode){
	if(billCode){
		if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.view.data'),basePath+"/cms/precative/precativeloandeferred/deferredInfolist?liCode="+billCode);
            return;
        }
	}
}