bt.use(['form','datatable','moneyType'], function(){
    $ = bt.jquery,form = bt.form(),moneyFun = bt.moneyType.getFun();
    //生成列表
    datatable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [],
        ajax: {
            'url': basePath+"/cms/loan/cmsloandeferred/datatablePro"
        },
        searchForm:{
            formElem:"#searchForm",
            formFilter:"search"
        },
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
        columns: [
            {data:"corpName",className:'center',bSortable:false},
            {data:"netName",className:'center',bSortable:false},
            {data:"billCode",className:'center',bSortable:false},
            {data:"createDate",className:'center',bSortable:false},
            {data:"liCode",className:'center',bSortable:false},
            {data:"matureDate",className:'center',bSortable:false},
            {data:"deferredRepayDate",className:'center',bSortable:false},
            {data:"deferredLimt",className:'center',bSortable:false,
            	render: function ( data, type, full, meta ) {
            		if(full.ternUnit==2){
            			return full.deferredLimt + bt.lang("cms.loan.ternUnit.day");
            		}else if(full.ternUnit==1){
            			return full.deferredLimt + bt.lang("cms.loan.ternUnit.month");
            		}else{
            			return full.deferredLimt;
            		}
                }
            },
            {data:"contractCode",className:'center',bSortable:false},
            {data:"bizStatus",className:'center',bSortable:false,
            	render: function ( data, type, full, meta ) {
                    return bt.dict("cms.bizStatus",full.bizStatus);
                }	
            },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
            	var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
            	if(bt.isResource('cms:loan:deferredpro:edit',full.corpId)){
            		html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="handle(this)">'+bt.lang("common.button.deferred.handle")+'</a>';
            	}
				html += '<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>';
                return html;
            }}
        ]
    });
});
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
    	if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.view.data'),basePath + "/cms/loan/cmsloandeferred/view/" + data.id);
            return;
        }
    	parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/loan/cmsloandeferred/view/" + data.id
        });
    }
}

function handle(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.deferred.Yes'),basePath + "/cms/loan/cmsloandeferred/handle?id=" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/cms/loan/cmsloandeferred/handle?id=" + data.id,
            end:function(){
                datatable && datatable.draw();
            }
        });
    }
}

//日志列表
function listMsg(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
        if(bt.navtab){
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/basedata/cmsprocessmsg/list?billCode=" + data.billCode);
            return;
        }
	}
}