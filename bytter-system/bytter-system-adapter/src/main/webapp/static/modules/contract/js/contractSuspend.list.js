var billTable={};
bt.use(['form','datatable','btdate','select','conType','dateType','dicts','selectType'], function(){
    $ = bt.jquery,form = bt.form();
    bt.dicts.init();
    bt.conType.init();
    bt.dateType.init();
    bt.selectType.init();

    //生成列表
    billTable = bt.datatable.init({
        elem: "#appTable",
        dom: 'Brtlip',
        colReorder: true,
        order: [[1,'desc']],
        searchForm: {
            formElem: "#searchForm",
            formFilter: "search"
        },
        ajax: {
            'url': basePath+"/contract/suspend/datatable"
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
                   {data:"beginDate",bSortable:true},
                   {data:"endDate",bSortable:true},
                   {data:"statusName",bSortable:true,
                       render: function (data, type, full, meta) {
                           return bt.dict("CON_TERMINATE_STATUS", full.status);
                       }},
                   {data:"opMsg",bSortable:true},
                   {data:null,bSortable:false,className:'center',render: function (data, type, full, meta) {
					   var html = '<a class="" href="#" data-row="'+meta.row+'" onclick="view(this)">'+bt.lang("common.button.view")+'</a>';
					   if(data.status==1){
						   //终止
						   html = html+'<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="signed(this)">'+bt.lang("contract.suspend.stop")+'</a>';
					   }
					   if(data.status==0 || data.status==-1){
						   //修改
						   html = html+'<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="update(this)">'+bt.lang("common.button.update")+'</a>';
						   //删除
						   html = html+'<span class="text-explode">|</span><a class="" href="#" data-row="'+meta.row+'" onclick="refused(this)">'+bt.lang("common.button.delete")+'</a>';
					   }
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

//终止
function signed(el){
	var data = billTable.row(layui.jquery(el).data("row")).data();
	var aid = data.rid;
    var url = basePath + "/contract/suspend/stopJump?id="+aid+"&opt=stop";
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('contract.suspend.stop.apply'), url);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: url,
        end: function () {
            datatable && datatable.draw();
        }
    })
}

//修改
function update(el){
	var data = billTable.row(layui.jquery(el).data("row")).data();
	var aid = data.rid;
    var url = basePath + "/contract/suspend/stopJump?id="+aid+"&opt=stop";
    if (bt.navtab) {
        bt.navtab.addNavtab(bt.lang('contract.suspend.stop.apply'), url);
        return;
    }
    parent.layer.open({
        type: 2,
        area: ['850px', '530px'],
        maxmin: true,
        content: url,
        end: function () {
            datatable && datatable.draw();
        }
    })
}

//删除
function refused(el){
	 var data = billTable.row(layui.jquery(el).data("row")).data();
	 parent.layer.confirm(bt.lang('common.message.confirm.delete'),function(){
	        $.post(basePath + "/contract/suspend/delCon",{"id":data.rid,"lastId":data.lastId}, function(result){
	        	if(result.success){
	                parent.layer.alert(result.msg, {icon: result.success?1:2});
	        		bt.datatable.refreshCurrentPage();
	        	}else{
	        		parent.layer.alert(bt.lang("contract.del.error"), {icon: result.success?1:2});
	        	}
	        },'JSON');
	 });
}
//查看详细
function view(el){
    var data = billTable.row(layui.jquery(el).data("row")).data();
    var aid = data.rid;

	if(bt.navtab){
		  bt.navtab.addNavtab(bt.lang('common.button.view'),basePath + "/contract/conCommon/conView?recordId=" + aid);
		  return;
	}

	parent.layer.open({
	  type: 2,
	  area: ['1080px', '720px'],
	  maxmin: true,
	  content: basePath + "/contract/conCommon/conView?recordId=" + aid
	});

}
