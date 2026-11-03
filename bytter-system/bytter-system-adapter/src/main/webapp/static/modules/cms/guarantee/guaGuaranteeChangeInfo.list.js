var datatable;
bt.use(['form','datatable','dateType','select','corp','money','moneyType','bank','btImport','upload','dicts','selectType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    var dicts = bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    selectTypes = bt.selectType.init();//下拉框

    
    //生成列表  
    datatable = bt.datatable.init({  
        elem: "#appTable",
        colReorder: true,
        checkbox:true,
        order: [[5,'asc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/letter/guarantee/guaGuaranteeChangeInfo/datatable"
        },
        buttons: [
            {
                hide:bt.isnotResource('gua:guarantee:change:apply'),//新增权限
                text: bt.lang('common.button.add'),
                action: function ( e, dt, node, config ) {
                    add();
                }
            },{
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ],
        fixedColumns: {
            rightColumns:1//代表冻结右边一列
        },
        columns: [
                  {data:"guaranteeNo",className:'center',bSortable:true},
                  {data:"corpName",bSortable:true,className:'center'},
                  {data:"guaraChangeNo",className:'center',bSortable:true},
                  {data:"openCorpName",className:'center',bSortable:true},
                  {data:"incomeCorp",className:'center',bSortable:true},
                  {data:"changeDate",className:'center',bSortable:true},
                  {data:"changeCount",className:'center',bSortable:true},
                  {data:"status",bSortable:true,className:'center',
                      render: function ( data, type, full, meta ) {
                          return bt.dict("guaGuarantee.status",full.status);
                      }
                  },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html ='';
                if(bt.isResource('gua:guarantee:change:apply',full.corpId) && full.status!=11){//更新和删除权限
                    html += '<a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.edit")+'</a><span class="text-explode"></span>';
                	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="deleteColumn(this)">'+bt.lang("common.button.delete")+'</a><span class="text-explode"></span>';
                 }
                if(bt.isResource('gua:guarantee:change:query',full.corpId)){//查看和日志权限权限
                	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a><span class="text-explode"></span>';
                	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="listMsg(this)">'+bt.lang("common.button.logInfo")+'</a>'; 
              	}
                  return html;
            }}
        ]
    });
});

function add(){
	if(bt.navtab){
        bt.navtab.addNavtab(bt.lang('common.add.data'),basePath + "/letter/guarantee/guaGuaranteeChangeInfo/addChange");
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: basePath + "/letter/guarantee/guaGuaranteeChangeInfo/addChange",
        end:function(){
        	datatable && datatable.draw();
        }
    });
}

//查看保函申请详情
function view(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
	        if(bt.navtab){
	            bt.navtab.addNavtab(bt.lang('common.button.view'),basePath + "/letter/guarantee/guaGuaranteeChangeInfo/view/" + data.id);
	            return;
	        }
	}
}

//编辑
function update(el) {
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if (data) {
        if (bt.navtab) {
            bt.navtab.addNavtab(bt.lang('common.update.data'), basePath + "/letter/guarantee/guaGuaranteeChangeInfo/update/" + data.id);
            return;
        }
        parent.layer.open({
            type: 2,
            area: ['850px', '530px'],
            maxmin: true,
            content: basePath + "/letter/guarantee/guaGuaranteeChangeInfo/update/" + data.id,
            end: function () {
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
            bt.navtab.addNavtab(bt.lang('common.button.logInfo'),basePath + "/cms/guarantee/gualog/listlog?guaId=" + data.id);
            return;
        }
  }
}

//删除行
function deleteColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
        $.post(basePath + "/letter/guarantee/guaGuaranteeChangeInfo/delete/" + data.id, function(result){
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}
