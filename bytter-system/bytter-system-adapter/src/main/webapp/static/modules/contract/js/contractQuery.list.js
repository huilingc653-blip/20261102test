var billTable={},corp;
bt.use(['form','datatable','btdate','conType','select','corp','dateType','dicts','selectType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    bt.conType.init();
    bt.dateType.init();
    bt.selectType.init();
    corp = bt.corp.init();

    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        checkbox:true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/contract/suspend/queryDatatable"
        },
        buttons: [
            {
                extend: "colvis",
                text: bt.lang('common.button.theadHide'),
                columns: ':not(:first):not(:last)'
            }
        ]
        ,columns: [
                   {data:"corpName",bSortable:true,
                       render: function ( data, type, full, meta ) {
                       	return '('+full.corpCode+')'+full.corpName;
                       }
                   },
                   {data:"conName",bSortable:true},
                   {data:"typeName",bSortable:true},
                   {data:"conNo",bSortable:true},
                   {data:"signTypeName",bSortable:true,
                       render: function (data, type, full, meta) {
                           return bt.dict("CON_SIGN_TYPE", full.signType);
                       }},
                   {data:"draftDate",bSortable:true},
                   {data:"signDate",bSortable:true},
                   {data:"beginDate",bSortable:true},
                   {data:"endDate",bSortable:true},
                   {data:"opDate",bSortable:true},
                   {data:"ciStatusName",bSortable:true,
                       render: function (data, type, full, meta) {
                           return bt.dict("CON_INFO_STATUS", full.ciStatus);
                       }},
                   {data:null,bSortable:true,className:'center',
                	   render: function (data, type, full, meta) {
	                	   if(null!=data.performanceNum && data.performanceNum>0){
	    					   var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="viewNum(this,\'perfor\')">'+data.performanceNum+'</a>';
	                		   return html;
	                	   }else{
	                		   return null;
	                	   }
                   }},
                   {data:null,bSortable:true,className:'center',
                	   render: function (data, type, full, meta) {
	                	   if(null!=data.changeNum && data.changeNum>0){
	    					   var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="viewNum(this,\'change\')">'+data.changeNum+'</a>';
	                		   return html;
	                	   }else{
	                		   return null;
	                	   }
                   }},
                   {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
					   var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
					   return html;
				   }}
        ],
        fixedColumns:{
        	rightColumns:2
        }
    });
    //时间
    $.each($("div[type='date']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD",
            istime: false
        });
    });
    $.each($("div[type='datetime']"),function(i,item){
        bt.btdate.init({
            elem: "[field='"+$(item).attr("field")+"']",
            name: $(item).attr("field"),
            start: $(item).attr("defaultVal"),
            format: "YYYY-MM-DD hh:mm:ss",
            istime: true
        });
    });

});

//查看履约，变更详细
function viewNum(el,opt){
	var data = billTable.row(layui.jquery(el).data("row")).data();
    var aid = data.mid;

	if(bt.navtab){
		  bt.navtab.addNavtab(bt.lang('common.button.view'),basePath + "/contract/suspend/conNumView/"+opt+"?id=" + aid);
		  return;
	}

	parent.layer.open({
	  type: 2,
	  area: ['1080px', '720px'],
	  maxmin: true,
	  content: basePath + "/contract/suspend/conNumView/"+opt+"?id=" + aid
	});
}

//查看详细
function view(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var aid = data.mid;

	if(bt.navtab){
		  bt.navtab.addNavtab(bt.lang('common.button.view'),basePath + "/contract/conCommon/conView?id=" + aid);
		  return;
	}

	parent.layer.open({
	  type: 2,
	  area: ['1080px', '720px'],
	  maxmin: true,
	  content: basePath + "/contract/conCommon/conView?id=" + aid
	});

}

function corpCallback(ids) {

}
