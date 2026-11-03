var datatable;
bt.use(['form','datatable','dateType','select','corp','dicts'], function(){
    $ = bt.jquery,form = bt.form();
    bt.corp.init();
    var dict = bt.dicts.init();
    bt.dateType.init();
    
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
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/relationbank/indexvaluedtl/datatable" 
        },
        columns: [
            {data:"corpName",bSortable:true,className:'center'},
            {data:"bankTypeName",bSortable:true,className:'center'},
            {data:"calDate",bSortable:true,className:'center'},
            {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
                var html ='';
                if(bt.isResource('bankRelation:relation:value:query',full.corpId)){//查询权限
                	html += '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.search")+'</a><span class="text-explode"></span>';
                }
                	return html;
            }}
        ]
    });
});

//查询
function view(el){
    var data = layui.jquery("#appTable").DataTable().row(layui.jquery(el).data("row")).data();
    if(data){
          if(bt.navtab){
              bt.navtab.addNavtab(bt.lang('common.button.search'),basePath+"/relationbank/indexvaluedtl/searchDtl?id=" + data.id);
              return;
          }
    }
}

