var datatable;
bt.use(['form','datatable','dateType','select','corp','money','moneyType','bank','btImport','upload','dicts','selectType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    var dicts = bt.dicts.init();
    bt.moneyType.init();
    bt.dateType.init();
    selectTypes = bt.selectType.init();//下拉框
   
  //下拉框
    $.each($("div[type='list']"),function(i,item){
        bt.select.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            defaultVal: $(item).attr("defaultVal"),
            valueField: $(item).attr("dictField"),
            textField: $(item).attr("dictText"),
            search: true,
            disabled: false,
            url: $(item).attr("fieldhref")
        });
    });
    
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
            'url': basePath+"/letter/guarantee/guaGuaranteeChangeHandle/datatable" 
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
                  {data:"guaranteeNo",className:'center',bSortable:true},
                  {data:"corpName",bSortable:true,className:'center'},
                  {data:"guaraChangeNo",className:'center',bSortable:true},
                  {data:"openCorpName",className:'center',bSortable:true},
                  {data:"changeDate",className:'center',bSortable:true},
                  {data:"changeCount",className:'center',bSortable:true},
                  {data:"status",bSortable:true,className:'center',
                      render: function ( data, type, full, meta ) {
                          return bt.dict("common.status",full.status);
                      }
                  },
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html ='';
                if(bt.isResource('gua:guarantee:changehandle:query',full.corpId)){//查看权限
                	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a><span class="text-explode"></span>'+'</a>&nbsp;|&nbsp;';
                 }
               if(bt.isResource('gua:guarantee:changehandle:apply',full.corpId)){//更改和拒绝权限
                	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="changeColumn(this)">'+bt.lang("common.button.changeLg")+'</a>'+'</a>&nbsp;|&nbsp;'; 
                	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="refuseColumn(this)">'+bt.lang("common.button.refuse")+'</a>'; 
              	}
                  return html;
            }}
        ]
    });
});



//查看保函更改信息
function view(el){
	var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
	if(data){
	        if(bt.navtab){
	            bt.navtab.addNavtab(bt.lang('common.button.view'),basePath + "/letter/guarantee/guaGuaranteeChangeInfo/view/" + data.id);
	            return;
	        }
	}
}

//更改
function changeColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.change'),function(){
        $.post(basePath + "/letter/guarantee/guaGuaranteeChangeHandle/submit/" + data.id, function(result){
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}

//拒绝更改
function refuseColumn(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    parent.layer.confirm(bt.lang('common.message.confirm.refuse'),function(){
        $.post(basePath + "/letter/guarantee/guaGuaranteeChangeHandle/refuse/" + data.id, function(result){
            bt.datatable.refreshCurrentPage();
            parent.layer.alert(result.msg, {icon: result.success?1:2});
        },'JSON');
    });
}